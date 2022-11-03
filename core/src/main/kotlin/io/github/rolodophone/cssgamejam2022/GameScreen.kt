package io.github.rolodophone.cssgamejam2022

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.utils.TimeUtils
import io.github.rolodophone.cssgamejam2022.comp.BoxBodyComp
import io.github.rolodophone.cssgamejam2022.comp.InfoComp
import io.github.rolodophone.cssgamejam2022.comp.PlayerComp
import io.github.rolodophone.cssgamejam2022.comp.TextureComp
import io.github.rolodophone.cssgamejam2022.sys.Box2DSys
import io.github.rolodophone.cssgamejam2022.sys.PlayerSys
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.with
import ktx.box2d.body
import ktx.box2d.box

class GameScreen(val game: CSSGameJam2022): KtxScreen {
	val entityPresets = EntityPresets(game)

	lateinit var player: Entity
	lateinit var door: Entity
	lateinit var background: Entity
	lateinit var barriers: List<Entity>
	lateinit var movingBarriers: List<Entity>
	lateinit var movingPlatforms: List<Entity>
	lateinit var platforms: List<Entity>
	lateinit var saws: List<Entity>
	lateinit var leds: List<Entity>
	lateinit var dialog: Entity

	lateinit var playerSys: PlayerSys
	lateinit var box2DSys: Box2DSys

	var currentLevel = 1
	var dialogShown = true
	var firstLevelJustStarted = true
	var levelFailed = false
	var levelComplete = false
	var gameComplete = false
	var gamePaused = true

	var timePlayerTextureLastSwitched = 0L

	override fun show() {
		box2DSys = Box2DSys(game.world)

		player = game.engine.entity {
			with<InfoComp> {
				name = "Player"
				tags = mutableSetOf(InfoComp.Tag.PLAYER)
			}
			with<BoxBodyComp> {
				width = 0.4f
				height = 0.7f
				body = game.world.body {
					type = BodyDef.BodyType.DynamicBody
					fixedRotation = true

					box(width, height, Vector2(width/2f, height/2f))
					box(width, 0.2f, Vector2(width/2f, 0f)) { // foot sensor
						isSensor = true
						userData = 0 //0 means player foot sensor
					}

					userData = this@entity.entity
				}
			}
			with<TextureComp> {
				texture = game.textureAssets.player1
			}
			with<PlayerComp>()
		}

		levels[1].initOneOff(this)
		levels[1].init(this)

		dialog = game.engine.entity {
			with<InfoComp> {
				name = "Dialog"
			}
			with<BoxBodyComp> {
				width = 7.5f
				height = 5f
				body = game.world.body {
					position.set(4.25f, 2f)
					box(width, height, Vector2(width/2f, height/2f)) {
						isSensor = true
						filter.categoryBits = 1
						filter.groupIndex = 1
						filter.maskBits = 1
					}
					userData = this@entity.entity
				}
			}
			with<TextureComp> {
				texture = game.textureAssets.dialog_init
			}
		}

		game.renderSys.dialog = dialog
	}

	override fun render(delta: Float) {
		if (!gamePaused) {
			if (player.getComp(BoxBodyComp.mapper).y < -0.7f) failLevel()

			val currentTime = TimeUtils.millis()
			if (currentTime > timePlayerTextureLastSwitched + 100L) {
				val playerTextureComp = player.getComp(TextureComp.mapper)
				playerTextureComp.texture =
					if (playerTextureComp.texture == game.textureAssets.player1) game.textureAssets.player2
					else game.textureAssets.player1

				timePlayerTextureLastSwitched = currentTime
			}
		}
	}

	fun continueDialog() {
		if (dialogShown && !gameComplete) {
			game.engine.removeEntity(dialog)
			dialogShown = false

			if (levelFailed) {
				levelFailed = false
				restartLevel()
			} else if (levelComplete) {
				nextLevel()
			} else if (firstLevelJustStarted) {
				firstLevelJustStarted = false
				resumeGame()
			}
		}
	}

	fun failLevel() {
		if (!levelFailed && !dialogShown) {
			pauseGame()
			dialog.getComp(TextureComp.mapper).texture = game.textureAssets.dialog_fail
			game.engine.addEntity(dialog)
			dialogShown = true
			levelFailed = true
		}
	}

	fun completeLevel() {
		if (!dialogShown) {
			pauseGame()
			currentLevel++

			if (currentLevel < levels.size) {
				dialog.getComp(TextureComp.mapper).texture = game.textureAssets.dialog_error
				levelComplete = true
			} else {
				dialog.getComp(TextureComp.mapper).texture = game.textureAssets.dialog_crash
				gameComplete = true
			}

			game.engine.addEntity(dialog)
			dialogShown = true
		}
	}

	fun restartLevel() {
		if (!dialogShown) {
			for (i in 1..currentLevel) levels[i].init(this)
			resumeGame()
		}
	}

	private fun nextLevel() {
		for (i in 1 until currentLevel) levels[i].init(this)
		levels[currentLevel].initOneOff(this)
		levels[currentLevel].init(this)
		resumeGame()
	}

	fun pauseGame() {
		game.engine.removeSystem(box2DSys)
		gamePaused = true
	}

	fun resumeGame() {
		game.engine.addSystem(box2DSys)
		gamePaused = false
	}
}
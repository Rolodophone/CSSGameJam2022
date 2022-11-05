package io.github.rolodophone.cssgamejam2022

import box2dLight.PointLight
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.utils.TimeUtils
import io.github.rolodophone.cssgamejam2022.comp.*
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
	lateinit var processor: Entity
	lateinit var background1: Entity
	lateinit var background2: List<Entity>
	lateinit var background3: List<Entity>
	lateinit var background4: List<Entity>
	lateinit var background5: List<Entity>
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

	val scheduledFunctions = mutableListOf<ScheduledFunction>()
	private val functionsToSchedule = mutableListOf<ScheduledFunction>()

	override fun show() {
		box2DSys = Box2DSys(game.world)

		player = game.engine.entity {
			with<InfoComp> {
				name = "Player"
				tags = mutableSetOf(InfoComp.Tag.PLAYER)
			}
			val boxBodyComp = with<BoxBodyComp> {
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
			with<LightComp> {
				addLightToBody(
					PointLight(game.rayHandler, 160, Color(0.75f, 0.75f, 0.5f, 0.75f), 1f, 0f, 0f),
					boxBodyComp, boxBodyComp.width/2f, boxBodyComp.height/2f
				)
			}
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
					box(width, height, Vector2(width/2f, height/2f))
					userData = this@entity.entity
				}.apply {
					isActive = false
				}
			}
			with<TextureComp> {
				texture = game.textureAssets.dialog_init
			}
		}

		game.renderSys.dialog = dialog

		background1 = game.engine.entity {
			with<InfoComp> {
				name = "Background1"
				tags = mutableSetOf(InfoComp.Tag.BACKGROUND)
			}
			with<BoxBodyComp> {
				width = WORLD_WIDTH
				height = WORLD_HEIGHT
				body = game.world.body {
					position.setZero()
					box(width, height, Vector2(width/2f, height/2f))
					userData = this@entity.entity
				}.apply {
					isActive = false
				}
			}
			with<TextureComp> {
				texture = game.textureAssets.background1
				z = -20
			}
		}

		background2 = entityPresets.scrollingBackground(2, 0.2f)
		background3 = entityPresets.scrollingBackground(3, 0.3f)
		background4 = entityPresets.scrollingBackground(4, 0.4f)
		background5 = entityPresets.scrollingBackground(5, 0.5f)
	}

	override fun render(delta: Float) {
		if (!gamePaused) {
			if (player.getComp(BoxBodyComp.mapper).y < -0.7f) failLevel()

			//handle scheduled functions
			val scheduledFunctionsToRemove = mutableListOf<ScheduledFunction>()
			for (scheduledFunction in scheduledFunctions) {
				if (TimeUtils.millis() > scheduledFunction.millisScheduled) {
					scheduledFunction.invoke()
					scheduledFunctionsToRemove.add(scheduledFunction)
				}
			}
			scheduledFunctions.removeAll { it in scheduledFunctionsToRemove }
			scheduledFunctionsToRemove.clear()
			scheduledFunctions.addAll(functionsToSchedule)
			functionsToSchedule.forEach { it.schedule() }
			functionsToSchedule.clear()

			//update background
			for (scrollingBackground in (background2 + background3 + background4 + background5)) {
				val body = scrollingBackground.getComp(BoxBodyComp.mapper).body
				if (body.position.y < -WORLD_HEIGHT) {
					body.setTransform(body.position.x, body.position.y + WORLD_HEIGHT*3f, 0f)
				}
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

	fun scheduleFunction(millisUntil: Long, function: () -> Unit) {
		functionsToSchedule.add(ScheduledFunction(millisUntil, function))
	}
}
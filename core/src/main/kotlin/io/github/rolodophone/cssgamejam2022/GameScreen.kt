package io.github.rolodophone.cssgamejam2022

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
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

class GameScreen(private val game: CSSGameJam2022) : KtxScreen {
	val entityPresets = EntityPresets(game)

	lateinit var player: Entity
	lateinit var door: Entity
	lateinit var background: Entity
	lateinit var barriers: List<Entity>
	lateinit var movingBarriers: List<Entity>
	lateinit var movingPlatforms: List<Entity>
	lateinit var platforms: List<Entity>
	lateinit var saws: List<Entity>

	lateinit var playerSys: PlayerSys
	lateinit var box2DSys: Box2DSys

	var currentLevel = 1
	var timeDied = 0L
	var waitingForRestart = false

	override fun show() {
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
				texture = game.textureAssets.player
			}
			with<PlayerComp>()
		}

		door = game.engine.entity {
			with<InfoComp> {
				name = "Door"
				tags = mutableSetOf(InfoComp.Tag.DOOR)
			}
			with<BoxBodyComp> {
				width = 0.45f
				height = 0.75f
				body = game.world.body {
					position.set(14.5f, 7f)
					box(width, height, Vector2(width/2f, height/2f)) {
						isSensor = true
					}
					userData = this@entity.entity
				}
			}
			with<TextureComp> {
				texture = game.textureAssets.door
				z = -10
			}
		}

		background = game.engine.entity {
			with<InfoComp> {
				name = "Background"
				tags = mutableSetOf(InfoComp.Tag.BACKGROUND)
			}
			with<BoxBodyComp> {
				width = WORLD_WIDTH
				height = WORLD_HEIGHT
				body = game.world.body {
					position.setZero()
					box(width, height, Vector2(width/2f, height/2f)) {
						isSensor = true //disable collision
					}
					userData = this@entity.entity
				}
			}
			with<TextureComp> {
				texture = game.textureAssets.background
				z = -20
			}
		}

		barriers = listOf(
			entityPresets.barrier(0f, 0.35f),
			entityPresets.barrier(0f, 3.75f),
			entityPresets.barrier(0f, 7.15f),
			entityPresets.barrier(1.25f, 4.15f),
			entityPresets.barrier(14.05f, 3.35f),
			entityPresets.barrier(15.8f, 7.15f),
		)
		movingBarriers = listOf(
			entityPresets.movingBarrier(12.6f, 12.6f, 5.5f, 7f),
			entityPresets.movingBarrier(7.7f, 14.35f,  0.35f, 0.35f),
		)
		movingPlatforms = listOf(
			entityPresets.movingPlatform(4f, 6f, 2f, 2f),
			entityPresets.movingPlatform(3.8f, 3.8f, 5.5f, 7.3f),
		)
		platforms = listOf(
			entityPresets.platform(0f, 0f),
			entityPresets.platform(2f, 0f),
			entityPresets.platform(4f, 0f),
			entityPresets.platform(8f, 0f),
			entityPresets.platform(10f, 0f),
			entityPresets.platform(12f, 0f),
			entityPresets.platform(1.5f, 3.8f),
			entityPresets.platform(0.25f, 8.7f),
			entityPresets.platform(6.8f, 4.5f),
			entityPresets.platform(12f, 3f),
			entityPresets.platform(13.75f, 8.7f),
			entityPresets.platform(13.6f, 6.7f),
			entityPresets.platform(9.5f, 6f)
		)
		saws = listOf(
			entityPresets.saw(-0.3f, 2.5f),
			entityPresets.saw(-0.3f, 5.9f),
			entityPresets.saw(5.2f, 8.7f),
			entityPresets.saw(15.7f, 4.9f),
		)

		playerSys = PlayerSys(this, player)
		box2DSys = Box2DSys(game.world)
		game.engine.addSystem(playerSys)
		game.engine.addSystem(box2DSys)

		Gdx.input.inputProcessor = GameInputProcessor(playerSys)

		game.world.setContactListener(GameContactListener(this))

		restartLevel()
	}

	override fun render(delta: Float) {
		if (waitingForRestart && TimeUtils.timeSinceMillis(timeDied) >= 500L) {
			waitingForRestart = false
			restartLevel()
		}

		if (player.getComp(BoxBodyComp.mapper).y < -0.7f) die()
	}

	fun die() {
		if (!waitingForRestart) {
			timeDied = TimeUtils.millis()
			waitingForRestart = true
			pauseGame()
		}
	}

	fun restartLevel() {
		initLevel(currentLevel)
	}

	fun nextLevel() {
		currentLevel++
		initLevel(currentLevel)
	}

	private fun initLevel(level: Int) {
		when (level) {
			1 -> {
				player.getComp(PlayerComp.mapper).reset()
				player.getComp(BoxBodyComp.mapper).body.apply {
					setTransform(1.5f, 0.5f, 0f)
					setLinearVelocity(0f, 0f)
					isAwake = true
				}
				movingBarriers[0].getComp(BoxBodyComp.mapper).body.apply {
					setTransform(12.6f, 5.5f, 0f)
					setLinearVelocity(0f, 1f)
				}
				movingBarriers[1].getComp(BoxBodyComp.mapper).body.apply {
					setTransform(7.7f, 0.35f, 0f)
					setLinearVelocity(1.8f, 0f)
				}
				movingPlatforms[0].getComp(BoxBodyComp.mapper).body.apply {
					setTransform(4f,2f, 0f)
					setLinearVelocity(1f, 0f)
				}
				movingPlatforms[1].getComp(BoxBodyComp.mapper).body.apply {
					setTransform(3.8f, 5.5f, 0f)
					setLinearVelocity(0f, 1f)
				}
			}
		}

		resumeGame()
	}

	fun pauseGame() {
		game.engine.removeSystem(box2DSys)
	}

	fun resumeGame() {
		game.engine.addSystem(box2DSys)
	}
}
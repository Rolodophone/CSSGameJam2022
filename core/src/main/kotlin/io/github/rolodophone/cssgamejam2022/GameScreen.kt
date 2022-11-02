package io.github.rolodophone.cssgamejam2022

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import io.github.rolodophone.cssgamejam2022.comp.BoxBodyComp
import io.github.rolodophone.cssgamejam2022.comp.TextureComp
import io.github.rolodophone.cssgamejam2022.sys.PlayerSys
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.getSystem
import ktx.ashley.with
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.fixture
import javax.swing.Box

class GameScreen(private val game: CSSGameJam2022) : KtxScreen {
	lateinit var player: Entity
	val entityPresets = EntityPresets(game)

	override fun show() {
		player = game.engine.entity {
			with<BoxBodyComp> {
				width = 0.4f
				height = 0.7f
				body = game.world.body {
					type = BodyDef.BodyType.DynamicBody
					fixedRotation = true
					position.set(1.5f, 0.5f)

					box(width, height, Vector2(width/2f, height/2f))
					box(width, 0.2f, Vector2(width/2f, 0f)) { // foot sensor
						isSensor = true
						userData = 0
					}
				}
			}
			with<TextureComp> {
				texture = game.textureAssets.player
			}
		}

		val door = game.engine.entity {
			with<BoxBodyComp> {
				width = 0.45f
				height = 0.75f
				body = game.world.body {
					position.set(14f, 7f)
					box(width, height, Vector2(width/2f, height/2f)) {
						isSensor = true
						userData = 1
					}
				}
			}
			with<TextureComp> {
				texture = game.textureAssets.door
				z = -10
			}
		}

		val background = game.engine.entity {
			with<BoxBodyComp> {
				width = WORLD_WIDTH
				height = WORLD_HEIGHT
				body = game.world.body {
					position.setZero()
					box(width, height, Vector2(width/2f, height/2f)) {
						isSensor = true //disable collision
					}
				}
			}
			with<TextureComp> {
				texture = game.textureAssets.background
				z = -20
			}
		}

		val barriers = listOf(
			entityPresets.barrier(0f, 0.35f),
			entityPresets.barrier(0f, 3.75f),
			entityPresets.barrier(0f, 7.15f),
			entityPresets.barrier(1.25f, 4.15f),
			entityPresets.barrier(14.05f, 3.35f),
			entityPresets.barrier(10.4f, 1.4f),
			entityPresets.barrier(10.6f, 6.5f),
			entityPresets.barrier(15.8f, 7.15f),
		)

		val platforms = listOf(
			entityPresets.platform(0f, 0f),
			entityPresets.platform(2f, 0f),
			entityPresets.platform(4f, 0f),
			entityPresets.platform(8f, 0f),
			entityPresets.platform(10f, 0f),
			entityPresets.platform(12f, 0f),
			entityPresets.platform(1.5f, 3.8f),
			entityPresets.platform(0.25f, 8.7f),
			entityPresets.platform(5.8f, 2f),
			entityPresets.platform(6.8f, 4.5f),
			entityPresets.platform(3.8f, 6.9f),
			entityPresets.platform(12f, 3f),
			entityPresets.platform(13.75f, 8.7f),
		)

		val saws = listOf(
			entityPresets.saw(-0.3f, 2.5f),
			entityPresets.saw(-0.3f, 5.9f),
			entityPresets.saw(5.2f, 8.7f),
			entityPresets.saw(15.7f, 4.9f),
		)

		val playerSys = PlayerSys(player)
		game.engine.addSystem(playerSys)
		Gdx.input.inputProcessor = GameInputProcessor(playerSys)
		game.world.setContactListener(GameContactListener(playerSys))
	}
}
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

class GameScreen(private val game: CSSGameJam2022) : KtxScreen {
	lateinit var player: Entity

	override fun show() {
		val ground = game.engine.entity {
			with<BoxBodyComp> {
				width = WORLD_WIDTH
				height = 0.5f
				body = game.world.body {
					box(width, height, Vector2(width/2f, height/2f)) {
						friction = 0.7f
					}
					position.setZero()
				}
			}
		}

		val platforms = listOf(
			game.engine.entity {
				with<BoxBodyComp> {
					width = 2.5f
					height = 0.5f
					body = game.world.body {
						box(width, height, Vector2(width/2f, height/2f))
						position.set(2.5f, 3f)
					}
				}
			},
			game.engine.entity {
				with<BoxBodyComp> {
					width = 2.5f
					height = 0.5f
					body = game.world.body {
						box(width, height, Vector2(width/2f, height/2f))
						position.set(7.5f, 1.5f)
					}
				}
			},
			game.engine.entity {
				with<BoxBodyComp> {
					width = 2.5f
					height = 0.5f
					body = game.world.body {
						box(width, height, Vector2(width/2f, height/2f))
						position.set(9f, 4f)
					}
				}
			}
		)

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

		val playerSys = PlayerSys(player)
		game.engine.addSystem(playerSys)
		Gdx.input.inputProcessor = GameInputProcessor(playerSys)
		game.world.setContactListener(GameContactListener(playerSys))
	}
}
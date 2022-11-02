package io.github.rolodophone.cssgamejam2022

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import io.github.rolodophone.cssgamejam2022.comp.BoxBodyComp
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
				body = game.world.body {
					box(WORLD_WIDTH, 0.5f, Vector2(WORLD_WIDTH_HALF, 0.25f)) {
						friction = 0.7f
					}
				}
			}
		}

		val platforms = listOf(
			game.engine.entity {
				with<BoxBodyComp> {
					body = game.world.body {
						box(2.5f, 0.5f, Vector2(3.25f, 3.25f))
					}
				}
			},
			game.engine.entity {
				with<BoxBodyComp> {
					body = game.world.body {
						box(2.5f, 0.5f, Vector2(8.75f, 2f))
					}
				}
			},
			game.engine.entity {
				with<BoxBodyComp> {
					body = game.world.body {
						box(2.5f, 0.5f, Vector2(11f, 4.5f))
					}
				}
			}
		)

		player = game.engine.entity {
			with<BoxBodyComp> {
				body = game.world.body {
					box(0.4f, 0.7f, Vector2(2f, 0.8f))
					type = BodyDef.BodyType.DynamicBody
				}
			}
		}

		val playerSys = PlayerSys(player)

		game.engine.addSystem(playerSys)

		Gdx.input.inputProcessor = GameInputProcessor(playerSys)
	}
}
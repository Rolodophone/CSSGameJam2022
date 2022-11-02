package io.github.rolodophone.cssgamejam2022

import com.badlogic.gdx.math.Vector2
import io.github.rolodophone.cssgamejam2022.comp.BoxBodyComp
import io.github.rolodophone.cssgamejam2022.comp.TextureComp
import ktx.ashley.entity
import ktx.ashley.with
import ktx.box2d.body
import ktx.box2d.box

class EntityPresets(private val game: CSSGameJam2022) {
	fun barrier(x: Float, y: Float) = game.engine.entity {
		with<BoxBodyComp> {
			width = 0.2f
			height = 1.5f
			body = game.world.body {
				box(width, height, Vector2(width/2f, height/2f))
				position.set(x, y)
			}
		}
		with<TextureComp> {
			texture = game.textureAssets.barrier
		}
	}

	fun platform(x: Float, y: Float) = game.engine.entity {
		with<BoxBodyComp> {
			width = 2f
			height = 0.3f
			body = game.world.body {
				box(width, height, Vector2(width / 2f, height / 2f))
				position.set(x, y)
			}
		}
		with<TextureComp> {
			texture = game.textureAssets.platform
		}
	}

	fun saw(x: Float, y: Float) = game.engine.entity {
		with<BoxBodyComp> {
			width = 0.6f
			height = 0.6f
			body = game.world.body {
				box(width, height, Vector2(width/2f, height/2f)) {
					isSensor = true
					userData = 2
				}
				position.set(x, y)
			}
		}
		with<TextureComp> {
			texture = game.textureAssets.saw
		}
	}
}
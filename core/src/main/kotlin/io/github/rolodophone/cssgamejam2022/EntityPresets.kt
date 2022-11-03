package io.github.rolodophone.cssgamejam2022

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import io.github.rolodophone.cssgamejam2022.comp.BoxBodyComp
import io.github.rolodophone.cssgamejam2022.comp.InfoComp
import io.github.rolodophone.cssgamejam2022.comp.KinematicComp
import io.github.rolodophone.cssgamejam2022.comp.TextureComp
import ktx.ashley.entity
import ktx.ashley.with
import ktx.box2d.body
import ktx.box2d.box

class EntityPresets(private val game: CSSGameJam2022) {
	fun barrier(x: Float, y: Float) = game.engine.entity {
		with<InfoComp> {
			name = "Barrier"
			tags = mutableSetOf(InfoComp.Tag.GROUND)
		}
		with<BoxBodyComp> {
			width = 0.2f
			height = 1.5f
			body = game.world.body {
				box(width, height, Vector2(width/2f, height/2f)) {
					density = 1f
				}
				position.set(x, y)
				userData = this@entity.entity
			}
		}
		with<TextureComp> {
			texture = game.textureAssets.barrier
		}
	}

	fun movingBarrier(minX: Float, maxX: Float, minY: Float, maxY: Float) = game.engine.entity {
		with<InfoComp> {
			name = "MovingBarrier"
			tags = mutableSetOf(InfoComp.Tag.GROUND)
		}
		with<BoxBodyComp> {
			width = 0.2f
			height = 1.5f
			body = game.world.body {
				type = BodyDef.BodyType.KinematicBody
				box(width, height, Vector2(width/2f, height/2f)) {
					density = 1f
				}
				userData = this@entity.entity
			}
		}
		with<TextureComp> {
			texture = game.textureAssets.barrier
		}
		with<KinematicComp> {
			this.minX = minX
			this.minY = minY
			this.maxX = maxX
			this.maxY = maxY
		}
	}

	fun movingPlatform(minX: Float, maxX: Float, minY: Float, maxY: Float) = game.engine.entity {
		with<InfoComp> {
			name = "Platform"
			tags = mutableSetOf(InfoComp.Tag.GROUND)
		}
		with<BoxBodyComp> {
			width = 2f
			height = 0.3f
			body = game.world.body {
				type = BodyDef.BodyType.KinematicBody
				box(width, height, Vector2(width / 2f, height / 2f)) {
					density = 1f
				}
				userData = this@entity.entity
			}
		}
		with<TextureComp> {
			texture = game.textureAssets.platform
		}
		with<KinematicComp> {
			this.minX = minX
			this.minY = minY
			this.maxX = maxX
			this.maxY = maxY
		}
	}

	fun platform(x: Float, y: Float) = game.engine.entity {
		with<InfoComp> {
			name = "Platform"
			tags = mutableSetOf(InfoComp.Tag.GROUND)
		}
		with<BoxBodyComp> {
			width = 2f
			height = 0.3f
			body = game.world.body {
				box(width, height, Vector2(width / 2f, height / 2f)) {
					density = 1f
				}
				position.set(x, y)
				userData = this@entity.entity
			}
		}
		with<TextureComp> {
			texture = game.textureAssets.platform
		}
	}

	fun saw(x: Float, y: Float) = game.engine.entity {
		with<InfoComp> {
			name = "Saw"
			tags = mutableSetOf(InfoComp.Tag.SAW)
		}
		with<BoxBodyComp> {
			width = 0.6f
			height = 0.6f
			body = game.world.body {
				box(width, height, Vector2(width/2f, height/2f)) {
					isSensor = true
					density = 1f
				}
				position.set(x, y)
				userData = this@entity.entity
			}
		}
		with<TextureComp> {
			texture = game.textureAssets.saw
		}
	}
}
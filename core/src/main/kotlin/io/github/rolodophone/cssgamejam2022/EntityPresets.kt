package io.github.rolodophone.cssgamejam2022

import box2dLight.ChainLight
import box2dLight.PointLight
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import io.github.rolodophone.cssgamejam2022.comp.*
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
		val boxBodyComp = with<BoxBodyComp> {
			width = 0.2f
			height = 1.5f
			body = game.world.body {
				box(width, height, Vector2(width/2f, height/2f)) {
					density = 1f
					filter.categoryBits = 0b11
				}
				position.set(x, y)
				userData = this@entity.entity
			}
		}
		with<TextureComp> {
			texture = game.textureAssets.barrier
		}
		with<LightComp> {
			//add lights all round to simulate glow (*41/40 because the last ray is not on the edge)
			addLightToBody(ChainLight(game.rayHandler, 40, Color(0.75f, 0.75f, 0.5f, 0.75f), 0.7f, 1,
				floatArrayOf(boxBodyComp.width/2f, 0f, boxBodyComp.width/2f, boxBodyComp.height*41/40)), boxBodyComp)
			addLightToBody(ChainLight(game.rayHandler, 40, Color(0.75f, 0.75f, 0.5f, 0.75f), 0.7f, -1,
				floatArrayOf(boxBodyComp.width/2f, 0f, boxBodyComp.width/2f, boxBodyComp.height*41/40)), boxBodyComp)
		}
	}

	fun movingBarrier(minX: Float, maxX: Float, minY: Float, maxY: Float) = game.engine.entity {
		with<InfoComp> {
			name = "MovingBarrier"
			tags = mutableSetOf(InfoComp.Tag.GROUND)
		}
		val boxBodyComp = with<BoxBodyComp> {
			width = 0.2f
			height = 1.5f
			body = game.world.body {
				type = BodyDef.BodyType.KinematicBody
				box(width, height, Vector2(width/2f, height/2f)) {
					density = 1f
					filter.categoryBits = 0b11
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
		with<LightComp> {
			//add lights all round to simulate glow (*41/40 because the last ray is not on the edge)
			addLightToBody(ChainLight(game.rayHandler, 40, Color(0.75f, 0.75f, 0.5f, 0.75f), 0.7f, 1,
				floatArrayOf(boxBodyComp.width/2f, 0f, boxBodyComp.width/2f, boxBodyComp.height*41/40)), boxBodyComp)
			addLightToBody(ChainLight(game.rayHandler, 40, Color(0.75f, 0.75f, 0.5f, 0.75f), 0.7f, -1,
				floatArrayOf(boxBodyComp.width/2f, 0f, boxBodyComp.width/2f, boxBodyComp.height*41/40)), boxBodyComp)
		}
	}

	fun movingPlatform(minX: Float, maxX: Float, minY: Float, maxY: Float) = game.engine.entity {
		val platformWidth = 2f
		val platformHeight = 0.3f

		with<InfoComp> {
			name = "Platform"
			tags = mutableSetOf(InfoComp.Tag.GROUND)
		}
		val boxBodyComp = with<BoxBodyComp> {
			width = 2f
			height = 0.3f
			body = game.world.body {
				type = BodyDef.BodyType.KinematicBody
				box(width, height, Vector2(width / 2f, height / 2f)) {
					density = 1f
					filter.categoryBits = 0b11
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
		with<LightComp> {
			//add lights all round to simulate glow (*41/40 because the last ray is not on the edge)
			addLightToBody(ChainLight(game.rayHandler, 40, Color(0.75f, 0.75f, 0.5f, 0.75f), 0.7f, 1,
				floatArrayOf(0f, platformHeight/2f, platformWidth*41/40, platformHeight/2f)), boxBodyComp)
			addLightToBody(ChainLight(game.rayHandler, 40, Color(0.75f, 0.75f, 0.5f, 0.75f), 0.7f, -1,
				floatArrayOf(0f, platformHeight/2f, platformWidth*41/40, platformHeight/2f)), boxBodyComp)
		}
	}

	fun platform(x: Float, y: Float) = game.engine.entity {
		val platformWidth = 2f
		val platformHeight = 0.3f

		with<InfoComp> {
			name = "Platform"
			tags = mutableSetOf(InfoComp.Tag.GROUND)
		}
		val boxBodyComp = with<BoxBodyComp> {
			width = platformWidth
			height = platformHeight
			body = game.world.body {
				box(width, height, Vector2(width / 2f, height / 2f)) {
					density = 1f
					filter.categoryBits = 0b11
				}
				position.set(x, y)
				userData = this@entity.entity
			}
		}
		with<TextureComp> {
			texture = game.textureAssets.platform
		}
		with<LightComp> {
			//add lights all round to simulate glow (*41/40 because the last ray is not on the edge)
			addLightToBody(ChainLight(game.rayHandler, 40, Color(0.75f, 0.75f, 0.5f, 0.75f), 0.7f, 1,
				floatArrayOf(0f, platformHeight/2f, platformWidth*41/40, platformHeight/2f)), boxBodyComp)
			addLightToBody(ChainLight(game.rayHandler, 40, Color(0.75f, 0.75f, 0.5f, 0.75f), 0.7f, -1,
				floatArrayOf(0f, platformHeight/2f, platformWidth*41/40, platformHeight/2f)), boxBodyComp)
		}
	}

	fun saw(x: Float, y: Float) = game.engine.entity {
		with<InfoComp> {
			name = "Saw"
			tags = mutableSetOf(InfoComp.Tag.SAW)
		}
		val boxBodyComp = with<BoxBodyComp> {
			width = 0.6f
			height = 0.6f
			body = game.world.body {
				box(width, height, Vector2(width/2f, height/2f)) {
					isSensor = true
					density = 1f
					filter.categoryBits = 0b11
				}
				position.set(x, y)
				userData = this@entity.entity
			}
		}
		with<TextureComp> {
			texture = game.textureAssets.saw
		}
		with<LightComp> {
			addLightToBody(PointLight(game.rayHandler, 80, Color(0.75f, 0.75f, 0.5f, 0.75f), 1f, 0f, 0f),
					boxBodyComp, boxBodyComp.width/2f, boxBodyComp.height/2f)
		}
	}

	fun led(x: Float, y: Float) = game.engine.entity {
		with<InfoComp> {
			name = "LED"
			tags = mutableSetOf()
		}
		val boxBodyComp = with<BoxBodyComp> {
			width = 0.1f
			height = 0.1f
			game.world.body {
				box(width, height, Vector2(width/2f, height/2f)) {
					isSensor = true
					density = 1f
				}
				position.set(x, y)
				userData = this@entity.entity
			}
		}
		with<TextureComp> {
			texture = game.textureAssets.led
		}
		with<LightComp> {
			addLightToBody(PointLight(game.rayHandler, 200, Color(0.75f, 0.75f, 0.5f, 0.75f), 7f, 0f, 0f),
					boxBodyComp, 0.5f, 0.5f)
		}
	}

	fun scrollingBackground(n: Int, scrollSpeed: Float) = List(3) { i ->
		game.engine.entity {
			with<InfoComp> {
				name = "Background$n"
				tags = mutableSetOf(InfoComp.Tag.BACKGROUND)
			}
			with<BoxBodyComp> {
				width = WORLD_WIDTH
				height = WORLD_HEIGHT
				body = game.world.body {
					type = BodyDef.BodyType.KinematicBody
					position.set(0f, i * WORLD_HEIGHT)
					linearVelocity.y = -scrollSpeed
					box(width, height, Vector2(width/2f, height/2f)) {
						filter.maskBits = 0
					}
					userData = this@entity.entity
				}
			}
			with<TextureComp> {
				texture = listOf(game.textureAssets.background2, game.textureAssets.background3,
					game.textureAssets.background4, game.textureAssets.background5)[n-2]
				z = -20
			}
		}
	}
}
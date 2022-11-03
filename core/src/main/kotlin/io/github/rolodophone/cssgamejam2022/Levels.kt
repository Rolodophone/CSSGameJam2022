package io.github.rolodophone.cssgamejam2022

import box2dLight.PointLight
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import io.github.rolodophone.cssgamejam2022.comp.*
import io.github.rolodophone.cssgamejam2022.sys.PlayerSys
import ktx.ashley.entity
import ktx.ashley.with
import ktx.box2d.body
import ktx.box2d.box
import kotlin.random.Random

class Level(val initOneOff: GameScreen.() -> Unit = {}, val init: GameScreen.() -> Unit = {})

val levels = listOf(
	Level(), //dummy level to make indexing start at 1
	Level(
		initOneOff = {
			door = game.engine.entity {
				with<InfoComp> {
					name = "Door"
					tags = mutableSetOf(InfoComp.Tag.DOOR)
				}
				with<BoxBodyComp> {
					width = 1.05f
					height = 1.05f
					body = game.world.body {
						position.set(14.15f, 7.3f)
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
				entityPresets.movingBarrier(7.7f, 14.3f,  0.35f, 0.35f),
			)
			movingPlatforms = listOf(
				entityPresets.movingPlatform(4f, 6f, 2f, 2f),
				entityPresets.movingPlatform(3.8f, 3.8f, 5.5f, 7.3f),
			)
			platforms = listOf(
				entityPresets.platform(0.5f, 0f),
				entityPresets.platform(2.75f, 0.2f),
				entityPresets.platform(5f, 0.4f),
				entityPresets.platform(8f, 0f),
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
				entityPresets.saw(0f, 2.5f),
				entityPresets.saw(0f, 5.9f),
				entityPresets.saw(5.2f, 8.7f),
				entityPresets.saw(13.6f, 4.9f),
			)
			leds = listOf()

			PointLight(game.rayHandler, 200, Color(0.75f, 0.75f, 0.5f, 0.75f), 7f, 0f, 0f).apply {
				attachToBody(player.getComp(BoxBodyComp.mapper).body)
				setContactFilter(1, 1, 1)
			}
			PointLight(game.rayHandler, 200, Color(0.75f, 0.75f, 0.5f, 0.75f), 7f, 0f, 0f).apply {
				attachToBody(door.getComp(BoxBodyComp.mapper).body)
				setContactFilter(1, 1, 1)
			}

			playerSys = PlayerSys(this, player)
			game.engine.addSystem(playerSys)

			Gdx.input.inputProcessor = GameInputProcessor(playerSys, this)

			game.world.setContactListener(GameContactListener(this))
		},
		init = {
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
		},
	),
	Level(
		initOneOff = {
			movingBarriers[0].apply {
				getComp(KinematicComp.mapper).minY = 6.5f
				getComp(KinematicComp.mapper).maxY = 6.6f
			}
			movingBarriers[1].apply {
				getComp(KinematicComp.mapper).maxX = 7.8f
			}
			movingPlatforms[0].apply {
				getComp(KinematicComp.mapper).minX = 5.9f
			}
			movingPlatforms[1].apply {
				getComp(KinematicComp.mapper).minY = 7.5f
				getComp(KinematicComp.mapper).maxY = 7.6f
			}
		},
		init = {
			movingBarriers[0].apply {
				getComp(BoxBodyComp.mapper).body.apply {
					setTransform(12.6f, 6.55f, 0f)
					setLinearVelocity(0f, 0.5f)
				}
			}
			movingBarriers[1].apply {
				getComp(BoxBodyComp.mapper).body.apply {
					setTransform(7.75f, 0.35f, 0f)
					setLinearVelocity(0.5f, 0f)
				}
			}
			movingPlatforms[0].apply {
				getComp(BoxBodyComp.mapper).body.apply {
					setTransform(5.95f,2f, 0f)
					setLinearVelocity(0.5f, 0f)
				}
			}
			movingPlatforms[1].apply {
				getComp(BoxBodyComp.mapper).body.apply {
					setTransform(3.8f, 7.55f, 0f)
					setLinearVelocity(0f, 0.5f)
				}
			}
		}
	),
	Level(
		initOneOff = {
			for (saw in saws) {
				val sawBody = saw.getComp(BoxBodyComp.mapper).body
				sawBody.type = BodyDef.BodyType.DynamicBody
				sawBody.fixtureList.single().isSensor = false
			}
		},
		init = {
			saws[0].getComp(BoxBodyComp.mapper).body.setTransform(0f, 2.5f, 0f)
			saws[1].getComp(BoxBodyComp.mapper).body.setTransform(0f, 5.9f, 0f)
			saws[2].getComp(BoxBodyComp.mapper).body.setTransform(5.2f, 8.7f, 0f)
			saws[3].getComp(BoxBodyComp.mapper).body.setTransform(13.6f, 4.9f, 0f)
			saws.forEach {
				it.getComp(BoxBodyComp.mapper).body.isAwake = true
			}
		}
	),
	Level(
		init = {
			val random = Random(328)
			(platforms + barriers + movingPlatforms + movingBarriers).forEach {
				val body = it.getComp(BoxBodyComp.mapper).body
				body.setTransform(body.position, random.nextFloat() * MathUtils.PI/4f - MathUtils.PI/8f)
			}
		}
	),
//	Level(
//		initOneOff = {
//			val hingePoints = listOf(
//				Vector2(2.9f, 0.35f)
//			)
//
//			for (indexedEntity in listOf(platforms[2]).withIndex()) {
//				val body = indexedEntity.value.getComp(BoxBodyComp.mapper).body
//				body.type = BodyDef.BodyType.DynamicBody
//				body.isAwake = true
//				body.revoluteJointWith(background.getComp(BoxBodyComp.mapper).body) {
//					initialize(bodyA, bodyB, bodyB.position)
//					collideConnected = false
//				}
//			}
//		}
//	)
)
package io.github.rolodophone.cssgamejam2022.comp

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.box2d.body
import ktx.box2d.box

class BoxBodyComp: Component, Pool.Poolable {
	companion object {
		val mapper = mapperFor<BoxBodyComp>()
	}

	lateinit var body: Body

	override fun reset() {}

	fun init(world: World, x: Float, y: Float, width: Float, height: Float) {
		body = world.body {
			box(width, height, position = Vector2(width, height))
		}
	}
}
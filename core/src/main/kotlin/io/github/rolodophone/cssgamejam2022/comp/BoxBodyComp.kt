package io.github.rolodophone.cssgamejam2022.comp

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class BoxBodyComp: Component, Pool.Poolable {
	companion object {
		val mapper = mapperFor<BoxBodyComp>()
	}

	lateinit var body: Body

	val x: Float
		get() = body.position.x
	val y: Float
		get() = body.position.y

	//only set once for each entity
	var width = 1f
	var height = 1f

	override fun reset() {}
}
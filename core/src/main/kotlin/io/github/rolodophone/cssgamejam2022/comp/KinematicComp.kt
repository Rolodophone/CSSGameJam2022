package io.github.rolodophone.cssgamejam2022.comp

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class KinematicComp: Component, Pool.Poolable {
	companion object {
		val mapper = mapperFor<KinematicComp>()
	}

	var minX = 0f
	var minY = 0f
	var maxX = 0f
	var maxY = 0f

	override fun reset() {}
}
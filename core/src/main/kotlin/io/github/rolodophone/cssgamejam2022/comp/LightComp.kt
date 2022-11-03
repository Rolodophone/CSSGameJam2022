package io.github.rolodophone.cssgamejam2022.comp

import box2dLight.PointLight
import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class LightComp : Component, Pool.Poolable {
	companion object {
		val mapper = mapperFor<LightComp>()
	}

	lateinit var light: PointLight

	override fun reset() {}
}

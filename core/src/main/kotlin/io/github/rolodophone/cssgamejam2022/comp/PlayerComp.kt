package io.github.rolodophone.cssgamejam2022.comp

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class PlayerComp: Component, Pool.Poolable {
	companion object {
		val mapper = mapperFor<PlayerComp>()
	}

	override fun reset() {}
}
package io.github.rolodophone.cssgamejam2022.comp

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class PlayerComp: Component, Pool.Poolable {
	companion object {
		val mapper = mapperFor<PlayerComp>()
	}

	var movingLeft = false
	var movingRight = false
	var onGround = true
	var numGroundsTouching = 0
		set(value) {
			print(value)
			field = value
		}

	override fun reset() {
		movingLeft = false
		movingRight = false
		onGround = true
		numGroundsTouching = 0
	}
}
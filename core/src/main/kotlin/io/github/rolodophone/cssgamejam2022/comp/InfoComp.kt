package io.github.rolodophone.cssgamejam2022.comp

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class InfoComp: Component, Pool.Poolable {
	companion object {
		val mapper = mapperFor<InfoComp>()
	}

	enum class Tag {
		SAW, DOOR, GROUND, PLAYER, BACKGROUND
	}

	var name = "Unnamed Entity"
	var tags = mutableSetOf<Tag>()

	override fun reset() {
		name = "Unnamed Entity"
		tags.clear()
	}
}
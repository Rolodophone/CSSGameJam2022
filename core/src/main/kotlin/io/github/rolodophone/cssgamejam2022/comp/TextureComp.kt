package io.github.rolodophone.cssgamejam2022.comp

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class TextureComp: Component, Pool.Poolable {
	companion object {
		val mapper = mapperFor<TextureComp>()
	}

	lateinit var texture: TextureRegion
	var z = 0

	override fun reset() {}
}
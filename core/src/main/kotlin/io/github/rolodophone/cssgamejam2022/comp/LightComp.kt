package io.github.rolodophone.cssgamejam2022.comp

import box2dLight.ChainLight
import box2dLight.Light
import box2dLight.PositionalLight
import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class LightComp : Component, Pool.Poolable {
	companion object {
		val mapper = mapperFor<LightComp>()
	}

	private val lights = mutableListOf<Light>()

	override fun reset() {
		lights.clear()
	}

	fun addLight(light: Light) {
		light.isXray = true
		lights.add(light)
	}

	fun addLightToBody(light: PositionalLight, boxBodyComp: BoxBodyComp,
			offsetX: Float = 0f, offsetY: Float = 0f, offsetAngle: Float = 0f) {
		addLight(light)
		light.attachToBody(boxBodyComp.body, offsetX, offsetY, offsetAngle)
	}

	fun addLightToBody(light: ChainLight, boxBodyComp: BoxBodyComp, offsetAngle: Float = 0f) {
		addLight(light)
		light.attachToBody(boxBodyComp.body, offsetAngle)
	}

	fun disableLights() {
		lights.forEach { it.isActive = false }
	}

	fun enableLights() {
		lights.forEach { it.isActive = true }
	}
}

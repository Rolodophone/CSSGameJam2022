package io.github.rolodophone.cssgamejam2022.sys

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import io.github.rolodophone.cssgamejam2022.CSSGameJam2022

class DebugSys(private val game: CSSGameJam2022): EntitySystem(5) {
	private val box2DDebugRenderer = Box2DDebugRenderer()

	override fun update(deltaTime: Float) {
		box2DDebugRenderer.render(game.world, game.camera.combined)
	}
}
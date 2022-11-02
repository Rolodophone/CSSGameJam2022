package io.github.rolodophone.cssgamejam2022.sys

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.physics.box2d.World
import io.github.rolodophone.cssgamejam2022.comp.BoxBodyComp
import io.github.rolodophone.cssgamejam2022.comp.KinematicComp
import io.github.rolodophone.cssgamejam2022.getComp
import ktx.ashley.allOf

class Box2DSys(
	private val world: World
): IteratingSystem(allOf(BoxBodyComp::class, KinematicComp::class).get(), 10) {
	override fun update(deltaTime: Float) {
		world.step(deltaTime, 6, 2)

		// process kinematic entities
		if (entities != null) super.update(deltaTime)
	}

	override fun processEntity(entity: Entity, deltaTime: Float) {
		val boxBodyComp = entity.getComp(BoxBodyComp.mapper)
		val kinematicComp = entity.getComp(KinematicComp.mapper)

		if (boxBodyComp.x < kinematicComp.minX || boxBodyComp.x > kinematicComp.maxX) {
			boxBodyComp.body.setLinearVelocity(-boxBodyComp.body.linearVelocity.x, boxBodyComp.body.linearVelocity.y)
		}
		if (boxBodyComp.y < kinematicComp.minY || boxBodyComp.y > kinematicComp.maxY) {
			boxBodyComp.body.setLinearVelocity(boxBodyComp.body.linearVelocity.x, -boxBodyComp.body.linearVelocity.y)
		}
	}
}
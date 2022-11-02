package io.github.rolodophone.cssgamejam2022.sys

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.physics.box2d.World
import io.github.rolodophone.cssgamejam2022.comp.BoxBodyComp
import io.github.rolodophone.cssgamejam2022.comp.KinematicComp
import io.github.rolodophone.cssgamejam2022.getComp

class Box2DSys(
	private val world: World,
	private val movingBarriers: List<Entity>
): EntitySystem(10) {
	override fun update(deltaTime: Float) {
		world.step(deltaTime, 6, 2)

		// handle kinematic barriers
		for (movingBarrier in movingBarriers) {
			val boxBodyComp = movingBarrier.getComp(BoxBodyComp.mapper)
			val kinematicComp = movingBarrier.getComp(KinematicComp.mapper)

			if (boxBodyComp.x < kinematicComp.minX || boxBodyComp.x > kinematicComp.maxX ||
				boxBodyComp.y < kinematicComp.minY || boxBodyComp.y > kinematicComp.maxY) {

				boxBodyComp.body.setLinearVelocity(-boxBodyComp.body.linearVelocity.x, -boxBodyComp.body.linearVelocity.y)
			}
		}
	}
}
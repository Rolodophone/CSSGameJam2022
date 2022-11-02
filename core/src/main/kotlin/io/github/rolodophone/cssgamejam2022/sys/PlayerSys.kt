package io.github.rolodophone.cssgamejam2022.sys

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import io.github.rolodophone.cssgamejam2022.comp.BoxBodyComp
import io.github.rolodophone.cssgamejam2022.getComp

class PlayerSys(player: Entity): EntitySystem() {
	private val playerBody = player.getComp(BoxBodyComp.mapper).body

	var movingLeft = false
	var movingRight = false
	var onGround = true

	override fun update(deltaTime: Float) {
		if (movingLeft && onGround) {
			playerBody.applyLinearImpulse(-0.2f, 0f, 0f, 0f, true)
		}
		if (movingRight && onGround) {
			playerBody.applyLinearImpulse(0.2f, 0f, 0f, 0f, true)
		}
	}

	fun moveLeft() {
		movingRight = false
		movingLeft = true
	}

	fun moveRight() {
		movingLeft = false
		movingRight = true
	}

	fun stop() {
		movingLeft = false
		movingRight = false
	}

	fun jump() {
		if (playerBody.linearVelocity.y == 0f) {
			playerBody.applyLinearImpulse(0f, 10f, 0f, 0f, true)

			//extra push in direction of travel
			if (movingLeft) playerBody.applyLinearImpulse(-1f, 0f, 0f, 0f, true)
			if (movingRight) playerBody.applyLinearImpulse(1f, 0f, 0f, 0f, true)

			movingLeft = false
			movingRight = false
		}
	}
}
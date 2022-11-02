package io.github.rolodophone.cssgamejam2022

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import io.github.rolodophone.cssgamejam2022.comp.InfoComp.Tag
import io.github.rolodophone.cssgamejam2022.comp.PlayerComp

class GameContactListener(
	private val gameScreen: GameScreen
): ContactListener {
	private val playerComp = gameScreen.player.getComp(PlayerComp.mapper)

	override fun beginContact(contact: Contact) {
		val a = contact.fixtureA
		val b = contact.fixtureB
		val entityA = a.body.userData as Entity
		val entityB = b.body.userData as Entity

		if (a.userData == 0 && entityB.hasTag(Tag.GROUND) ||
			b.userData == 0 && entityA.hasTag(Tag.GROUND)) {

			//player foot contacted platform
			playerComp.numGroundsTouching++
			playerComp.onGround = true
		}

		// player touching...
		if (entityA.hasTag(Tag.PLAYER) || entityB.hasTag(Tag.PLAYER)) {
			val playerEntity: Entity
			val otherEntity: Entity
			if (entityA.hasTag(Tag.PLAYER)) {
				playerEntity = entityA
				otherEntity = entityB
			}
			else {
				playerEntity = entityB
				otherEntity = entityA
			}

			when {
				otherEntity.hasTag(Tag.SAW) -> {
					gameScreen.die()
				}
				otherEntity.hasTag(Tag.DOOR) -> gameScreen.nextLevel()
			}
		}
	}

	override fun endContact(contact: Contact) {
		if (contact.fixtureA.userData == 0 && (contact.fixtureB.body.userData as Entity).hasTag(Tag.GROUND) ||
				contact.fixtureB.userData == 0 && (contact.fixtureA.body.userData as Entity).hasTag(Tag.GROUND)) {
			//player foot left platform
			playerComp.numGroundsTouching--
			if (playerComp.numGroundsTouching == 0) playerComp.onGround = false
		}
	}

	override fun preSolve(contact: Contact?, oldManifold: Manifold?) {}
	override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {}
}
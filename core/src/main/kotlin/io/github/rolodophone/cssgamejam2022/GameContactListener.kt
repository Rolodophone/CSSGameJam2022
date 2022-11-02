package io.github.rolodophone.cssgamejam2022

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import io.github.rolodophone.cssgamejam2022.comp.InfoComp
import io.github.rolodophone.cssgamejam2022.comp.PlayerComp

class GameContactListener(private val playerComp: PlayerComp): ContactListener {
	override fun beginContact(contact: Contact) {
		if (contact.fixtureA.userData == 0 && (contact.fixtureB.body.userData as Entity).hasTag(InfoComp.Tag.GROUND) ||
				contact.fixtureB.userData == 0 && (contact.fixtureA.body.userData as Entity).hasTag(InfoComp.Tag.GROUND)) {
			//player foot contacted platform
			playerComp.numGroundsTouching++
			playerComp.onGround = true
		}
	}

	override fun endContact(contact: Contact) {
		if (contact.fixtureA.userData == 0 && (contact.fixtureB.body.userData as Entity).hasTag(InfoComp.Tag.GROUND) ||
				contact.fixtureB.userData == 0 && (contact.fixtureA.body.userData as Entity).hasTag(InfoComp.Tag.GROUND)) {
			//player foot left platform
			playerComp.numGroundsTouching--
			if (playerComp.numGroundsTouching == 0) playerComp.onGround = false
		}
	}

	override fun preSolve(contact: Contact?, oldManifold: Manifold?) {}
	override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {}
}
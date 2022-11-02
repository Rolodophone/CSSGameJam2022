package io.github.rolodophone.cssgamejam2022

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import io.github.rolodophone.cssgamejam2022.sys.PlayerSys

class GameContactListener(private val playerSys: PlayerSys): ContactListener {
	override fun beginContact(contact: Contact) {
		if (contact.fixtureA.userData == 0 || contact.fixtureB.userData == 0) {
			//player foot contacted platform
			playerSys.onGround = true
		}
	}

	override fun endContact(contact: Contact) {
		if (contact.fixtureA.userData == 0 || contact.fixtureB.userData == 0) {
			//player foot left platform
			playerSys.onGround = false
		}
	}

	override fun preSolve(contact: Contact?, oldManifold: Manifold?) {}
	override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {}
}
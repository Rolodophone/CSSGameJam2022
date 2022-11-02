package io.github.rolodophone.cssgamejam2022

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import ktx.ashley.get

fun <C: Component> Entity.getCompOrNull(mapper: ComponentMapper<C>) = this[mapper]

fun <C: Component> Entity.getComp(mapper: ComponentMapper<C>): C {
	val component = this[mapper]
	requireNotNull(component) { "Component not found in entity $this" }
	return component
}
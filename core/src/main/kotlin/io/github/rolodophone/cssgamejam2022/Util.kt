package io.github.rolodophone.cssgamejam2022

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import io.github.rolodophone.cssgamejam2022.comp.InfoComp
import ktx.ashley.get
import kotlin.random.Random

fun <C: Component> Entity.getCompOrNull(mapper: ComponentMapper<C>) = this[mapper]

fun <C: Component> Entity.getComp(mapper: ComponentMapper<C>): C {
	val component = this[mapper]
	requireNotNull(component) { "Component not found in entity ${repr()}" }
	return component
}

fun Entity.repr() = getCompOrNull(InfoComp.mapper)?.name ?: "Unnamed Entity"

fun Entity.hasTag(tag: InfoComp.Tag): Boolean {
	val tags = getCompOrNull(InfoComp.mapper)?.tags
	return if (tags == null) false
	else tag in tags
}

fun nextFloat(min: Float, max: Float) = Random.Default.nextFloat() * (max - min) + min
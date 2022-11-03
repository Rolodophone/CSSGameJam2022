package io.github.rolodophone.cssgamejam2022

import com.badlogic.gdx.utils.TimeUtils

class ScheduledFunction(millisLater: Long, private val function: () -> Unit) {
	val millisScheduled = TimeUtils.millis() + millisLater

	fun invoke() = function()
}
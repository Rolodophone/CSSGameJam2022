package io.github.rolodophone.cssgamejam2022

import com.badlogic.gdx.utils.TimeUtils

class ScheduledFunction(private val millisLater: Long, private val function: () -> Unit) {
	var millisScheduled = Long.MAX_VALUE

	fun schedule() {
		millisScheduled = TimeUtils.millis() + millisLater
	}
	fun invoke() = function()
}
package io.github.rolodophone.cssgamejam2022

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Disposable

class MusicManager: Disposable {
	val music = Gdx.audio.newMusic(Gdx.files.internal("music.ogg"))

	init {
		music.isLooping = true
	}

	fun play() {
		music.play()
	}

	override fun dispose() {
		music.dispose()
	}
}
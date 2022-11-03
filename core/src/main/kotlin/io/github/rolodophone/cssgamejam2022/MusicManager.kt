package io.github.rolodophone.cssgamejam2022

import com.badlogic.gdx.Gdx

class MusicManager {
	val music = Gdx.audio.newMusic(Gdx.files.internal("music.ogg"))

	init {
		music.isLooping = true
	}

	fun play() {
		music.play()
	}
}
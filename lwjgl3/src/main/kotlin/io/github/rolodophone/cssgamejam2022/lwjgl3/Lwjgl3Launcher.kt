@file:JvmName("Lwjgl3Launcher")

package io.github.rolodophone.cssgamejam2022.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import io.github.rolodophone.cssgamejam2022.CSSGameJam2022

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(CSSGameJam2022(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("CSSGameJam2022")
        setWindowedMode(1920, 1080)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}

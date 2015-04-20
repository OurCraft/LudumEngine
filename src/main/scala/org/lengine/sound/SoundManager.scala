package org.lengine.sound

import java.net.URL
import java.util.{Map, HashMap, Iterator}

import paulscode.sound.{SoundSystemConfig, SoundSystem}
import paulscode.sound.codecs.{CodecWav, CodecJOgg}
import paulscode.sound.libraries.LibraryLWJGLOpenAL

class SoundManager {

  val soundSystem = new SoundSystem
  SoundSystemConfig.addLibrary(classOf[LibraryLWJGLOpenAL])
  SoundSystemConfig.setCodec("ogg", classOf[CodecJOgg])
  SoundSystemConfig.setCodec("wav", classOf[CodecWav])

  def play(url: URL, id: String): Unit = {
    soundSystem.newStreamingSource(true, id, url, url.toExternalForm.substring(url.toExternalForm.lastIndexOf(".") + 1), false, 0, 0, 0, 0, 0)
    soundSystem.setVolume(id, 0.5f)
    soundSystem.setPitch(id, 1f)
    soundSystem.play(id)
  }

  def play(id: String): Unit = {
    val url = ClassLoader.getSystemResource("assets/sounds/" + id)
    this.play(url, id)
  }

  def stop(id: String): Unit = {
    soundSystem.stop(id)
  }

  def resume(id: String): Unit = {
    soundSystem.play(id)
  }

  def cleanup() = {
    soundSystem.cleanup()
  }
}

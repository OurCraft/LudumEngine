package org.lengine.sound

import java.net.URL
import java.util.{ArrayList}

import paulscode.sound.{SoundSystemConfig, SoundSystem}
import paulscode.sound.codecs.{CodecWav, CodecJOgg}
import paulscode.sound.libraries.LibraryLWJGLOpenAL

import scala.collection.JavaConversions._

class SoundManager {


  val sources = new ArrayList[String]
  SoundSystemConfig.addLibrary(classOf[LibraryLWJGLOpenAL])
  SoundSystemConfig.setCodec("ogg", classOf[CodecJOgg])
  SoundSystemConfig.setCodec("wav", classOf[CodecWav])
  val soundSystem = new SoundSystem
  soundSystem.setMasterVolume(0.25f)
  def play(url: URL, id: String, loop: Boolean): Unit = {
    soundSystem.removeSource(id)
    soundSystem.newStreamingSource(true, id, url, url.toExternalForm.substring(url.toExternalForm.lastIndexOf(".") + 1), loop, 0, 0, 0, 0, 0)
    soundSystem.setVolume(id, 0.5f)
    soundSystem.setPitch(id, 1f)
    soundSystem.play(id)
    sources.add(id)
  }

  def stopAll(): Unit = {
    for(source <- sources) {
      stop(source)
    }
    sources.clear()
  }

  def play(id: String, loop: Boolean = false): Unit = {
    val url = ClassLoader.getSystemResource("assets/sounds/" + id)
    this.play(url, id, loop)
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

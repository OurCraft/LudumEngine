package org.lengine.sound

import java.net.URL
import java.util

import eu.thog92.lwjall.ALSoundProvider
import eu.thog92.lwjall.api.{AbstractSource, ISoundProvider}

import scala.collection.JavaConversions._

class SoundManager {

  val activeSounds: java.util.Map[String, AbstractSource] = new util.HashMap()
  val sourcesCache: java.util.Map[String, AbstractSource] = new util.HashMap()
  val soundProvider: ISoundProvider = new ALSoundProvider


  def play(url: URL, id: String, loop: Boolean): AbstractSource = {
    if (activeSounds.containsKey(id))
      return activeSounds.get(id)

    val source: AbstractSource = soundProvider.newSource(id, url, true)

    source.setGain(0.5F)
    soundProvider.play(id)
    activeSounds.put(id, source)
  }

  def play(id: String): AbstractSource = {
    val url = ClassLoader.getSystemResource("assets/sounds/" + id)
    this.play(url, id, false)
  }

  def stop(id: String): Unit = {
    if (activeSounds.containsKey(id))
      sourcesCache.put(id, activeSounds.remove(id))
  }

  def resume(id: String): Unit = {
    if (!activeSounds.containsKey(id) && sourcesCache.containsKey(id))
      activeSounds.put(id, sourcesCache.remove(id))
  }


  def update(): Unit = {
    soundProvider.update()
  }

  def stopAll() = {
    for (source <- activeSounds) {
      stop(source._1)
    }
    activeSounds.clear()
  }

  def cleanup() = {
    soundProvider.cleanup()
  }
}

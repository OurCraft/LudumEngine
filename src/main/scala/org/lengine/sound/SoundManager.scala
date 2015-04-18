package org.lengine.sound

import java.net.URL
import java.util.{Map, HashMap, Iterator}

import eu.thog92.lwjall.ALSoundProvider
import eu.thog92.lwjall.api.{AbstractSource, ISoundProvider}

class SoundManager {



  val activeSounds: Map[String, AbstractSource] = new HashMap
  val sourcesCache: Map[String, AbstractSource] = new HashMap
  val soundProvider: ISoundProvider = new ALSoundProvider


  def play(url: URL, id: String): AbstractSource = {
    if (activeSounds.containsKey(id))
      return activeSounds.get(id)

    val source: AbstractSource = soundProvider.newSource(id, url, true)

    source.setGain(0.5F)
    soundProvider.play(id)

    activeSounds.put(id, source)
  }

  def play(id: String): AbstractSource = {
    val url = ClassLoader.getSystemResource("assets/sounds/" + id)
    this.play(url, id)
  }

  def stop(id: String): Unit = {
    if(activeSounds.containsKey(id))
      sourcesCache.put(id, activeSounds.remove(id))
  }

  def resume(id: String): Unit = {
    if(!activeSounds.containsKey(id) && sourcesCache.containsKey(id))
      activeSounds.put(id, sourcesCache.remove(id))
  }


  def update(): Unit = {
    val it: Iterator[AbstractSource] = activeSounds.values().iterator()
    while(it.hasNext) {
      val source: AbstractSource = it.next()
      if(soundProvider.isPlaying(source.getName)) {
        source.update
      }
    }
  }

  def cleanup() = {
    soundProvider.cleanup()
  }
}

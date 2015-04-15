package org.lengine.level

import java.util.{ArrayList, List}

import org.lengine.entity.Entity

class Level {

  private val entities: List[Entity] = new ArrayList[Entity]
  private val spawningList: List[Entity] = new ArrayList[Entity]
  private val despawningList: List[Entity] = new ArrayList[Entity]

  def render: Unit = {
    for(i <- 0 until entities.size) {
      val entity: Entity = entities.get(i)
      entity.render
    }
  }

  def update: Unit = {
    entities addAll spawningList
    entities removeAll despawningList
    spawningList.clear
    despawningList.clear
    for(i <- 0 until entities.size) {
      val entity: Entity = entities.get(i)
      entity.update
    }
  }

  def spawn(entity: Entity): Unit = {
    spawningList add entity
  }

  def despawn(entity: Entity): Unit = {
    despawningList add entity
  }
}

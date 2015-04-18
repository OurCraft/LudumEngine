package org.lengine.level

import java.util.{ArrayList, List}

import org.lengine.entity.Entity

class Level {

  val entities: List[Entity] = new ArrayList[Entity]
  private val spawningList: List[Entity] = new ArrayList[Entity]
  private val despawningList: List[Entity] = new ArrayList[Entity]

  def render(delta: Float): Unit = {
    for(i <- 0 until entities.size) {
      val entity: Entity = entities.get(i)
      entity.render(delta)
    }
  }

  def onEntityUpdate(entity: Entity, delta: Float) = {}

  def update(delta: Float): Unit = {
    entities addAll spawningList
    entities removeAll despawningList
    spawningList.clear
    despawningList.clear
    for(i <- 0 until entities.size) {
      val entity: Entity = entities.get(i)
      entity.update(delta)
      onEntityUpdate(entity, delta)
    }
  }

  def spawn(entity: Entity): Unit = {
    entity.level = this
    spawningList add entity
  }

  def despawn(entity: Entity): Unit = {
    despawningList add entity
  }
}

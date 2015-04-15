package org.lengine

import org.lengine.level.Level
import org.lengine.tests.EntityPlayer

object GameTest extends GameBase("test") {

  var level: Level = _

  override def initGame: Unit = {
    level = new Level
    val player: EntityPlayer = new EntityPlayer
    level spawn player

  }

  override def update: Unit = {
    level update
  }

  override def render: Unit = {
    level render
  }
}

package org.lengine

import org.lengine.level.Level
import org.lengine.render.Sprite
import org.lengine.tests.EntityPlayer
import org.lwjgl.input.Mouse

object GameTest extends GameBase("test") {

  var level: Level = _

  var testSprite: Sprite = _

  override def initGame: Unit = {
    level = new Level
    val player: EntityPlayer = new EntityPlayer
    level spawn player
    testSprite = new Sprite("assets/textures/test.png")
    testSprite.width /= 2f
    testSprite.height /= 2f
  }

  override def update: Unit = {
    level update
  }

  override def render: Unit = {
    level.render
    testSprite.setPos(Mouse.getX, Mouse.getY)
    testSprite.render
  }

  override def getBaseHeight: Int = 640
}

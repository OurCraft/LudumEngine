package org.lengine.entity

import org.lengine.level.Level
import org.lengine.maths.Vec2f

abstract class Entity {

  private val pos: Vec2f = new Vec2f
  private var angle: Float = 0
  var level: Level = _
  init

  def render(delta: Float): Unit

  def update(delta: Float): Unit

  def init: Unit

  def getPos: Vec2f = pos

  def setPos(v: Vec2f): Vec2f = pos.set(v)

  def getAngle: Float = angle

  def setAngle(newAngle: Float): Unit = {
    angle = newAngle
  }

  def getDirection: Vec2f = new Vec2f(Math.cos(angle).toFloat, Math.sin(angle).toFloat)

  def setDirection(dir: Vec2f): Unit = {
    angle = dir.angle()
  }
}

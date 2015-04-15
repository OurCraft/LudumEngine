package org.lengine.entity

import org.lengine.maths.Vec2f

abstract class Entity {

  init
  private val pos: Vec2f = new Vec2f
  private var angle: Float = 0

  def render: Unit

  def update: Unit = {
    println(pos)
    println(~pos)
  }

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

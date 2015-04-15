package org.lengine.maths

object X extends Vec2f(1)
object Y extends Vec2f(0,1)

class Vec2f(var x: Float = 0, var y: Float = 0) {

  def dot(other: Vec2f): Float = this.x * other.x + this.y * other.y

  def angle(): Float = Math.acos(dot(X)).toFloat

  def isNull(): Boolean = x == 0f && y == 0f

  def set(other: Vec2f): Vec2f = {
    x = other.x
    y = other.y
    this
  }

  def rotate(angle: Float): Vec2f = {
    val l: Float = ~this
    val nx: Float = (Math.cos(angle) * l).asInstanceOf[Float]
    val ny: Float = (-Math.sin(angle) * l).asInstanceOf[Float]
    x = nx
    y = ny
    this
  }

  def +(other: Vec2f): Vec2f = {
    new Vec2f(x + other.x, y + other.y)
  }

  def /=(other: Vec2f): Vec2f = {
    x /= other.x
    y /= other.y
    this
  }

  def *=(other: Vec2f): Vec2f = {
    x *= other.x
    y *= other.y
    this
  }

  def -=(other: Vec2f): Vec2f = {
    x -= other.x
    y -= other.y
    this
  }

  def +=(other: Vec2f): Vec2f = {
    x += other.x
    y += other.y
    this
  }

  def unary_-(): Vec2f = {
    new Vec2f(-x, -y)
  }

  def -(other: Vec2f): Vec2f = {
    new Vec2f(x - other.x, y - other.y)
  }

  def *(other: Vec2f): Vec2f = {
    new Vec2f(x * other.x, y * other.y)
  }

  def /(other: Vec2f): Vec2f = {
    new Vec2f(x / other.x, y / other.y)
  }

  def unary_~(): Float = len

  def len: Float = {
    val dx: Float = x*x
    val dy: Float = y*y
    Math.sqrt(dx+dy).toFloat
  }

  override def toString: String = s"Vec2f($x, $y)"
}

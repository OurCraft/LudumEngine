package org.lengine.maths

object NULL2 extends Vec2f
object X2 extends Vec2f(1,0)
object Y2 extends Vec2f(0,1)

class Vec2f(var x: Float = 0, var y: Float = 0) {
  def lerp(other: Vec2f, factor: Float): Vec2f = {
    new Vec2f(x * (1f-factor) + other.x * factor, y * (1f-factor) + other.y * factor)
  }

  def norm(): Vec2f = {
    if(isNull())
      this
    else {
      val l = ~this
      x /= l
      y /= l
      this
    }
  }

  def toVec3(): Vec3f = new Vec3f(x,y,0)


  def dot(other: Vec2f): Float = this.x * other.x + this.y * other.y

  def angle(): Float = Math.acos(dot(X2)).toFloat

  def isNull(): Boolean = x == 0f && y == 0f

  def set(x: Float, y: Float): Vec2f = {
    this.x = x
    this.y = y
    this
  }

  def set(other: Vec2f): Vec2f = {
    x = other.x
    y = other.y
    this
  }

  def rotate(angle: Float): Vec2f = {
    val l: Float = ~this
    val nx: Float = (Math.cos(angle) * l).toFloat
    val ny: Float = (-Math.sin(angle) * l).toFloat
    x = nx
    y = ny
    this
  }

  def +(other: Float): Vec2f = {
    new Vec2f(x + other, y + other)
  }

  def /=(other: Float): Vec2f = {
    x /= other
    y /= other
    this
  }

  def *=(other: Float): Vec2f = {
    x *= other
    y *= other
    this
  }

  def -=(other: Float): Vec2f = {
    x -= other
    y -= other
    this
  }

  def +=(other: Float): Vec2f = {
    x += other
    y += other
    this
  }

  def -(other: Float): Vec2f = {
    new Vec2f(x - other, y - other)
  }

  def *(other: Float): Vec2f = {
    new Vec2f(x * other, y * other)
  }

  def /(other: Float): Vec2f = {
    new Vec2f(x / other, y / other)
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


  def -(other: Vec2f): Vec2f = {
    new Vec2f(x - other.x, y - other.y)
  }

  def *(other: Vec2f): Vec2f = {
    new Vec2f(x * other.x, y * other.y)
  }

  def /(other: Vec2f): Vec2f = {
    new Vec2f(x / other.x, y / other.y)
  }

  def +(other: (Float, Float)): Vec2f = {
    new Vec2f(x + other._1, y + other._2)
  }

  def /=(other: (Float, Float)): Vec2f = {
    x /= other._1
    y /= other._2
    this
  }

  def *=(other: (Float, Float)): Vec2f = {
    x *= other._1
    y *= other._2
    this
  }

  def -=(other: (Float, Float)): Vec2f = {
    x -= other._1
    y -= other._2
    this
  }

  def +=(other: (Float, Float)): Vec2f = {
    x += other._1
    y += other._2
    this
  }

  def -(other: (Float, Float)): Vec2f = {
    new Vec2f(x - other._1, y - other._2)
  }

  def *(other: (Float, Float)): Vec2f = {
    new Vec2f(x * other._1, y * other._2)
  }

  def /(other: (Float, Float)): Vec2f = {
    new Vec2f(x / other._1, y / other._2)
  }

  def ==(other: Vec2f): Boolean = {
    x == other.x && y == other.y
  }

  def len: Float = {
    val dx: Float = x*x
    val dy: Float = y*y
    Math.sqrt(dx+dy).toFloat
  }

  def unary_~(): Float = len

  def unary_-(): Vec2f = new Vec2f(-x, -y)

  override def toString: String = s"Vec2f($x, $y)"
}

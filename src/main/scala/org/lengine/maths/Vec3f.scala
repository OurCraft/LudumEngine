package org.lengine.maths

object NULL3 extends Vec3f
object X3 extends Vec3f(1,0,0)
object Y3 extends Vec3f(0,1,0)
object Z3 extends Vec3f(0,0,1)

class Vec3f(var x: Float = 0, var y: Float = 0, var z: Float = 0) {

  def copy: Vec3f = new Vec3f(x,y,z)

  def rotate(angle: Float, axis: Vec3f = Z3): Vec3f = {
    val sinAngle: Float = Math.sin(-angle).toFloat
    val cosAngle: Float = Math.cos(-angle).toFloat
    val naxis = axis.copy
    val cross: Vec3f = copy.cross(naxis * sinAngle)
    val mul: Vec3f = copy * cosAngle
    val dotResult: Float = dot(naxis * (1f - cosAngle))

    cross + mul + naxis * dotResult
  }

  def rotate(rotation: Quaternion): Vec3f = {
    val conjugate: Quaternion = rotation.conjugate
    val w: Quaternion = rotation.mul(copy).mul(conjugate)
    x = w.x
    y = w.y
    z = w.z

    this
  }

  def xy(): Vec2f = new Vec2f(x,y)
  def yx(): Vec2f = new Vec2f(y,x)
  def xz(): Vec2f = new Vec2f(x,z)
  def zx(): Vec2f = new Vec2f(z,x)
  def yz(): Vec2f = new Vec2f(y,z)
  def zy(): Vec2f = new Vec2f(z,y)

  def cross(other: Vec3f): Vec3f = {
    cross(other.x, other.y, other.z)
  }

  def cross(x: Float, y: Float, z: Float): Vec3f = {
    val nx: Float = this.y * z - this.z * y
    val ny: Float = this.x * z - this.z * x
    val nz: Float = this.x * y - this.y * x
    this.x = nx
    this.y = ny
    this.z = nz
    this
  }

  def dot(other: Vec3f): Float = this.x * other.x + this.y * other.y + this.z * other.z

  def angle(): Float = Math.acos(dot(X3)).toFloat

  def isNull(): Boolean = x == 0f && y == 0f

  def set(other: Vec3f): Vec3f = {
    x = other.x
    y = other.y
    this
  }

  def +(other: Float): Vec3f = {
    new Vec3f(x + other, y + other, z + other)
  }

  def /=(other: Float): Vec3f = {
    x /= other
    y /= other
    z /= other
    this
  }

  def *=(other: Float): Vec3f = {
    x *= other
    y *= other
    z *= other
    this
  }

  def -=(other: Float): Vec3f = {
    x -= other
    y -= other
    z -= other
    this
  }

  def +=(other: Float): Vec3f = {
    x += other
    y += other
    z += other
    this
  }

  def -(other: Float): Vec3f = {
    new Vec3f(x - other, y - other, z - other)
  }

  def *(other: Float): Vec3f = {
    new Vec3f(x * other, y * other, z * other)
  }

  def /(other: Float): Vec3f = {
    new Vec3f(x / other, y / other, z / other)
  }

  def +(other: Vec3f): Vec3f = {
    new Vec3f(x + other.x, y + other.y, z + other.z)
  }

  def /=(other: Vec3f): Vec3f = {
    x /= other.x
    y /= other.y
    z /= other.z
    this
  }

  def *=(other: Vec3f): Vec3f = {
    x *= other.x
    y *= other.y
    z *= other.z
    this
  }

  def -=(other: Vec3f): Vec3f = {
    x -= other.x
    y -= other.y
    z -= other.z
    this
  }

  def +=(other: Vec3f): Vec3f = {
    x += other.x
    y += other.y
    z += other.z
    this
  }


  def -(other: Vec3f): Vec3f = {
    new Vec3f(x - other.x, y - other.y, z - other.z)
  }

  def *(other: Vec3f): Vec3f = {
    new Vec3f(x * other.x, y * other.y, z * other.z)
  }

  def /(other: Vec3f): Vec3f = {
    new Vec3f(x / other.x, y / other.y, z / other.z)
  }

  def +(other: (Float, Float, Float)): Vec3f = {
    new Vec3f(x + other._1, y + other._2, z + other._3)
  }

  def /=(other: (Float, Float, Float)): Vec3f = {
    x /= other._1
    y /= other._2
    z /= other._3
    this
  }

  def *=(other: (Float, Float, Float)): Vec3f = {
    x *= other._1
    y *= other._2
    z *= other._3
    this
  }

  def -=(other: (Float, Float, Float)): Vec3f = {
    x -= other._1
    y -= other._2
    z -= other._3
    this
  }

  def +=(other: (Float, Float, Float)): Vec3f = {
    x += other._1
    y += other._2
    z += other._3
    this
  }

  def -(other: (Float, Float, Float)): Vec3f = {
    new Vec3f(x - other._1, y - other._2, z - other._3)
  }

  def *(other: (Float, Float, Float)): Vec3f = {
    new Vec3f(x * other._1, y * other._2, z * other._3)
  }

  def /(other: (Float, Float, Float)): Vec3f = {
    new Vec3f(x / other._1, y / other._2, z / other._3)
  }

  def len: Float = {
    val dx: Float = x*x
    val dy: Float = y*y
    val dz: Float = z*z
    Math.sqrt(dx+dy+dz).toFloat
  }

  def unary_~(): Float = len

  def unary_-(): Vec3f = new Vec3f(-x, -y, -z)

  override def toString: String = s"Vec3f($x, $y, $z)"
}
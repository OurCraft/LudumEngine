package org.lengine.maths

import java.nio._

class Mat4f {
  private val data: Array[Float] = new Array[Float](4 * 4)

  def mul(other: Mat4f): Mat4f = {
    val newMat = new Mat4f
    for(i <- 0 until 4) {
      for(j <- 0 until 4) {
        val firstRow: Float = get(i, 0) * other.get(0, j)
        val secondRow: Float = get(i, 1) * other.get(1, j)
        val thirdRow: Float = get(i, 2) * other.get(2, j)
        val fourthRow: Float = get(i, 3) * other.get(3, j)
        newMat.set(i, j, firstRow + secondRow + thirdRow + fourthRow)
      }
    }
    newMat
  }

  def copy: Mat4f = {
    new Mat4f().set(this)
  }

  def identity: Mat4f = {
    all(0)
    set(0, 0, 1)
    set(1, 1, 1)
    set(2, 2, 1)
    set(3, 3, 1)
    this
  }

  def translation(x: Float, y: Float, z: Float): Mat4f = {
    identity
    set(0, 3, x)
    set(1, 3, y)
    set(2, 3, z)
    this
  }

  def scale(x: Float, y: Float, z: Float): Mat4f = {
    identity
    set(0, 0, x)
    set(1, 1, y)
    set(2, 2, z)
    this
  }

  def rotation(x: Float, y: Float, z: Float): Mat4f = {
    val rotx: Mat4f = new Mat4f().identity
    val roty: Mat4f = new Mat4f().identity
    val rotz: Mat4f = new Mat4f().identity
    rotx.set(1, 1, Math.cos(x).toFloat)
    rotx.set(1, 2, -Math.sin(x).toFloat)
    rotx.set(2, 1, Math.sin(x).toFloat)
    rotx.set(2, 2, Math.cos(x).toFloat)

    roty.set(0, 0, Math.cos(y).toFloat)
    roty.set(0, 2, -Math.sin(y).toFloat)
    roty.set(2, 0, Math.sin(y).toFloat)
    roty.set(2, 2, Math.cos(y).toFloat)

    rotz.set(0, 0, Math.cos(z).toFloat)
    rotz.set(0, 1, -Math.sin(z).toFloat)
    rotz.set(1, 0, Math.sin(z).toFloat)
    rotz.set(1, 1, Math.cos(z).toFloat)

    set(rotz.mul(roty.mul(rotx)))
  }

  def perspective(fov: Float, aspectRatio: Float, near: Float, far: Float): Mat4f = {
    all(0)
    val tanHlfFov: Float = Math.tan(fov / 2).asInstanceOf[Float]
    val zRange: Float = near - far
    set(0, 0, 1.0f / (tanHlfFov * aspectRatio))
    set(1, 1, 1.0f / tanHlfFov)
    set(2, 2, (-near - far) / zRange)
    set(2, 3, 2 * far * near / zRange)
    set(3, 2, 1)
    this
  }

  def orthographic(left: Float, right: Float, bottom: Float, top: Float, near: Float = -1, far: Float = 1): Mat4f = {
    all(0)
    val width: Float = right - left
    val height: Float = top - bottom
    val depth: Float = far - near
    set(0, 0, 2f / width)
    set(0, 3, -(right + left) / width)
    set(1, 1, 2f / height)
    set(1, 3, -(top + bottom) / height)
    set(2, 2, -2f / depth)
    set(2, 3, -(far + near) / depth)
    set(3, 3, 1f)
    this
  }

  def transform(r: Vec2f): Vec3f = {
    transform(r.toVec3)
  }

  def transform(r: Vec3f): Vec3f = {
    val x: Float = get(0, 0) * r.x + get(0, 1) * r.y + get(0, 2) * r.z + get(0, 3)
    val y: Float = get(1, 0) * r.x + get(1, 1) * r.y + get(1, 2) * r.z + get(1, 3)
    val z: Float = get(2, 0) * r.x + get(2, 1) * r.y + get(2, 2) * r.z + get(2, 3)
    r.x = x
    r.y = y
    r.z = z
    r
  }

  def set(other: Mat4f): Mat4f = {
    for(i <- 0 until 16) {
      data(i) = other.data(i)
    }
    this
  }

  def set(x: Int, y: Int, value: Float): Mat4f = {
    data(y + x * 4) = value
    this
  }

  def get(x: Int, y: Int): Float = {
    data(y + x * 4)
  }

  def all(value: Float): Mat4f = {
    for(i <- 0 until 16) {
      data(i) = value
    }
    this
  }

  def rotation(forward: Vec3f, up: Vec3f, right: Vec3f): Mat4f = {
    all(0)

    set(0, 0, right.x)
    set(0, 1, right.y)
    set(0, 2, right.z)

    set(1, 0, up.x)
    set(1, 1, up.y)
    set(1, 2, up.z)

    set(2, 0, forward.x)
    set(2, 1, forward.y)
    set(2, 2, forward.z)

    set(3, 3, 1)

    this
  }

}

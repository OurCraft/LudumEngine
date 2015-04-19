package org.lengine.render

import java.util.{HashMap, Map}

import org.lengine.maths.{Vec3f, Vec2f, Quaternion}

private class TextData {
  var text: String = _
  var x: Float = _
  var y: Float = _
  var scale: Float = _
  var color: Int = _

  override def hashCode(): Int = {
    val BASE: Int = 17
    val MULTIPLIER: Int = 31
    var result: Int = BASE
    result = MULTIPLIER * result + java.lang.Float.floatToRawIntBits(y)
    result = MULTIPLIER * result + java.lang.Float.floatToRawIntBits(x)
    result = MULTIPLIER * result + color
    result = MULTIPLIER * result + java.lang.Float.floatToRawIntBits(scale)
    result = MULTIPLIER * result + text.hashCode
    result
  }

  override def equals(o: Any): Boolean = {
    if (o.isInstanceOf[TextData]) {
      val infos: TextData = o.asInstanceOf[TextData]
      (infos.text == text) && infos.color == color && infos.x == x && infos.y == y && infos.scale == scale
    } else {
      false
    }
  }
}

class FontRenderer(val atlas: TextureAtlas) {
  def getWidth(s: String) = {
    s.length * (16f-4f)
  }


  private val cache: Map[TextData, VertexArray] = new HashMap[TextData, VertexArray]

  private var check: TextData = new TextData

  def renderString(text: String, x: Float, y: Float, color: Int = 0xFFFFFFFF, scale: Float = 1f): Unit = {
    check.color = color
    check.text = text
    check.x = x
    check.y = y
    check.scale = scale
    var bufferToRender: VertexArray = null
    if(cache.containsKey(check)) {
      val buffer: VertexArray = cache.get(check)
      bufferToRender = buffer
    } else {
      val buffer: VertexArray = new VertexArray

      val a: Float = (color >> 24 & 0xFF) / 255f
      val r: Float = (color >> 16 & 0xFF) / 255f
      val g: Float = (color >> 8 & 0xFF) / 255f
      val b: Float = (color & 0xFF) / 255f

      val colorQuaternion: Quaternion = new Quaternion(r,g,b,a)

      var xpos: Float = x
      var currentIndex: Int = 0
      val w: Float = 16f * scale
      val h: Float = 16f * scale
      for(c <- text) {
        if(c == ' ') {
          // NOOP
        } else {
          val xIndex: Int = c.toInt % atlas.xFrequency
          val yIndex: Int = c.toInt / atlas.xFrequency
          val region: TextureRegion = atlas.getRegion(xIndex, yIndex)


          buffer.defineVertex(new Vec3f(xpos, y, 0), new Vec2f(region.minU, region.maxV), colorQuaternion)
          buffer.defineVertex(new Vec3f(xpos + w, y, 0), new Vec2f(region.maxU, region.maxV), colorQuaternion)
          buffer.defineVertex(new Vec3f(xpos + w, y + h, 0), new Vec2f(region.maxU, region.minV), colorQuaternion)
          buffer.defineVertex(new Vec3f(xpos, y + h, 0), new Vec2f(region.minU, region.minV), colorQuaternion)

          buffer.defineIndex(1 + currentIndex)
          buffer.defineIndex(0 + currentIndex)
          buffer.defineIndex(2 + currentIndex)

          buffer.defineIndex(2 + currentIndex)
          buffer.defineIndex(0 + currentIndex)
          buffer.defineIndex(3 + currentIndex)

          currentIndex += 4
        }

        xpos += w - 4f
      }

      buffer.compile
      bufferToRender = buffer
      cache.put(check, buffer)
      check = new TextData
    }
    atlas.texture.bind
    bufferToRender.quickRender
  }
}

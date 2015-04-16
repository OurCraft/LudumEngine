package org.lengine.render

import org.lengine.maths.Vec2f

class TextureAtlas(val texture: Texture, val tileWidth: Int, val tileHeight: Int) {
  val xFrequency: Int = texture.getWidth/tileWidth
  val yFrequency: Int = texture.getHeight/tileHeight

  val regions: Array[TextureRegion] = new Array[TextureRegion](xFrequency*yFrequency)
  val sprites: Array[Sprite] = new Array[Sprite](xFrequency*yFrequency)

  for(x <- 0 until xFrequency) {
    for(y <- 0 until yFrequency) {
      val minUV: Vec2f = halfPixelCorrection(x*tileWidth, y*tileHeight)
      val maxUV: Vec2f = halfPixelCorrection((x+1)*tileWidth, (y+1)*tileHeight)
      val region: TextureRegion = new TextureRegion(minUV.x, minUV.y, maxUV.x, maxUV.y)
      regions(x+y*tileWidth) = region
      sprites(x+y*tileWidth) = new Sprite(texture, region)
    }
  }

  private def halfPixelCorrection(x: Int, y: Int): Vec2f = {
    val xpos: Float = x + .5f
    val ypos: Float = y + .5f
    val u: Float = xpos / texture.getWidth.toFloat
    val v: Float = ypos / texture.getHeight.toFloat
    new Vec2f(u, v)
  }

  def getSprite(x: Int, y: Int): Sprite = {
    sprites(y*tileWidth+x)
  }

  def getRegion(x: Int, y: Int): TextureRegion = {
    regions(y*tileWidth+x)
  }
}

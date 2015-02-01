package org.mbari.openimaj

import java.lang.{Float => JFloat}

import org.openimaj.image.FImage
import org.openimaj.image.processor.{SinglebandImageProcessor, SinglebandKernelProcessor, PixelProcessor}

/**
 * Handles the boiler plate to manage the dissonance between Java and Scala Array[Float] stuff
 */
class SFloatPixelProcessor(fn: Array[Float] => Array[Float]) extends PixelProcessor[Array[JFloat]] {
  override def processPixel(pixel: Array[JFloat]): Array[JFloat] = {
    val floatArray: Array[Float] = pixel.map(_.floatValue()) // Map to java primitives which align with Scala types
    val newPixels = fn(floatArray) // execute function
    newPixels.map(JFloat.valueOf)  // Map back to java primitives
  }
}

class SPixelProcessor(fn: Float => Float) extends PixelProcessor[JFloat] {
  override def processPixel(q: JFloat): JFloat = fn(q)
}

/**
 * Does neighborhood processing for each band in an image. Areas outside of the
 * processable regions (i.e. the edges) are set to 0 (BLACK). A copy of the original is made internally to preserve
 * the original values during processing. Usage is like:
 * {{{
 * val averager = new NeighborhoodProcessor(5, 5,
 *     (a: Array[Float]) => a.sum / a.size.toFloat)
 * val newImage = image.process(averager)
 * }}}
 *
 * @param width THe width of the window (must be odd)
 * @param height The height of the window (must be odd)
 * @param fn Function that converts all values within the window to a single value. The
 *           value is applied at the center of the window.
 */
class NeighborhoodProcessor(width: Int, height: Int, fn: Array[Float] => Float, missingValue: Float = 0F)
    extends SinglebandImageProcessor[JFloat, FImage] {
  assume(width % 2 == 1, "Width must be odd")
  assume(height % 2 == 1, "Height must be odd")
  
  private[this] val xOffset = (width - 1) / 2
  private[this] val yOffset = (height - 1) / 2

  override def processImage(image: FImage): Unit = {

    // --- Apply the function
    val copy = image.clone()
    for {
      x <- xOffset until image.getCols - xOffset
      y <- yOffset until image.getRows - yOffset
    }  {
      val i = copy.extractROI(x - xOffset, y - yOffset, width, height).getFloatPixelVector
      val v = fn(i)
      image.setPixel(x, y, v)
    }

    // --- Crop the edges
    for {
      x <- 0 until xOffset
      y <- 0 until image.getRows
    } {
      image.setPixel(x, y, missingValue)
      image.setPixel(image.getWidth - x - 1, y, missingValue)
    }

    for {
      x <- xOffset until image.getCols - xOffset
      y <- 0 until yOffset
    } {
      image.setPixel(x, y, missingValue)
      image.setPixel(x, image.getHeight - y - 1, missingValue)
    }
  }
}

/**
 * Similar to NeighboorhoodProcessor but applies the value to all pixels within a block
 * @param width
 * @param height
 * @param fn
 */
class BlockProcessor(width: Int, height: Int, fn: Array[Float] => Float, missingValue: Float = 0F)
    extends SinglebandImageProcessor[JFloat, FImage] {

  assume(width % 2 == 1, "Width must be odd")
  assume(height % 2 == 1, "Height must be odd")

  private[this] val xOffset = (width - 1) / 2
  private[this] val yOffset = (height - 1) / 2

  override def processImage(image: FImage): Unit = {

    val windowSize = width * height

    // --- Apply the function
    val copy = image.clone()
    for {
      x <- xOffset until (image.getCols - xOffset) by width
      y <- yOffset until (image.getRows - yOffset) by height
    } {

      val i = copy.extractROI(x - xOffset, y - yOffset, width, height).getFloatPixelVector
      val v = fn(i)

      for {
        xi <- (x - xOffset) to (x + xOffset)
        yi <- (y - yOffset) to (y + yOffset)
      } {
        image.setPixel(xi, yi, v)
      }
    }

    // --- Crop the edges
    val rx = image.getWidth % width
    if (rx != 0) {
      for {
        x <- image.getWidth - rx until image.getWidth
        y <- 0 until image.getHeight
      } image.setPixel(x, y, missingValue)
    }

    val ry = image.getHeight % height
    if (ry != 0) {
      for {
        x <- 0 until image.getWidth
        y <- image.getHeight - ry until image.getHeight
      } image.setPixel(x, y, missingValue)
    }

  }

}

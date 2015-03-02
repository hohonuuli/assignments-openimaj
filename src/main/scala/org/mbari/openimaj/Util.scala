package org.mbari.openimaj

import java.awt.image.BufferedImage
import java.io.File
import java.net.URL

import breeze.linalg.DenseMatrix
import org.mbari.swing.{JImageFrame, Swing}
import org.openimaj.image.{FImage, ImageUtilities, MBFImage}

import scala.math._

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-01-07T10:39:00
 */
object Util {

  val IMAGE_SMALL_TEST = getClass.getResource("/images/smalltestimage.png")

  val IMAGE_LENNA = getClass.getResource("/images/Lenna.png")

  val IMAGE_CIRCLES2 = getClass.getResource("/images/circles2.gif")

  def read(url: URL): MBFImage = ImageUtilities.readMBF(url)

  def read(location: String): MBFImage = {
    if (location.startsWith("file:") || location.startsWith("http")) {
      ImageUtilities.readMBF(new URL(location))
    }
    else {
      ImageUtilities.readMBF(new File(location))
    }
  }

  def toNextPowerOf2(i: Int): Int = pow(2, ceil(log(i.toDouble) / log(2))).toInt

  def toNearestPowerOf2(i: Int): Int = {
    val a = toNextPowerOf2(i)
    val b = pow(2, sqrt(a) - 1)
    val da = abs(i - a)
    val db = abs(i - b)
    if (db <= da) b.toInt else a.toInt
  }

  def toBufferedImage(image: MBFImage): BufferedImage =
      ImageUtilities.createBufferedImageForDisplay(image)

  def toMatrix(image: MBFImage, band: Int): DenseMatrix[Float] = toMatrix(image.getBand(band))

  def toMatrix(image: FImage): DenseMatrix[Float] = {
    val data = image.getFloatPixelVector
    new DenseMatrix(image.getRows, image.getCols, data).t
  }

  /**
   * Maps a matrix of values between 0-255 into a FImage with values between
   * 0-1.
   *
   * @param matrix
   * @return
   */
  def toFImage(matrix: DenseMatrix[Float]): FImage = {
    val data = matrix.data.map(_ / 255F)
    new FImage(data, matrix.cols, matrix.rows)
  }

  def namedWindow(name: String, image: MBFImage): JImageFrame = {
    val frame = Swing.namedWindow(name, toBufferedImage(image))
    val height = if (image.getRows < 400) 400 else image.getRows
    val width = if (image.getCols < 400) 400 else image.getCols
    frame.setSize(width, height)
    frame
  }

  def namedWindow(name: String, image: FImage): JImageFrame =
      namedWindow(name, new MBFImage(image, image, image))




}

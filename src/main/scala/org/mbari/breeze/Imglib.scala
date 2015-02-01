package org.mbari.breeze

import breeze.linalg.DenseMatrix
import scilube.Matlib
import scala.math._

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-01-22T14:07:00
 */
object Imglib {

  /**
   * Histogram Equalization
     * @param i
   * @param min
   * @param max
   * @param n
   * @return
   */
  def histeq(i: DenseMatrix[Double],
      min: Double = 0,
      max: Double = 255,
      n: Int = 256): DenseMatrix[Double] = {
    val (centers, counts) = hist(i, min, max, n)
    val cdf = Matlib.cumsum(counts)
    val cdfMin = centers(cdf.indexOf(cdf.min))
    def hv(v: Double) = {
      val idx = Matlib.near(centers, v)
      val cdfV = cdf(idx)
      (round((cdfV - cdfMin) / (i.size - cdfMin) * (n - 2)) + 1).toDouble
    }

    i.map(hv)

  }

  /**
   * Return a histogram of the matrix
   * @param i the matrix
   * @param min The minimum value in the histogram. The default is 0
   * @param max The maximum value in the histogram. The default is 255
   * @param n The number of values in the histogram. The default is 256
   * @return A tuple. The first value is the array of centers, the second is
   *         an array of the histogram values.
   */
  def hist(i: DenseMatrix[Double],
      min: Double = 0,
      max: Double = 255,
      n: Int = 256): (Array[Double], Array[Double]) = {

    val a = i.toArray
    val c = Matlib.linspace(0, max, n)
    val h = Matlib.hist(a, c, inclusive = false)
    (c, h)
  }

  /**
   * Apply median filter to Matrix
   * @param i The matrix
   * @param width The width of the neighborhood, should be odd
   * @param height The height of the neighborhood, should be odd
   * @return A median filtered matrixf
   */
  def medfilt2(i: DenseMatrix[Double], width: Int = 3, height: Int = 3): DenseMatrix[Double] = {
    def fn(m: DenseMatrix[Double]) = Matlib.median(m.toArray)
    val op = new NeighborhoodOp(width, height, fn)
    op(i)
  }

}

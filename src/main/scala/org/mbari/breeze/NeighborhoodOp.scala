package org.mbari.breeze

import breeze.linalg.DenseMatrix

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-01-22T15:32:00
 */
class NeighborhoodOp(width: Int, height: Int, fn: (DenseMatrix[Double]) => Double, missingValue: Double = 0) {

  assume(width % 2 == 1, "Width must be odd")
  assume(height % 2 == 1, "Height must be odd")

  private[this] val colOffset = (width - 1) / 2
  private[this] val rowOffset = (height - 1) / 2

  def apply(i: DenseMatrix[Double]): DenseMatrix[Double] = {


    val n = DenseMatrix.zeros[Double](i.rows, i.cols)

    for {
      r <- rowOffset until (i.rows - rowOffset)
      c <- colOffset until (i.cols - colOffset)
    } {
      // With the same logical layout of pixels:
      //   indexing in matrix is (row, col) i.e. (y, x)
      //   indexing in FImage is (x, y) i.e. (col, row)
      val rows = (r - rowOffset) to (r + rowOffset)
      val cols = (c - colOffset) to (c + colOffset)
      n(r, c) = fn(i(rows, cols))
    }

    n

  }

}

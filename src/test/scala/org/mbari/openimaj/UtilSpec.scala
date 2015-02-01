package org.mbari.openimaj

import org.scalatest.{Matchers, FlatSpec}

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-01-07T15:00:00
 */
class UtilSpec extends FlatSpec with Matchers {

  "Breeze functions" should "do a round-trip from FImage -> DenseMatrix -> FImage" in {
    val image = Util.read(Util.IMAGE_SMALL_TEST)
    val r0 = image.getBand(0)
    val m = Util.toMatrix(r0)  // To Breeze
    r0.getRows should equal (m.rows)
    r0.getCols should equal (m.cols)
    for {
      row <- 0 until m.rows
      col <- 0 until m.cols
    } m(row, col) === r0.getPixel(row, col)

    val r1 = Util.toFImage(m) // To OpenImaj
    r0 === r1
  }

}

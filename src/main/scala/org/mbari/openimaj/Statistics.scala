package org.mbari.openimaj

import org.openimaj.image.FImage
import scilube.Matlib

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-01-30T15:50:00
 */
object Statistics {

  def meanAndStd(fimage: FImage): (Double, Double) = {
    val data = fimage.pixels.flatten.map(_.toDouble)
    (Matlib.mean(data), Matlib.std(data))
  }
}

package org.mbari.openimaj.fn

import org.openimaj.image.FImage

import scala.util.Random
import scilube.Matlib

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-01-24T10:27:00
 */
object SupportFunctions {

  /**
   * Create a function that generates salt and pepper noise at the given probability
   * @param noiseProbability A value between 0 and 1
   * @return A function that may flip a value to 0 or 1 using the giving probability of change
   */
  def saltAndPepperNoise(noiseProbability: Double): Float => Float = {

    if (noiseProbability <= 0) (f: Float) => f
    else if (noiseProbability >= 1) {
      (f: Float) => {
        val v = Random.nextBoolean()
        if (v) 1F else 0F
      }
    }
    else {
      (f: Float) => {
        val p = Random.nextDouble()
        if (p <= noiseProbability) {
          if (Random.nextBoolean()) 1F else 0F
        }
        else f
      }
    }
  }

}

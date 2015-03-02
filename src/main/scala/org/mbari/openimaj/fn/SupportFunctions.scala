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
   * Create a function that generates salt and pepper noise at the given probability. The
   * usage is: {{{
   *   val fn = SupportFunction.saltAndPepperNoise(0.1) // 10% probablity of noise
   *   val v = fn(0.5F) // v is 90% likely to be original value, 10% likly to be 0 or 1
   *
   *   // To apply to an array:
   *   val a = (0 to 100).map(_ / 100) // create a ramp from 0 to 1
   *   val b = a.map(fn)              // add salt and pepper noise to 10% of the values
   * }}}
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

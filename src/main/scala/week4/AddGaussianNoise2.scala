package week4

import java.util.Random

import cern.jet.random.Normal
import cern.jet.random.engine.MersenneTwister
import org.mbari.openimaj.fn.SupportFunctions
import org.mbari.openimaj.{SFloatPixelProcessor, SPixelProcessor, Statistics, Util}
import org.openimaj.image.{MBFImage}

/**
 * Adds
 *
 * @author Brian Schlining
 * @since 2015-01-30T15:37:00
 */
object AddGaussianNoise2 extends App {

  val image = Util.read(Util.IMAGE_LENNA)
  Util.namedWindow("Original", image)

  // --- Add Gaussian noise (i.e Normal distribution)

  // used to get a random value from a normal distribution
  // TO vary the effect of the noise, just change the standard
  // deviation, 1 is crazy noise, 0 is no noise.
  val std = 0.25
  val normal = new Normal(0, std, new MersenneTwister())
  val fn1 = (a: Array[Float]) => a.map(_ + normal.nextDouble().toFloat)
  val noiseProcessor = new SFloatPixelProcessor(fn1)
  val noisyImage = image.process(noiseProcessor)
  Util.namedWindow(f"Gaussian, std = $std%.2f", noisyImage)



}

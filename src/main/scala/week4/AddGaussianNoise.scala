package week4

import java.util.Random

import cern.jet.random.Normal
import cern.jet.random.engine.MersenneTwister
import org.mbari.openimaj.{SPixelProcessor, Statistics, Util}
import org.openimaj.image.{MBFImage}

/**
 * Adds Gaussian noise to an image based using per band statistics
 *
 * @author Brian Schlining
 * @since 2015-01-30T15:37:00
 */
object AddGaussianNoise extends App {

  val image = Util.read(Util.IMAGE_LENNA)
  Util.namedWindow("Original", image)

  // --- Add Gaussian noise (i.e Normal distribution)
  // We apply it to each band as each bands mean and std will be different
  val noisyBands = for (i <- 0 until image.numBands()) yield {
    val band = image.getBand(i)
    val (mean, std) = Statistics.meanAndStd(band)

    // used to get a random value from a normal distribution
    //val normal = new Normal(mean, std, new MersenneTwister())
    val normal = new Normal(0, std, new MersenneTwister())

    // used to determine if we add or subtract
    val random = new Random

    val fn = (a: Float) => if (random.nextBoolean()) a + normal.nextDouble().toFloat
        else a - normal.nextDouble().toFloat
    val noiseProcessor = new SPixelProcessor(fn)
    band.process(noiseProcessor)
  }
  val noisyImage = new MBFImage(noisyBands:_*)
  Util.namedWindow("Gaussian Noise, params from band stats", noisyImage)


}

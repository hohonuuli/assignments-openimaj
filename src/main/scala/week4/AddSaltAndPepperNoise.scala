package week4

import org.mbari.openimaj.{SFloatPixelProcessor, Util}
import org.mbari.openimaj.fn.SupportFunctions

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-01-30T15:35:00
 */
object AddSaltAndPepperNoise extends App {

  val image = Util.read(Util.IMAGE_LENNA)
  Util.namedWindow("Original", image)

  // --- Add noise

  // saltAndPepperNoise takes a probablity that noise will happen
  val fn0 = SupportFunctions.saltAndPepperNoise(0.1)
  val fn1 = (a: Array[Float]) => a.map(fn0)
  val noiseProcessor = new SFloatPixelProcessor(fn1)
  val noisyImage = image.process(noiseProcessor)
  Util.namedWindow("Salt and Pepper", noisyImage)

}

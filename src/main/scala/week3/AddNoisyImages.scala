package week3

import org.mbari.openimaj.{SFloatPixelProcessor, Util}
import org.mbari.openimaj.fn.SupportFunctions

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-01-24T20:26:00
 */
object AddNoisyImages extends App {

  val image = Util.read(Util.IMAGE_LENNA)
  Util.namedWindow("Original", image)

  // --- Add noise
  val fn0 = SupportFunctions.saltAndPepperNoise(0.1)
  val fn1 = (a: Array[Float]) => a.map(fn0)
  val noiseProcessor = new SFloatPixelProcessor(fn1)

  // --- The larger the number of images we add the more like the original it looks
  val n = 30

  val noisyImage0 = image.process(noiseProcessor)
  for (i <- 0 until n) {
    val noisyImageN = image.process(noiseProcessor)
    noisyImage0.addInplace(noisyImageN)

  }

  noisyImage0.divideInplace(n.toFloat)
  Util.namedWindow(s"Added $n noisy images", noisyImage0)

}

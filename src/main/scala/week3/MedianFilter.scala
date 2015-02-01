package week3

import org.mbari
import org.mbari.openimaj.{SFloatPixelProcessor, Util}
import org.mbari.openimaj.fn.SupportFunctions
import org.openimaj.image.processing.algorithm.{FilterSupport, MedianFilter}

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-01-24T10:08:00
 */
object MedianFilter extends App {

  val image = Util.read(Util.IMAGE_LENNA)
  Util.namedWindow("Original", image)

  // --- Add noise
  val fn0 = SupportFunctions.saltAndPepperNoise(0.1)
  val fn1 = (a: Array[Float]) => a.map(fn0)
  val noiseProcessor = new SFloatPixelProcessor(fn1)
  val noisyImage = image.process(noiseProcessor)
  Util.namedWindow("Noisy", noisyImage)

  // --- Median Filter
  val processor = new MedianFilter(FilterSupport.createBlockSupport(3, 3))
  val medfiltImage = noisyImage.process(processor)
  Util.namedWindow("Median Filtered", medfiltImage)

}

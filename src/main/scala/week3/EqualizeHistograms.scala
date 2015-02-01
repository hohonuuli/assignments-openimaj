package week3

import org.mbari.openimaj.Util
import org.openimaj.image.processing.algorithm.EqualisationProcessor

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-01-24T09:57:00
 */
object EqualizeHistograms extends App {

  val image = Util.read(Util.IMAGE_LENNA)
  val processor = new EqualisationProcessor
  val equalizedImage = image.process(processor)

  Util.namedWindow("Original", image)
  Util.namedWindow("Equalized Histogram", equalizedImage)

}

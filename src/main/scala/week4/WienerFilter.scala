package week4

import org.mbari.openimaj.Util
import org.openimaj.image.processing.algorithm.FourierTransform

import scala.collection.JavaConverters._

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-02-01T09:10:00
 */
object WienerFilter extends App {

  val image = Util.read(Util.IMAGE_LENNA)
  Util.namedWindow("Original", image)

  val ffts = image.bands.asScala.map(new FourierTransform(_, false))

}

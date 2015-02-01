package week1

import org.mbari.openimaj.Util
import org.openimaj.image.MBFImage
import org.openimaj.image.processing.transform.{AffineSimulation, AffineParams}
import scala.math._

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-01-08T10:37:00
 */
object RotateImage extends App {

  val image = Util.read(Util.IMAGE_SMALL_TEST)
  Util.namedWindow("Original", image)

//  val affineParams = new AffineParams(toRadians(45).toFloat, 0F)
//  val bands = for (i <- 0 until image.numBands())
//      yield AffineSimulation.transformImage(image.getBand(i), affineParams)
//  val newImage = new MBFImage(bands:_*)
//  Util.namedWindow("Rotated", newImage)
}

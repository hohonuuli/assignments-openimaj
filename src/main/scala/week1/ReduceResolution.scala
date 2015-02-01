package week1

import org.mbari.openimaj.{BlockProcessor, NeighborhoodProcessor, Util}
import java.lang.{Float => JFloat}

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-01-08T11:01:00
 */
object ReduceResolution extends App {

  val image = Util.read(Util.IMAGE_SMALL_TEST)
  image.setPixel(10, 10, Array[JFloat](0F, 0F, 0F))
  Util.namedWindow("Original", image)

  val processor = new BlockProcessor(3, 3,
    (a: Array[Float]) => a.sum / a.size.toFloat)
  val newImage = image.process(processor)
  Util.namedWindow("Averaged", newImage)
}

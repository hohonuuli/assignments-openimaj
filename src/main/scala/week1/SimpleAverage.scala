package week1

import org.mbari.openimaj.{NeighborhoodProcessor, Util}
import java.lang.{Float => JFloat}


object SimpleAverage {
  def main(args: Array[String]) {
    val image = Util.read(Util.IMAGE_SMALL_TEST)
    image.setPixel(10, 10, Array[JFloat](0F, 0F, 0F))
    Util.namedWindow("Original", image)

    val processor = new NeighborhoodProcessor(3, 3, (a: Array[Float]) => a.sum / a.size.toFloat)
    val newImage = image.process(processor)
    Util.namedWindow("Averaged", newImage)

  }
}
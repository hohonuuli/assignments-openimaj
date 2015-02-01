package week1

import java.io.File
import java.net.URL

import org.mbari.openimaj.{Util, SFloatPixelProcessor}
import org.mbari.swing.Swing
import org.openimaj.image.{ImageUtilities, MBFImage}
import scala.math._

/**
 * This demo takes the test images and reduces the number of colors by
 * applying:
 * {{{
 *   floor(pixel / n) * n;
 * }}}
 *
 * In this demo, we use n as a power of 2 between 2 and 256
 */
object ReduceByPower {
  def main(args: Array[String]): Unit = {

    // -- Read image
    val image = ImageUtilities.readMBF(Util.IMAGE_SMALL_TEST)
    
    val rp = new ReduceByPower(image)

    val powers = (1 to 8).map(pow(2, _).toInt)
    for (n <- powers) {
      val reducedImage = rp(n)
      val frame = Swing.namedWindow(s"Divided by ${n.toString}",
        Util.toBufferedImage(reducedImage))
      frame.setSize(400, 400)
    }
  }

}

class ReduceByPower(image: MBFImage) {

  def apply(levels: Int): MBFImage = {
    val n = levels.toDouble
    val cloneImage = image.clone()
    cloneImage.processInplace(new SFloatPixelProcessor(pixel => {
      val u = pixel.map(v => {            // retain ref to u for debugging
        val b = v * 255                   // openimaj uses 0-1 instead of 0-255
        (floor(b / n) * n / 255D).toFloat // apply reduction and convert back to 0-1 range
      })
      u
    }))

    cloneImage
  }

}

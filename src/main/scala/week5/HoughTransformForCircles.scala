package week5

import org.mbari.openimaj.Util
import org.openimaj.image.MBFImage
import org.openimaj.image.analysis.algorithm.HoughCircles
import org.openimaj.image.colour.Transforms
import org.openimaj.image.processing.edges.CannyEdgeDetector
import scala.collection.JavaConverters._

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-02-09T10:06:00
 */
object HoughTransformForCircles extends App {

  val image = Util.read(Util.IMAGE_CIRCLES2)
  Util.namedWindow("Original", image)

  // --- Use and edge detector to just draw circles
  val cannyEdgeDetector = new CannyEdgeDetector()
  val edgeImage = image.process(cannyEdgeDetector)
  Util.namedWindow("Edges", edgeImage)

  // Create and apply a Hough Transform. Convert to grey
  val houghCircles = new HoughCircles(20, image.getWidth, 10, 180)
  val greyscaleImage = Transforms.calculateIntensity(image)
  greyscaleImage.analyseWith(houghCircles)

  val circles = houghCircles.getBest(50).asScala
  println(s"Found ${circles.size} circles")

  val intensityImage = new MBFImage(greyscaleImage, greyscaleImage, greyscaleImage)
  circles.foreach(c => intensityImage.drawShape(c, 4, Array(1f, 0.5f, 0f)))
  Util.namedWindow("Hough Circles", intensityImage)

}

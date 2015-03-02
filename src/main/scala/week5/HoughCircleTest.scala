package week5



import java.util.List

import org.junit.Test
import org.mbari.openimaj.Util
import org.openimaj.image.{MBFImage, FImage}
import org.openimaj.image.analysis.algorithm.HoughCircles
import org.openimaj.image.analysis.algorithm.HoughCircles.WeightedCircle
import org.openimaj.image.processing.edges.CannyEdgeDetector
import org.openimaj.math.geometry.shape.Circle

/**
 * Tests for {@link HoughCircles}
 *
 * @author Sina Samangooei (ss@ecs.soton.ac.uk)
 *
 */
object HoughCirclesTest extends App {

  val imgWidthHeight = 200;

  val circleImage = new FImage(imgWidthHeight, imgWidthHeight)
  val c = new Circle(imgWidthHeight / 2 + 3, imgWidthHeight / 2 + 1, imgWidthHeight / 4)
  circleImage.drawShapeFilled(c, 1f)
  Util.namedWindow("Circle Image", circleImage)

  val det = new CannyEdgeDetector()
  val edgeImage = circleImage.process(det)

  val circ = new HoughCircles(5, imgWidthHeight, 5, 360)
  edgeImage.analyseWith(circ)

  val best = circ.getBest(1)
  val b = best.get(0)

}
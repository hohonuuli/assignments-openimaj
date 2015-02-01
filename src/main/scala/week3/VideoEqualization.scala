package week3

import org.openimaj.image.MBFImage
import org.openimaj.image.processing.algorithm.EqualisationProcessor
import org.openimaj.image.processing.edges.CannyEdgeDetector
import org.openimaj.image.processing.threshold.AdaptiveLocalThresholdContrast
import org.openimaj.video.{VideoDisplayListener, VideoDisplay}
import org.openimaj.video.xuggle.XuggleVideo

/**
 *
 *
 * @author Brian Schlining
 * @since 2015-01-24T20:42:00
 */
object VideoEqualization extends App {

  val video = new XuggleVideo(getClass.getResource("/videos/small.mp4"))

  val display = VideoDisplay.createVideoDisplay(video)

  val processor = new EqualisationProcessor

  display.addVideoListener(new VideoDisplayListener[MBFImage] {
    override def afterUpdate(videoDisplay: VideoDisplay[MBFImage]): Unit = {
    }

    override def beforeUpdate(frame: MBFImage): Unit = {
      frame.processInplace(processor)
      //frame.processInplace(new CannyEdgeDetector())
    }
  })

}

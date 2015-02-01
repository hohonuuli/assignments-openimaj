package org.mbari.swing

import java.awt.image.BufferedImage
import javax.swing.{WindowConstants, JFrame}

/**
 *
 *
 * @author Brian Schlining
 * @since 2014-10-06T16:40:00
 */
object Swing {

  /**
   * Display an image in a named window
   * @param name
   * @param image
   */
  def namedWindow(name: String, image: BufferedImage): JImageFrame = {
    val window = new JImageFrame(image)
    window.setTitle(name)
    window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    window.setVisible(true)
    window
  }

}

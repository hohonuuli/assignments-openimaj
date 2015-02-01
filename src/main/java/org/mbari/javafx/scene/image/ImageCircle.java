package org.mbari.javafx.scene.image;

import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Circle that stores information about a point (pixel location) in an image
 *
 * @author Brian Schlining
 * @since 2014-12-16T15:57:00
 */
public class ImageCircle extends Circle {
    private final Point2D imagePoint;

    public ImageCircle(double radius, Point2D imagePoint) {
        super(radius);
        this.imagePoint = imagePoint;
    }

    public ImageCircle(double radius, Paint fill, Point2D imagePoint) {
        super(radius, fill);
        this.imagePoint = imagePoint;
    }

    public ImageCircle(Point2D imagePoint) {
        this.imagePoint = imagePoint;
    }

    public ImageCircle(double centerX, double centerY, double radius, Point2D imagePoint) {
        super(centerX, centerY, radius);
        this.imagePoint = imagePoint;
    }

    public ImageCircle(double centerX, double centerY, double radius, Paint fill, Point2D imagePoint) {
        super(centerX, centerY, radius, fill);
        this.imagePoint = imagePoint;
    }

    public Point2D getImagePoint() {
        return imagePoint;
    }

}

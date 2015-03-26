package org.mbari.javafx.scene.image;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Utility to display images in their own resizable windows. Each window is assigned a Name/title.
 * You can reuse a window by using the same name. Usage:
 * <pre>
 * JavaFX.namedWindow("My Window", 
 *   new java.net.URL("http://www.mbari.org/staff/brian/images/storage/brian-schlining5_sm.jpg"));
 * </pre>
 *
 * If you need a reference to a window that was created:
 * <pre>
 *  Optional<ImageStage> myWindow = JavaFX.getNamedWindow("My Window");
 * </pre>
 * @author Brian Schlining
 * @since 2014-12-05T13:46:00
 */
public class JavaFX {

    private static int TIMEOUT = 5000;
    private static final Logger log = LoggerFactory.getLogger(JavaFX.class);

    private static Application myApp;

    // Used to launch the application before running any test
    private static final CountDownLatch launchLatch = new CountDownLatch(1);
    private static Map<String, ImageStage> namedWindows = new HashMap<>();

    /**
     * JavaFX requires one and only one Application instance per JVM. This class
     * provides a simple default App to use if you want to use JavaFX as a
     * standalone image dispay utility. Otherwise you can can call
     * setJavaFXApplication(app) to use a different one.
     */
    public static class MyApp extends Application {

        Stage primaryStage;

        @Override
        public void init() throws Exception {
            JavaFX.myApp = this;
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            primaryStage.setTitle(getClass().getSimpleName());
            this.primaryStage = primaryStage;
            launchLatch.countDown();
        }

        @Override
        public void stop() throws Exception {
            JavaFX.myApp = null;
            super.stop();
        }
    }

    /**
     * If you are using this from an already running JavaFX Application. You'll
     * need to set the parent app so that it doesn't try to create one for you.
     *
     * @param app The JavaFX application
     */
    public static void setJavaFXApplication(Application app) {
        myApp = app;
    }

    /**
     * Manages the JavaFX life-cycle. We need to ensure a JavaFX app is running
     * before we try to start opening new Stages.
     */
    private static void startJavaFX() {
        if (myApp == null) {
            new Thread(() -> {
                try {
                    Application.launch(MyApp.class, "");
                }
                catch (Exception e) {
                    log.info("JavaFX did not launch app. A JavaFX app may already be running", e);
                }
            }).start();

            try {
                if (!launchLatch.await(TIMEOUT, TimeUnit.MILLISECONDS)) {
                    throw new RuntimeException("Timeout waiting for Application to launch");
                }
            }
            catch (InterruptedException e) {
                throw new RuntimeException("Timeout waiting for Application to launch");
            }
        }
    }

    /**
     * Opens a resizable window with the given title and image.
     * @param title The title (name) of the window
     * @param image The JavaFX image to display
     */
    public static void namedWindow(String title, Image image) {
        namedWindow(title, image, false);
    }

    /**
     * Opens a resizable window with the given title and image.
     * @param title The title (name) of the window
     * @param image The JavaFX image to display
     * @param showTooltip if true, the current mouse position in image coordinates is displayed
     */
    public static void namedWindow(String title, Image image, boolean showTooltip) {
        startJavaFX();
        runOnJavaFXThread(() -> {

            ImageStage stage;
            if (namedWindows.containsKey(title)) {
                stage = namedWindows.get(title);
            }
            else {
                stage = new ImageStage();
                namedWindows.put(title, stage);
            }

            stage.setTitle(title);
            stage.setImage(image);

            if (showTooltip) {

                BorderPane root =  stage.getRoot();
                final Text text = new Text("100, 100");
                text.setLayoutX(100);
                text.setLayoutX(100);
                text.setFill(Color.WHITE); // WHITE + BlendMode.DIFFERENCE = XOR
                text.setBlendMode(BlendMode.DIFFERENCE);

                root.getChildren().add(text);

                root.setOnMouseMoved(e -> {
                    String msg = "";
                    Point2D scenePoint = new Point2D(e.getSceneX(), e.getSceneY());
                    Point2D imagePoint = stage.convertToImage(scenePoint);
                    msg = String.format("%.1f, %.1f", imagePoint.getX(), imagePoint.getY());

                    text.setLayoutX(e.getX());
                    text.setLayoutY(e.getY());
                    text.setText(msg);
                });

            }

            stage.show();
        });
    }

    /**
     * Opens a resizable window with the given title and image.
     * @param title The title (name) of the window
     * @param url A URL to an image to be displayed
     */
    public static void namedWindow(String title, URL url) {
        namedWindow(title, url, false);
    }

    /**
     * Opens a resizable window with the given title and image.
     * @param title The title (name) of the window
     * @param url A URL to an image to be displayed
     * @param showTooltip if true, the current mouse position in image coordinates is displayed
     */
    public static void namedWindow(String title, URL url, boolean showTooltip) {
        startJavaFX();
        runOnJavaFXThread(() -> {
            try {
                Image image = new Image(url.toExternalForm());
                namedWindow(title, image, showTooltip);
            } catch (Exception e) {
                log.warn("Failed to open " + url.toExternalForm(), e);
            }
        });

    }

    /**
     * Get a handle to a Stage that has already been created. This call does not create
     * a new Stage, it only grabs a reference to ones that have already been created.
     * @param title The named window to find
     * @return If the window exists, the optional will contain a reference to the iamge
     */
    public static Optional<ImageStage> getNamedWindow(String title) {
        return Optional.ofNullable(namedWindows.get(title));
    }

    private static void runOnJavaFXThread(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        }
        else {
            Platform.runLater(r);
        }
    }


    public static void main(String[] args) throws Exception {

        String a = args[0];
        if (a.startsWith("http:")) {
            namedWindow("", new URL(a), true);
        }
        else {
            File f = new File(a);
            namedWindow("", f.toURI().toURL(), true);
        }

    }

}

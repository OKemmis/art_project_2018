package gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.util.Duration;
import java.util.Random;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class Main extends Application {

    // set up background music
    private static final String LOOP_URI = Main.class.getResource("assets/audio_asset_1.wav").toString();
    private AudioClip loop;

    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Weird Line Generator");
        Group root = new Group();
        Scene scene = new Scene(root, 1000, 1000, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        scene.setCursor(Cursor.NONE);

        // set primaryStage boundaries to visible bounds of the main screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());

        // play background music
        loop = new AudioClip(LOOP_URI);
        loop.setCycleCount(AudioClip.INDEFINITE);
        loop.play();

        AtomicInteger leftXPos = new AtomicInteger((int) ((primaryScreenBounds.getWidth()/2)-2));
        AtomicInteger leftYPos = new AtomicInteger((int) (primaryScreenBounds.getHeight()/2));

        AtomicInteger rightXPos = new AtomicInteger((int) ((primaryScreenBounds.getWidth()/2)+2));
        AtomicInteger rightYPos = new AtomicInteger((int) (primaryScreenBounds.getHeight()/2));

        AtomicInteger length = new AtomicInteger(1);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50),
                ae -> {

                    // initialise new line
                    Line leftLine = new Line();
                    Line rightLine = new Line();

                    // get end points of previous lines
                    int lx = leftXPos.get();
                    int ly = leftYPos.get();
                    int rx = rightXPos.get();
                    int ry = rightYPos.get();

                    // set the start points of the new left-hand line
                    leftLine.setStartX(lx);
                    leftLine.setStartY(ly);

                    // set the start points of the new right-hand line
                    rightLine.setStartX(rx);
                    rightLine.setStartY(ry);

                    // set the end points of the new left-hand line (enforcing uniform length)
                    Random rand = new Random();
                    int r = rand.nextInt(2);
                    int p = rand.nextInt(2);
                    if (p == 0)
                        r = -r;
                    int newLx = lx + ((10 * length.get()) * r);
                    leftLine.setEndX(newLx);
                    r = rand.nextInt(2);
                    p = rand.nextInt(2);
                    if (p == 0)
                        r = -r;
                    int newLy = ly + ((10 * length.get()) * r);
                    leftLine.setEndY(newLy);

                    // set the end points of the new right-hand line
                    int newRx = rx + (lx - newLx);
                    int newRy = ry + (ly - newLy);
                    rightLine.setEndX(newRx);
                    rightLine.setEndY(newRy);

                    // randomise and set the colour of the new lines
                    int colourRand = rand.nextInt((3-1) + 1) + 1;
                    if (colourRand == 1) {
                        leftLine.setStroke(Color.BLACK);
                        rightLine.setStroke(Color.BLACK);
                    } else if (colourRand == 2) {
                        leftLine.setStroke(Color.WHITESMOKE);
                        rightLine.setStroke(Color.WHITESMOKE);
                    } else if (colourRand == 3) {
                        leftLine.setStroke(Color.WHITE);
                        rightLine.setStroke(Color.WHITE);
                    }

                    // add the new lines to root
                    root.getChildren().add(leftLine);
                    root.getChildren().add(rightLine);

                    // if one of the lines endpoints lies outside of the visible range, clear the group and re-centre
                    if ((newRx < 0 || newRx > primaryScreenBounds.getWidth()) || (newRy < 0 || newRy > primaryScreenBounds.getHeight()-20)) {
                        root.getChildren().clear();
                        leftXPos.set((int) ((primaryScreenBounds.getWidth()/2)-2));
                        leftYPos.set((int) (primaryScreenBounds.getHeight()/2));
                        rightXPos.set((int) ((primaryScreenBounds.getWidth()/2)+2));
                        rightYPos.set((int) (primaryScreenBounds.getHeight()/2));

                        if (length.get() == 5)
                            length.set(1);
                        else
                            length.set(length.get()+1);

                    } else {
                        // otherwise, update leftXPos, leftYPos, rightXPos, and rightYPos values to reflect changes
                        leftXPos.set(newLx);
                        leftYPos.set(newLy);
                        rightXPos.set(newRx);
                        rightYPos.set(newRy);
                    }

                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        primaryStage.show();
    }
}
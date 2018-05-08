/*package io.github.cshadd.fetch_bot.controllers;

import java.io.ByteArrayInputStream;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.dnn.Net;

public class Camera extends Application
{

	private static final int SCENE_W = 640;
	private static final int SCENE_H = 480;

	VideoCapture videoCapture;

	Canvas canvas;
	GraphicsContext g2d;
	Stage stage;
	AnimationTimer timer;
	Net net = Dnn.readNetFromCaffe("MobileNetSSD_deploy.prototxt.txt", "MobileNetSSD_deploy.caffemodel");
	Mat det;
	Classes arr[] = Classes.values();

	static
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	// The objects that the current Caffe model can detect. Do not change the order (explained in the Detections method)
	public enum Classes
	{
		BACKGROUND, AEROPLANE, BICYCLE, BIRD, BOAT, BOTTLE, BUS, CAR, CAT, CHAIR, COW, DININGTABLE, DOG, HORSE, MOTORBIKE, PERSON, POTTEDPLANT, SHEEP, SOFA, TRAIN, TVMONITOR
	}

	// Starting JavaFX and OpenCV Camera
	@Override
	public void start(Stage stage)
	{

		this.stage = stage;

		initOpenCv();

		canvas = new Canvas(SCENE_W, SCENE_H);
		g2d = canvas.getGraphicsContext2D();
		g2d.setStroke(Color.GREEN);

		Group group = new Group(canvas);

		Scene scene = new Scene(group, SCENE_W, SCENE_H);

		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();

		// Captures a frame of the video, converts it to an OpenCV Matrix, displayed as an image on screen, and finally fed through Detections method,
		// handling the items detected in that specific frame of video
		timer = new AnimationTimer()
		{

			Mat mat = new Mat();

			@Override
			public void handle(long now)
			{

				videoCapture.read(mat);

				Image image = mat2Image(mat);

				g2d.drawImage(image, 0, 0);

				Detections(mat);

			}
		};
		timer.start();

	}

	// Handles the OpenCV Camera and events
	private void initOpenCv()
	{

		videoCapture = new VideoCapture();
		videoCapture.open(0);

		System.out.println("Camera open: " + videoCapture.isOpened());

		stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			public void handle(WindowEvent we)
			{

				timer.stop();
				videoCapture.release();

				System.out.println("Camera released");

			}
		});
	}

	// Converts a matrix captured from the OpenCV camera to an Image to be displayed
	public static Image mat2Image(Mat mat)
	{
		MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".png", mat, buffer);
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}

	// Handles everything to do with object detection
	public void Detections(Mat mat)
	{
		int index;

		// Converts the image matrix into a "blob," which is then fed into the Caffe neural network using net.setInput() and detections are output as a
		// matrix in net.forward()
		Mat blob = Dnn.blobFromImage(mat, .007843, new Size(300, 300), new Scalar(300, 300), false, false);

		net.setInput(blob);

		det = net.forward();

		// For each detection, there are 7 numbers the matrix det outputs. The first is always 0 for some unknown reason, the second is the object it
		// detects (output as the index from the Classes enum (which is why you shouldn't change the order of the classes)), the third is the confidence
		// the network has that the object is what they predicted it was, and the fourth to seventh numbers are the starting x position, starting y
		// position, ending x position, and ending y position of the object respectively. They are represented proportionally to the image, so you must
		// multiply them by the size of the image to get the actual location of the object with respects to the image. This for loop then draws a
		// rectangle around each detection and text identifying each image
		for (int i = 0; i < det.reshape(1, 1).size().width; i += 7)
		{
			index = (int) det.reshape(1, 1).get(0, i + 1)[0];
			double con = det.reshape(1, 1).get(0, i + 2)[0];
			double startX = det.reshape(1, 1).get(0, i + 3)[0] * SCENE_W;
			double startY = det.reshape(1, 1).get(0, i + 4)[0] * SCENE_H;
			double endX = det.reshape(1, 1).get(0, i + 5)[0] * SCENE_W;
			double endY = det.reshape(1, 1).get(0, i + 6)[0] * SCENE_H;

			g2d.strokeRect(startX, startY, endX - startX, endY - startY);
			g2d.strokeText(arr[index].toString() + " " + con + "%", startX, startY);

		}

	}

	public static void main(String[] args)
	{
		launch(args);
	}
}*/
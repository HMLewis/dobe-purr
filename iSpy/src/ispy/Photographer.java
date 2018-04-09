package ispy;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import edu.hendrix.ev3webcam.BooleanImage;
import edu.hendrix.ev3webcam.Webcam;
import edu.hendrix.ev3webcam.YUYVImage;
import edu.hendrix.lmsl.demos.localize1.GyroLocalizer;

public class Photographer {
	public final static double PHOTO_INTERVAL = 30;
	
	private static Key QUIT = Button.ESCAPE,
			PHOTO = Button.DOWN;
	
	private static NXTRegulatedMotor M_LEFT = Motor.A, M_RIGHT = Motor.D;
	private static EV3UltrasonicSensor sonar;
	private static float[] samp;
	
	public void run() {
		try {
			ButtonMover mover = new ButtonMover(M_LEFT, M_RIGHT, 300, 150);
			//GyroLocalizer localizer = new GyroLocalizer(M_LEFT, M_RIGHT, SensorPort.S4);
			sonar=new EV3UltrasonicSensor(SensorPort.S2);
			samp=new float[1];
			YUYVImageList images = new YUYVImageList();
			Webcam.start();
			boolean taking = false;
			while (!QUIT.isDown()) {
				//localizer.update();
				YUYVImage img = YUYVImage.grab();
				BooleanImage bw = new BooleanImage(img);
				
				
				mover.move();
				
				if (PHOTO.isUp()) {
					taking = false;
				}
				//if ((!taking && PHOTO.isDown()) || images.size() == 0 || localizer.getLocation().distanceTo(images.getLastLocation()) > PHOTO_INTERVAL) {
				if ((!taking && PHOTO.isDown()) || images.size() == 0 /*|| Math.abs(localizer.getLocation()-images.getLastLocation()) > PHOTO_INTERVAL*/) {
					//images.add(img, (double)localizer.getLocation());
					sonar.getDistanceMode().fetchSample(samp, 0);
					images.add(img, (double) samp[0]);
					taking = true;
				}
				if (taking) {
					bw.flipAll();
				}
				bw.displayLCD();
				//localizer.update();
			}

			mover.stop();
			double fps = Webcam.end();
			LCD.clear();
			//localizer.close();
			System.out.println(String.format("%5.2f fps", fps));
			if (images.size() > 0) {
				System.out.println("Saving images...");
				YUYVImageListStorage.getEV3Storage().save(images);
				System.out.println(images.size() + " images saved.");
			}
			while (!Button.ESCAPE.isDown());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Driver exception: " + ioe.getMessage());
		}
	}	
	
	public static void main(String[] args) {
		new Photographer().run();
	}
}

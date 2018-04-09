package trash;

import ispy.ImageMatch;
import ispy.ImageMatcher;
import ispy.Photographer;
import ispy.YUYVImageList;
import ispy.YUYVImageListStorage;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import edu.hendrix.ev3webcam.Webcam;
import edu.hendrix.ev3webcam.YUYVImage;
import edu.hendrix.img.IntImage;
import edu.hendrix.lmsl.demos.localize1.GyroLocalizer;
import edu.hendrix.lmsl.storage.Chooser;

abstract public class Trainer<T,M extends ImageMatcher<T>> {
	
	public Trainer() {
		this(Photographer.PHOTO_INTERVAL);
	}
	
	public Trainer(double checkDistance) {
	}
	
	public double getImageShare(double imgDist) {
		// Place your solution here
		// This version simply replaces the original location
		// with that given for the winning image.eturn .25;
		return 1.0;
	}
	
	abstract public M makeMatcherFrom(YUYVImageList images);
	
	public void msg(String msg) {
		LCD.drawString(msg, 8, 2);
	}
	
	public void ok() {
		msg("OK!       ");
	}
	
	public void run() {
		//ButtonMover mover = new ButtonMover(Motor.A, Motor.D, 300, 150);
		YUYVImageListStorage storage = YUYVImageListStorage.getEV3Storage();
		Chooser<YUYVImageList,YUYVImageListStorage> chooser = new Chooser<YUYVImageList,YUYVImageListStorage>();
		chooser.choose(storage);
		if (chooser.isSelected()) {
			LCD.clear();
			LCD.drawString("Processing image", 0, 0);
			LCD.drawString("database...", 0, 1);
			YUYVImageList il=chooser.getSelected();
			M matcher = makeMatcherFrom(il);
			LCD.clear();
			LCD.drawString("Starting webcam...", 0, 0);
			String picked = chooser.getSelectedFilename();
			try {
				Webcam.start();
				LCD.clear();
				LCD.drawString("Good pictures", 0, 7);
				while (!Button.ESCAPE.isDown() && !Button.ENTER.isDown()) {
					YUYVImage img = YUYVImage.grab();
					msg("Imaging...");
					ImageMatch now = matcher.getBestMatch(img);
					LCD.drawString(String.format("%7.2f    ", now.getDistance()), 0, 6);
					IntImage match4 = IntImage.toShrunkenGrayInts(now.getImage(), 4);
					match4.displayLCD(0, LCD.SCREEN_HEIGHT / 2);
					ok();
					IntImage img4 = IntImage.toShrunkenGrayInts(img, 4);
					img4.displayLCD(0, 0);
					if (Button.DOWN.isDown()) {
						il.addGood(now.getDistance());
					}
				}
				LCD.drawString("Bad pictures ", 0, 7);
				/*while (!Button.ESCAPE.isDown() && !Button.ENTER.isDown()) {
					YUYVImage img = YUYVImage.grab();
					msg("Imaging...");
					ImageMatch now = matcher.getBestMatch(img);
					LCD.drawString(String.format("%7.2f    ", now.getDistance()), 0, 6);
					IntImage match4 = IntImage.toShrunkenGrayInts(now.getImage(), 4);
					match4.displayLCD(0, LCD.SCREEN_HEIGHT / 2);
					ok();
					IntImage img4 = IntImage.toShrunkenGrayInts(img, 4);
					img4.displayLCD(0, 0);
					if (Button.DOWN.isDown()) {
						il.addBad(now.getDistance());
					}
				}*/
				msg("Closing...");
				Webcam.end();
				double fps = Webcam.end();
				LCD.clear();
				System.out.println(String.format("%5.2f fps", fps));
				System.out.println("Saving images...");
				
				YUYVImageListStorage.getEV3Storage().save(il);
				System.out.println("\nimages saved.");
				while (!Button.ESCAPE.isDown());

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Driver exception: " + e.getMessage());
			} finally {
				ok();
			}
		}
	}
}

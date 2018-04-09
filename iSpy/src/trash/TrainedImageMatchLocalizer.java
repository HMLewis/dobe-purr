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
import edu.hendrix.ev3webcam.Webcam;
import edu.hendrix.ev3webcam.YUYVImage;
import edu.hendrix.img.IntImage;
import edu.hendrix.lmsl.storage.Chooser;

abstract public class TrainedImageMatchLocalizer<T,M extends ImageMatcher<T>> {
	private double baseline;
	
	public TrainedImageMatchLocalizer() {
		this(Photographer.PHOTO_INTERVAL);
	}
	
	public TrainedImageMatchLocalizer(double checkDistance) {
		//this.checkDistance = checkDistance;
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
		//Chooser<YUYVImageList,YUYVImageListStorage> chooser = new Chooser<YUYVImageList,YUYVImageListStorage>();
		chooser.choose(storage);
		if (chooser.isSelected()) {
			LCD.clear();
			LCD.drawString("Processing image", 0, 0);
			LCD.drawString("database...", 0, 1);
			YUYVImageList il=chooser.getSelected();
			M matcher = makeMatcherFrom(chooser.getSelected());
			LCD.clear();
			LCD.drawString("Starting webcam...", 0, 0);
			baseline=il.getBaseline();
			try {
				Webcam.start();
				LCD.clear();
				while (!Button.ESCAPE.isDown()) {
					//ButtonMover mover = new ButtonMover(Motor.A, Motor.D, 300, 150);
					YUYVImage img = YUYVImage.grab();
					msg("Imaging...");
					ImageMatch now = matcher.getBestMatch(img);
					LCD.drawString(String.format("%7.2f    ", now.getDistance()), 0, 6);
					if (now.getDistance()<baseline*1.1) {
						Sound.beep();
					}
					IntImage match4 = IntImage.toShrunkenGrayInts(now.getImage(), 4);
					match4.displayLCD(0, LCD.SCREEN_HEIGHT / 2);
					ok();
					IntImage img4 = IntImage.toShrunkenGrayInts(img, 4);
					img4.displayLCD(0, 0);
				}
				msg("Closing...");
				Webcam.end();

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Driver exception: " + e.getMessage());
			} finally {
				ok();
			}
		}
	}
}

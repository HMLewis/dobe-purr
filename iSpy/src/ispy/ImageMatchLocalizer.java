package ispy;

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

abstract public class ImageMatchLocalizer<T,M extends ImageMatcher<T>> {
	private double checkDistance;
	private double baseLine;
	private double threshold = 50;
	
	public ImageMatchLocalizer() {
		this(Photographer.PHOTO_INTERVAL);
	}
	
	public ImageMatchLocalizer(double checkDistance) {
		this.checkDistance = checkDistance;
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
			M matcher = makeMatcherFrom(chooser.getSelected());
			LCD.clear();
			LCD.drawString("Starting webcam...", 0, 0);
			
			//set baseline according to goal object.
			String picked = chooser.getSelectedFilename();
			if (picked.contentEquals("13")){baseLine = 750;} //blue
			if (picked.contentEquals("14")){baseLine = 750;} //red
			if (picked.contentEquals("15")){baseLine = 550;} //tennis ball
			if (picked.contentEquals("19")){baseLine = 430;} //tissue box
			
			//GyroLocalizer localizer = new GyroLocalizer(Motor.A, Motor.D, SensorPort.S4);
			double prev = (float) 0.0;
			try {
				Webcam.start();
				LCD.clear();
				while (!Button.ESCAPE.isDown()) {
					ButtonMover mover = new ButtonMover(Motor.A, Motor.D, 300, 150);
					//localizer.update();
					YUYVImage img = YUYVImage.grab();
					//double at = localizer.getLocation();
					prev= 0.0;
					//if (at.distanceTo(prev) > checkDistance) {
					//if (checkDistance < Math.abs(at-prev)) {
						//mover.stop();
						msg("Imaging...");
						ImageMatch now = matcher.getBestMatch(img);
						LCD.drawString(String.format("%7.2f    ", now.getDistance()), 0, 6);
//						if (Math.abs(now.getDistance() - baseLine)<threshold) {
//							Sound.beep();
//						}
						//LCD.drawString(String.format("%3.2f    %3.2f", localizer.getD(), now.getLocation()), 0, 7);
						IntImage match4 = IntImage.toShrunkenGrayInts(now.getImage(), 4);
						match4.displayLCD(0, LCD.SCREEN_HEIGHT / 2);
						//localizer.reset(at.partialMeanWith(prev, getImageShare(now.getDistance())));
						//localizer.reset(0);
						//prev = localizer.getLocation();
						ok();
					//} else 
					if (Button.DOWN.isDown()) {
						msg("Reset...");
						mover.stop();
						//localizer.totalReset();
						ok();
					} else {
						mover.move();
					}
					IntImage img4 = IntImage.toShrunkenGrayInts(img, 4);
					img4.displayLCD(0, 0);
					//localizer.getLocation().display(7);
				}
				msg("Closing...");
				Webcam.end();

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Driver exception: " + e.getMessage());
			} finally {
				//localizer.close();
				ok();
			}
		}
	}
}

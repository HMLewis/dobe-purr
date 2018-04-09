package ispy;

import java.awt.image.BufferedImage;
import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import edu.hendrix.ev3webcam.Webcam;
import edu.hendrix.ev3webcam.YUYVImage;
import edu.hendrix.img.IntImage;
import edu.hendrix.lmsl.storage.Chooser;


abstract public class RGBImageMatchLocalizer<T,M extends ImageMatcher<T>> {
	private double checkDistance;
	private double baseLine;
	private double threshold = 50;
	
	public RGBImageMatchLocalizer() {
		this(Photographer.PHOTO_INTERVAL);
	}
	
	public RGBImageMatchLocalizer(double checkDistance) {
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
			if (picked.contentEquals("1")){baseLine = 750;} //blue
			if (picked.contentEquals("2")){baseLine = 750;} //red
			if (picked.contentEquals("3")){baseLine = 550;} //tennis ball
			if (picked.contentEquals("4")){baseLine = 430;} //tissue box
			else{baseLine = 500;}
			//double prev = (float) 0.0;
			try {
				Webcam.start();
				LCD.clear();
				while (!Button.ESCAPE.isDown()) {
					ButtonMover mover = new ButtonMover(Motor.A, Motor.D, 300, 150);
					YUYVImage img = YUYVImage.grab();
					//prev= 0.0;
					//if (at.distanceTo(prev) > checkDistance) {
					//if (checkDistance < Math.abs(at-prev)) {
						//mover.stop();
						msg("Imaging...");
						ImageMatch now = matcher.getBestMatch(img);
						LCD.drawString(String.format("%7.2f    ", now.getDistance()), 0, 6);
						if (now.getDistance() < baseLine + threshold) {
							if (colorCheck(now.getImage(), img)){
								Sound.beep();
							}
							Sound.beep();
						}
						IntImage match4 = IntImage.toShrunkenGrayInts(now.getImage(), 4);
						match4.displayLCD(0, LCD.SCREEN_HEIGHT / 2);
						//localizer.reset(at.partialMeanWith(prev, getImageShare(now.getDistance())));
						//prev = localizer.getLocation();
						ok();
					//} else 
					if (Button.DOWN.isDown()) {
						msg("Reset...");
						mover.stop();
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
				ok();
			}
		}
	}
	
	private boolean colorCheck(YUYVImage bestImg, YUYVImage choiceImg){
		
		BufferedImage bestBuff = bestImg.toBufferedImage();
		BufferedImage choiceBuff = choiceImg.toBufferedImage();
		
		//imgSize = 160 x 120 pix
		//bins: 0-15, 16-31, 32-47, 48-63, 64-79, 80-95, 96-111,..., 240-255
		int[] bestRedHist = new int[16];
		int[] bestGreenHist = new int[16];
		int[] bestBlueHist = new int[16];
		int[] choiceRedHist = new int[16];
		int[] choiceGreenHist = new int[16];
		int[] choiceBlueHist = new int[16];
		
		for (int i = 0; i<=15; i++){
			bestRedHist[i] = 0;
			bestGreenHist[i] = 0;
			bestBlueHist[i] = 0;
			choiceRedHist[i] = 0;
			choiceGreenHist[i] = 0;
			choiceBlueHist[i] = 0;
		}
		
		for(int x=0; x<bestBuff.getWidth(); x++){
			for(int y=0; y<bestBuff.getHeight(); y++){
				
				int bestRGB = bestBuff.getRGB(x, y);
				int r = (bestRGB & 0x00F00000)  >>> 20;
				int g = (bestRGB & 0x0000F000)  >>> 12;
				int b = (bestRGB & 0x000000F0)  >>> 4;

				for (int i=0; i<=15; i++){
					if (r == i){bestRedHist[i]++;}
					if (g == i){bestGreenHist[i]++;}
					if (b == i){bestBlueHist[i]++;}
				}
			}
		}
		
		for (int x = 0; x<choiceBuff.getWidth(); x++){
			for (int y = 0; y<choiceBuff.getHeight(); y++){
				
				int choiceRGB = choiceBuff.getRGB(x, y);
				int r = (choiceRGB & 0x00F00000)  >>> 20;
				int g = (choiceRGB & 0x0000F000)  >>> 12;
				int b = (choiceRGB & 0x000000F0)  >>> 4;
				
				for (int i=0; i<=15; i++){
					if (r == i){choiceRedHist[i]++;}
					if (g == i){choiceGreenHist[i]++;}
					if (b == i){choiceBlueHist[i]++;}
				}
			}
		}
				
		int rThresh = 30000;
		int gThresh = 30000;
		int bThresh = 30000;

		int rDiff = 0;
		int gDiff = 0;
		int bDiff = 0;
		
		for (int i=0; i<=15; i++){
			rDiff += Math.abs(bestRedHist[i] - choiceRedHist[i]);
			gDiff += Math.abs(bestGreenHist[i] - choiceGreenHist[i]);
			bDiff += Math.abs(bestBlueHist[i] - choiceBlueHist[i]);
		}
		LCD.drawString(Double.toString(rDiff), 0, 7);

		if (rDiff < rThresh){
			if (gDiff < gThresh){
				if (bDiff < bThresh){
					return true;
		}}}
		return false;
	}
}


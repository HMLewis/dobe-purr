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

abstract public class SplitImageMatchLocalizer<T,M extends ImageMatcher<T>> {
	private double baseline;
	
	public SplitImageMatchLocalizer() {
		this(Photographer.PHOTO_INTERVAL);
	}
	
	public SplitImageMatchLocalizer (double checkDistance) {
	}
	
	public double getImageShare(double imgDist) {
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
			//if (picked.contentEquals("13")){baseline = 750;} //blue
			//if (picked.contentEquals("14")){baseline = 750;} //red
			//if (picked.contentEquals("15")){baseline = 550;} //tennis ball
			//if (picked.contentEquals("19")){baseline = 430;} //tissue box
			//GyroLocalizer localizer = new GyroLocalizer(Motor.A, Motor.D, SensorPort.S4);
			try {
				Webcam.start();
				LCD.clear();
				while (!Button.ESCAPE.isDown()) {
					ButtonMover mover = new ButtonMover(Motor.A, Motor.D, 300, 150);
					//localizer.update();
					int num_pics=5;
					YUYVImage img=YUYVImage.grab();
					YUYVImage[] imgs=new YUYVImage[5];
					imgs[0]=img;
					imgs[1]=img.makeSection(img, 0, 0, (int)Math.floor(img.getWidth()*.75), (int)Math.floor(img.getHeight()*.75));
					imgs[2]=img.makeSection(img, (int)Math.floor(img.getWidth()*.25), 0, img.getWidth(), (int)Math.floor(img.getHeight()*.75));
					imgs[3]=img.makeSection(img, 0, (int)Math.floor(img.getHeight()*.75), (int)Math.floor(img.getWidth()*.75), img.getHeight());
					imgs[4]=img.makeSection(img, (int)Math.floor(img.getWidth()*.25), (int)Math.floor(img.getHeight()*.75), img.getWidth(), img.getHeight());
					msg("Imaging...");
					ImageMatch bestImg = matcher.getBestMatch(imgs[0]);
					ImageMatch now=bestImg;
					int choice=0;
					double best=bestImg.getDistance();
					for (int i=1; i<num_pics; i++) {
						now = matcher.getBestMatch(imgs[i]);
						if (now.getDistance()< best) {
							best=now.getDistance();
							bestImg=now;
							choice=i;
						}
					}
					LCD.drawString(String.format("%7.2f    ", best), 0, 6);
					if (best<baseline*1.1) {
						Sound.beep();
					}
					drawLoc(choice);
					IntImage match4 = IntImage.toShrunkenGrayInts(bestImg.getImage(), 4);
					match4.displayLCD(0, LCD.SCREEN_HEIGHT / 2);
					//localizer.reset(0);
					ok();
					mover.move();
					IntImage img4 = IntImage.toShrunkenGrayInts(imgs[choice], 4);
					img4.displayLCD(0, 0);
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
	private void drawLoc(int c) {
		if (c==0) {LCD.drawString("Whole",0,7);}
		else if (c==1) {LCD.drawString("NW   ",0,7);}
		else if (c==2) {LCD.drawString("NE   ",0,7);}
		else if (c==3) {LCD.drawString("SW   ",0,7);}
		else if (c==4) {LCD.drawString("SE   ",0,7);}
	}
}

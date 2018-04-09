package flutebot;
import java.io.File;
import lejos.hardware.Button;
import lejos.hardware.Sound;

public class FluteBot {
	public static File f=new File("C:/Users/Collin/Desktop/GoldDigger.mp3");
	public static void main(String[] args) {
		while (!Button.ESCAPE.isDown()) {
			Sound.playSample(f);
		}
	}
}

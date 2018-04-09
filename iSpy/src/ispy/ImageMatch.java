package ispy;

import edu.hendrix.ev3webcam.YUYVImage;

public class ImageMatch {
	private YUYVImage img;
	private double where;
	private double similarity;
	
	public ImageMatch(YUYVImage img, double where, double similarity) {
		this.img = img;
		this.where = where;
		this.similarity = similarity;
	}
	
	public YUYVImage getImage() {return img;}
	
	public double getDistance() {return similarity;}
	
	public double getLocation() {return where;}
}

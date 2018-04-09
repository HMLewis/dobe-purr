package ispy;

import edu.hendrix.ev3webcam.YUYVImage;

abstract public class ImageData<T> {
	private YUYVImage src;
	private double where;
	private T processed;
	
	public ImageData(YUYVImage src, double where) {
		this.src = src;
		this.where = where;
		processed = process(src);
	}
	
	public double distanceTo(YUYVImage other) {
		return distanceTo(processed, process(other));
	}
	
	public YUYVImage getSourceImage() {return src;}
	
	public double getDistance() {return where;}
	
	abstract public T process(YUYVImage src);
	
	abstract protected double distanceTo(T srcProc, T otherProc);
}

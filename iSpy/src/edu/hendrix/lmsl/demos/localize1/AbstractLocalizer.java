package edu.hendrix.lmsl.demos.localize1;

import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

abstract public class AbstractLocalizer {
	//private NXTRegulatedMotor left, right;
	private EV3UltrasonicSensor front;
	private float [] samp;
	//private int leftCount, rightCount;
	private double d;//x;, y, theta;
	private double changeLeft, changeRight;
	
	public final static double RADIUS = 2.36, BASE = 12.1;
	
	public static double counts2cm(int counts) {
		return RADIUS * Math.toRadians(counts);
	}
	
	public AbstractLocalizer(NXTRegulatedMotor left, NXTRegulatedMotor right) {
		//this.left = left;
		//this.right = right;
		front=new EV3UltrasonicSensor(SensorPort.S2);
		samp=new float [1];
	}
	
	public void update() {
		/*int leftUpdate = left.getTachoCount();
		int rightUpdate = right.getTachoCount();
		changeLeft = counts2cm(leftUpdate - leftCount);
		changeRight = counts2cm(rightUpdate - rightCount);
		leftCount = leftUpdate;
		rightCount = rightUpdate;
		double r = (changeLeft + changeRight) / 2;
		theta = getUpdatedTheta();
		x += r * Math.cos(theta);
		y += r * Math.sin(theta);*/
		front.getDistanceMode().fetchSample(samp, 0);
		d=(double) samp[0];
	}
	
	public void reset(float fromElsewhere) {
		d=0;
	}
	
	abstract protected double getUpdatedTheta();
	
	public double getLeftChange() {return changeLeft;}
	public double getRightChange() {return changeRight;}
	
	public double getD() {return d;}
	/*public double getY() {return y;}
	public double getTheta() {return theta;}*/
	
	public double getLocation() {
		return d;
	}
	
	public String toString() {
		return String.format("(%f)", d);
		//return String.format("(%6.2f,%6.2f,%6.2f)", x, y, theta);
	}
}

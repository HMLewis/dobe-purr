package wander;

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;

public class Wall {
	public static void main(String args[]) throws InterruptedException {
		EV3UltrasonicSensor left=new EV3UltrasonicSensor(SensorPort.S3);
		EV3UltrasonicSensor front=new EV3UltrasonicSensor(SensorPort.S2);
		EV3GyroSensor gyro=new EV3GyroSensor(SensorPort.S4);
		EV3UltrasonicSensor right=new EV3UltrasonicSensor(SensorPort.S1);
		float rsample[]=new float [1];
		float lsample[]=new float [1];
		float fsample[]=new float [1];
		while(!Button.ESCAPE.isDown()) {
			if (check()) {
				go(gyro,fsample,lsample, rsample, left, right,front);
				break;
			}
			update(left, lsample, right, rsample, front, fsample);
			if (isBlocked(fsample, lsample)) {turn(gyro,fsample,lsample, rsample, left, right,front);}
			else if (noWall(lsample)) {wallTurn();}
			//else if (lsample[0]>.35) {forward();}
			else {forward();}
		}
		stop();
	}
	private static void go(EV3GyroSensor gyro, float[] fsample, float[] lsample, float[] rsample, EV3UltrasonicSensor left, EV3UltrasonicSensor right, EV3UltrasonicSensor front) {
		//robot drives to target
		update(left, lsample, right, rsample, front, fsample);
		while (fsample[0]>.05) {
			forward();
		}
		stop();
	}
	private static boolean check() {
		//robot checks for target
		return false;	
	}
	private static void update(EV3UltrasonicSensor left, float[] lsample, EV3UltrasonicSensor right, float[] rsample, EV3UltrasonicSensor front, float[] fsample) {
		left.getDistanceMode().fetchSample(lsample, 0);
		right.getDistanceMode().fetchSample(rsample, 0);
		front.getDistanceMode().fetchSample(fsample, 0);	
	}
	private static void wallTurn() {
		Motor.A.setSpeed(50);
		Motor.D.setSpeed(250);
		Motor.A.forward();
		Motor.D.forward();
		check();
	}
	private static boolean noWall(float[] sample) {
		if (sample[0]>1.0) {return true;}
		return false;
	}
	public static void turn(EV3GyroSensor gy, float[] fsample, float[] lsample, float[] rsample, EV3UltrasonicSensor left, EV3UltrasonicSensor right, EV3UltrasonicSensor front) throws InterruptedException {
		double angle;
		double init=updateGyro(gy);//init is somehow positive?
		gy.reset();
		double buf=.3;
		update(left, lsample, right, rsample, front, fsample);
		float y=fsample[0];
		angle=updateGyro(gy);
		/*while (angle>=init+buf) {
			LCD.drawString("Loop 1", 0, 0);
			turnMotor();
			angle=updateGyro(gy);
		}*/
		//while (angle>init-buf || angle<init+buf) {
		while (angle<2*Math.PI) {
			turnMotor();
			check();
			LCD.drawString("Loop 2", 0, 0);
			LCD.drawString("init"+(init), 0, 1);
			LCD.drawString("angle "+angle,0,2);
			angle=updateGyro(gy);
			if (Button.ESCAPE.isDown()) {break;}
		}
		turnMotor();
		update(left, lsample, right, rsample, front, fsample);
		while (lsample[0]>y) {
			turnMotor();
			LCD.drawString("Loop 3", 0, 0);
			LCD.drawString("fsamp "+fsample[0]+" y "+y+"\nlsamp "+lsample[0], 0, 3);
			update(left, lsample, right, rsample, front, fsample);
			check();
			if (Button.ESCAPE.isDown()) {break;}
		}
		turnMotor();
		Thread.sleep(200);
	}
	public static double updateGyro(EV3GyroSensor gy) {
		float[] gsample=new float [1];
		gy.getAngleMode().fetchSample(gsample, 0);
		double angle = Math.toRadians(gsample[0]);
		return (angle % (2*Math.PI));
	}
	public static boolean isBlocked(float[] fsample, float[] lsample) {
		if (fsample[0]<=lsample[0]) {return true;}
		return false;
	}
	public static void forward() {
		Motor.A.setSpeed(250);
		Motor.D.setSpeed(250);
		Motor.A.forward();
		Motor.D.forward();
	}
	public static void turnMotor() {
		Motor.A.setSpeed(50);
		Motor.D.setSpeed(50);
		Motor.A.forward();
		Motor.D.backward();
	}
	private static void stop() {
		Motor.A.stop(true);
		Motor.B.stop();	
	}
}
/*private static void turn(float[] fsample, float[] lsample, float[] rsample, EV3UltrasonicSensor left, EV3UltrasonicSensor right, EV3UltrasonicSensor front) {
float x=lsample[0];
float y=fsample[0];
Motor.A.setSpeed(50);
Motor.D.setSpeed(50);
Motor.A.forward();
Motor.D.backward();
update(left, lsample, right, rsample, front, fsample);
while (fsample[0]!=y && lsample[0]!=x) {
	Motor.A.forward();
	Motor.D.backward();
	System.out.println("fsamp "+fsample[0]+" y "+y+"\nlsamp "+lsample[0]+" x "+x);
	update(left, lsample, right, rsample, front, fsample);
	check();
}
Motor.A.forward();
Motor.D.backward();
update(left, lsample, right, rsample, front, fsample);
while (lsample[0]!=x) {
	Motor.D.backward();
	Motor.A.forward();
	System.out.println("fsamp "+fsample[0]+" y "+y+"\nlsamp "+lsample[0]+" x "+x);
	update(left, lsample, right, rsample, front, fsample);
	check();
}
}
public static void turn2 (EV3GyroSensor gy){
double angle;
double init=updateGyro(gy);
Motor.A.setSpeed(50);
Motor.D.setSpeed(50);
angle=updateGyro(gy);
while (init==angle) {//1
	Motor.A.forward();
	Motor.D.backward();
	angle=updateGyro(gy);
}
double buf=.05;
while (angle>init+buf || angle<init-buf) {//2
	Motor.A.forward();
	Motor.D.backward();
	check();
	LCD.drawString("1", 0, 0);
	LCD.drawString("init"+(init-(Math.PI/2)), 0, 1);
	LCD.drawString("angle"+angle,0,2);
	angle=updateGyro(gy);
	if (Button.ESCAPE.isDown()) {break;}
}
while (angle>(init-(Math.PI/2)+buf) % (Math.PI*2) || angle<(init-(Math.PI/2)-buf) % (Math.PI*2)) {//3
	Motor.A.forward();
	Motor.D.backward();
	check();
	angle=updateGyro(gy);
	LCD.drawString("2", 0, 0);
	LCD.drawString("init"+(init-(Math.PI/2)), 0, 1);
	LCD.drawString("angle"+angle,0,2);
	if (Button.ESCAPE.isDown()) {break;}
}
}*/
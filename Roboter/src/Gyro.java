import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.utility.Delay;

public class Gyro {
	EV3GyroSensor gyroSensor;
	float[] gyroSample;
	
	public Gyro() {
		
		gyroSensor = new EV3GyroSensor(SensorPort.S2);
		gyroSample = new float[gyroSensor.sampleSize()];
		gyroSensor.setCurrentMode("Angle");
		
	}
	
	public float getgyro() {
		
		gyroSensor.getAngleMode();
		gyroSensor.fetchSample(gyroSample, 0);
		return gyroSample[0];
		
	}
}

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class Distance {
	EV3UltrasonicSensor distanceSensor;
	float[] distanceSample;
	
	public Distance(){
		
		distanceSensor = new EV3UltrasonicSensor(SensorPort.S1);
		distanceSensor.enable();
		distanceSample = new float[distanceSensor.sampleSize()];
	}
	
	public float getdistance() {
		
		distanceSensor.fetchSample(distanceSample, 0); 
		return distanceSample[0];
		
	}
	
}

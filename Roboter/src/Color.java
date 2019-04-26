import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

public class Color {
	EV3ColorSensor colorSensor;
	float[] colorSample;
	
	public Color() {
	
		 colorSensor = new EV3ColorSensor(SensorPort.S3);
		 colorSensor.setCurrentMode("Red");
		 colorSample = new float[colorSensor.sampleSize()]; 
	}
	
	public float brightness() {
		
		colorSensor.fetchSample(colorSample, 0);
		return colorSample[0];
		
	}

}

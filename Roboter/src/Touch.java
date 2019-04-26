import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Touch {
	public static void main(String[] args) {
		
		
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.C, 56).offset(-52);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.B, 56).offset(52);

		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot pilot = new MovePilot(chassis);
				
		while (true) {
		
			while (sample[0] == 1.0) {
				pilot.forward();
			}
		
		
		if ( Button.ESCAPE.isDown() ) {
		break;
		}
		
		}
		
		touch.close();
	
		
		
	}
}

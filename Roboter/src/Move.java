import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Move {
	Wheel wheel1 = WheeledChassis.modelWheel(Motor.C, 56).offset(-52);
	Wheel wheel2 = WheeledChassis.modelWheel(Motor.B, 56).offset(52);
	MovePilot pilot;

	public Move() {

		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);

	}
	
	public MovePilot getit() {
		return pilot;
		
	}
	

}

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class gyro_test {

	static Gyro test = new Gyro();
	static UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.B);
	static UnregulatedMotor motorC = new UnregulatedMotor(MotorPort.C);

	public static void main(String[] args) {

		while (Button.ESCAPE.isUp()) {

			float leckmichamarsch = test.getgyro();
			System.out.println(leckmichamarsch);
		}

	}

}

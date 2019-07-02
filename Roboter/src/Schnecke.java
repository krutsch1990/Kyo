import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;

public class Schnecke {

	static UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.B);
	static UnregulatedMotor motorC = new UnregulatedMotor(MotorPort.C);

	public static void main(String[] args) {

		int flex = 10;

		while (Button.ESCAPE.isUp()) {

			long now = System.currentTimeMillis();
			long loopend = now + 3000 ;

			while (System.currentTimeMillis() < loopend) {

				motorC.setPower(40);
				motorB.setPower(flex);


			}
			if (flex < 30) {
			++flex;
			}
			System.out.println(flex);
		}

	}
}

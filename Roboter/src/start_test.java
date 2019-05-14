import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;

public class start_test {

	static UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.B);
	static UnregulatedMotor motorC = new UnregulatedMotor(MotorPort.C);

	public static void main(String[] args) {

		Color color = new Color();
		Distance distance = new Distance();
		System.out.println("Press any Button");
		Sound.beepSequence();
		Button.waitForAnyPress();

		float tisch = color.getbrightness();
		Sound.beepSequence();
		Button.waitForAnyPress();
		float linie = color.getbrightness();
		Sound.beepSequence();
		float mid = (tisch + linie) / 2;
		
		System.out.println("Tisch:  " + tisch);
		System.out.println("Linie:  " + linie);
		System.out.println("Mitte:  " + mid);
		
		Button.waitForAnyPress();


		float[] error_list = new float [8];
		int count=0;
		
		int speed_max = 25;

		
		
		
		int speed_int_right;
		int speed_int_left;

		while (Button.ESCAPE.isUp()) {
		


			float actually = color.getbrightness();

			if (actually < mid) {

				float basis = linie;
				float max_error = mid - linie;
				float faktor = (actually - basis) / max_error;
				float error = (max_error - (actually - max_error)) / max_error;
				System.out.println("LINKS" + error);
					float speed = faktor * speed_max;
					speed_int_right = (int) speed;

					motorB.setPower(speed_max);
				motorC.setPower(speed_int_right);

			} else if (actually > mid) {

				float basis = mid;
				float max_error = tisch - mid;
				float faktor = (actually - basis) / max_error;
				float error = 1 - ((max_error - (actually - max_error)) / max_error);
				System.out.println("RECHTS" + error);
				
					float speed = speed_max - (faktor * speed_max);
					speed_int_left = (int) speed;

				motorC.setPower(speed_max);
				motorB.setPower(speed_int_left);

			} else {
				motorC.setPower(speed_max);
				motorB.setPower(speed_max);
			}
		++count;
		}

		motorB.stop();
		motorC.stop();
		Sound.beepSequence();

	}
}

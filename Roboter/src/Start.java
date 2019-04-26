import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;

public class Start {

	static UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.B);
	static UnregulatedMotor motorC = new UnregulatedMotor(MotorPort.C);
//	static TouchSensor touch= new TouchSensor(SensorPort.S1);

	public static void main(String[] args) {

		Color color = new Color();

		Sound.beepSequence();
		System.out.println("Press any button");
		Button.waitForAnyPress();
		float tisch = color.brightness();
		Sound.beepSequence();
		Button.waitForAnyPress();
		float linie = color.brightness();
		Sound.beepSequence();

		int speed_max = 25;
		float mid = (tisch + linie) / 2;

		System.out.println(tisch);
		System.out.println(linie);
		System.out.println(mid);

		Button.waitForAnyPress();
		System.out.println("i am driving");

		float error_count = 0;
		float error_sum = 0;
		float error_average = 0;

		while (Button.ESCAPE.isUp()) {
			++error_count;

			if (error_count > 1000) {
				error_count = 0;
				error_sum = 0;
			}

			// System.out.println( color.brightness() );
			System.out.println(error_average);
			float actually = color.brightness();

			if (actually < mid) {

				int speed_int_right;

				float min_error = linie;
				float max_error = mid - linie;

				float actually_error = (actually - min_error) / max_error;
				error_sum += actually_error;
				error_average = error_sum / error_count;

				float error_check = error_average / actually_error;

				if (error_check > 2) {
					speed_int_right = -15;
				} else {

					float speed = actually_error * speed_max;
					speed_int_right = (int) speed;
				}

				motorB.setPower(speed_max);
				motorC.setPower(speed_int_right);

			} else if (actually > mid) {
				int speed_int_left;

				float min_error = mid;
				float max_error = tisch - mid;

				float actually_error = (actually - min_error) / max_error;
				error_sum += actually_error;
				error_average = error_sum / error_count;

				float error_check = error_average / actually_error;

				if (error_check < 2) {
					speed_int_left = -15;
				} else {
					float speed = speed_max - (actually * speed_max);
					speed_int_left = (int) speed;
				}

				motorC.setPower(speed_max);
				motorB.setPower(speed_int_left);
			} else {
				motorC.setPower(speed_max);
				motorB.setPower(speed_max);
			}

		}

		motorB.stop();
		motorC.stop();
		Sound.beepSequence();

	}
}

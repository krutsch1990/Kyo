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

		int speed_max = 40;

		int speed_int_right;
		int speed_int_left;
		int max_reverse = -40;

		float last_error = 0;
		float mult_regler = 4f;
		float last_error_diff_linie=0;
		float last_error_diff_tisch=0;
		
		while (Button.ESCAPE.isUp()) {
			System.out.println(last_error);
			int reverse_value = 0;
			float actually = color.getbrightness();

			if (actually < mid) {

				float basis = linie;
				float max_error = mid - linie;
				float faktor = (actually - basis) / max_error;
				float error = (max_error - (actually - linie)) / max_error;

				float error_diff = error - last_error;
				if (error_diff > 0.4) {
					float revers_calc = (error_diff * 10) * mult_regler;
					reverse_value = (int) revers_calc;
				}

				
				float speed = faktor * speed_max;
				speed_int_right = (int) speed;

				motorB.setPower(speed_max);

				if ( (speed_int_right-reverse_value) < max_reverse) {
					motorC.setPower(max_reverse);
				} else {
					motorC.setPower(speed_int_right - reverse_value);
				}
				
				
				
				if ( error_diff < last_error_diff_linie) {
					last_error=error;
				}
				
				last_error_diff_linie=error_diff;
				
				
				
			
			} else if (actually > mid) {

				float basis = mid;
				float max_error = tisch - mid;
				float faktor = (actually - basis) / max_error;
				float error = 1 - ((max_error - (actually - mid)) / max_error);

				float error_diff = error - last_error;
				if (error_diff > 0.4) {
					float revers_calc = (error_diff * 10) * mult_regler;
					reverse_value = (int) revers_calc;
				}

				float speed = speed_max - (faktor * speed_max);
				speed_int_left = (int) speed;

				motorC.setPower(speed_max);

				if ((speed_int_left-reverse_value) < max_reverse) {
					motorB.setPower(max_reverse);
				}
				motorB.setPower(speed_int_left - reverse_value);
				
				
				if ( error_diff < last_error_diff_tisch) {
					last_error=error;
				}
				
				
				last_error_diff_tisch=error_diff;
				
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

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;

public class Ausweichen {

	static UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.B);
	static UnregulatedMotor motorC = new UnregulatedMotor(MotorPort.C);
	static Distance distance = new Distance();
	static Gyro gyro = new Gyro();
	static float last_error = 0;
	static Color color = new Color();

	public static void main(String[] args) {

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

		while (Button.ESCAPE.isUp()) {

			float actually = color.getbrightness();
			drive(tisch, linie, mid, actually);
		}

		motorB.stop();
		motorC.stop();
		Sound.beepSequence();
	}

	public static void drive(float tisch, float linie, float mid, float actually) {
		float error = get_error(tisch, linie, mid, actually);
		float error_diff = error - last_error;
		float next_error = error + error_diff;
		float reverse_value = 0;

		if (error < 0.2) {
			last_error = error;
		}

		if (next_error > 1.6f) {
			reverse_value = next_error * 20;
		}
		if (reverse_value > 35) {
			reverse_value = 35;
		}

		float basis;
		float max_error;

		float speed_max = 30;

		float abstand = distance.getdistance();

		if (abstand < 0.1f) {
			Sound.beepSequence();
			float end = (mid + tisch) / 2;
			ausweichen(end);
			Sound.beepSequence();
		}

		if (actually <= mid) {
			basis = linie;
			max_error = mid - linie;
		} else {
			basis = mid;
			max_error = tisch - mid;
		}

		float faktor = (actually - basis) / max_error;
		float speed = faktor * speed_max;
		if (actually > mid) {
			speed = speed_max - speed;
			int int_speed = (int) speed - (int) reverse_value;

			motorC.setPower((int) speed_max);
			motorB.setPower(int_speed);
		} else {
			int int_speed = (int) speed - (int) reverse_value;

			motorC.setPower(int_speed);
			motorB.setPower((int) speed_max);
		}
	}

	public static float get_error(float tisch, float linie, float mid, float actually) {
		float max_error;
		float diff;

		if (actually <= mid) {
			diff = linie;
			max_error = mid - linie;

		} else {
			diff = mid;
			max_error = tisch - mid;

		}
		float error = ((max_error - (actually - diff)) / max_error);
		if (actually > mid) {
			error = 1 - error;
		}

		return error;
	}

	public static void ausweichen(float end) {

		leave_line(90);
		move(2000, end);

		if (color.getbrightness() < end) {
			return;
		}
		turn_left(82, end);
		move(3000, end);

		if (color.getbrightness() < end) {
			return;
		}
		check(end);
		search(end);

	}

	public static void leave_line(int grad) {
		float last_pos = gyro.getgyro();

		while (gyro.getgyro() > (last_pos - grad)) {
			motorC.setPower(-25);
			motorB.setPower(25);
		}
	}

	public static void turn_right(int grad, float end) {
		
		
		float last_pos = gyro.getgyro();

		while (gyro.getgyro() > (last_pos - grad) && color.getbrightness() > end) {
			motorC.setPower(-25);
			motorB.setPower(25);
		}
	}

	public static void turn_left(int grad, float end) {
		
		float last_pos = gyro.getgyro();
		
		while (gyro.getgyro() < (last_pos + grad) && color.getbrightness() > end) {
			motorC.setPower(25);
			motorB.setPower(-25);
		}
	}

	public static void move(int time, float end) {

		final long endtime = System.currentTimeMillis() + time;


		while (System.currentTimeMillis() < endtime && color.getbrightness() > end) {
			motorC.setPower(30);
			motorB.setPower(30);
		}
		motorC.setPower(0);
		motorB.setPower(0);
	}

	public static void check(float end) {

		for (int i = 1; i < 10; ++i) {

			turn_left(10, end);

			if (color.getbrightness() < end) {
				return;
			}

			if (distance.getdistance() < 0.2f) {
				turn_right((i) * 10, end);
				move(3000, end);
				check(end);
				break;

			}
		}

	}

	public static void search(float end) {

		int flex = 10;

		while (color.getbrightness() > end && Button.ESCAPE.isUp()) {

			long loopend = System.currentTimeMillis() + 2000;

			while (System.currentTimeMillis() < loopend && color.getbrightness() > end) {

				motorC.setPower(flex);
				motorB.setPower(40);

			}
			if (flex < 30) {
				++flex;
			}

		}

	}

}

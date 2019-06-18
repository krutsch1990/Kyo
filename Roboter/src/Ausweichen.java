import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

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

		float speed_max = 35;

		float abstand = distance.getdistance();

		if (abstand < 0.3f) {

			ausweichen(mid);

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

	public static void ausweichen(float mid) {

		turn_right(90);
		move(2000);
		turn_left(90);
		move(4000);
		check();
		move(2000);
		search(mid);

	}

	public static void turn_right(int grad) {
		float actuell_pos = gyro.getgyro();
		float last_pos = actuell_pos;
		
		
		while (actuell_pos < (last_pos + grad)) {
			motorC.setPower(-20);
			motorB.setPower(20);
		}
	}

	public static void turn_left(int grad) {
		float actuell_pos = gyro.getgyro();
		float last_pos = actuell_pos;
		while (actuell_pos > (last_pos - grad)) {
			motorC.setPower(20);
			motorB.setPower(-20);
		}
	}

	public static void move(int time) {

		motorC.setPower(20);
		motorB.setPower(20);
		Delay.msDelay(time);
		motorC.stop();
		motorB.stop();
	}

	public static void check() {

		for (int i = 1; i < 10; ++i) {

			turn_left(10);
			float abstand = distance.getdistance();

			if (abstand < 0.6f) {
				turn_right(i*10);
				move(2000);
				check();
				break;
				
			}
		}

		

	}

	public static void search(float mid) {
		
		float actually = color.getbrightness();
		int rotation=20;
		
		while(actually > mid) {
			actually= color.getbrightness();
			float actuell_pos = gyro.getgyro();
			
			if ( (actuell_pos % 90) == 0) {
				rotation-=1;
			}
			
			
			motorC.setPower(rotation*(-1));
			motorB.setPower(rotation);
			
		}
	}
}

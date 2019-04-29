import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;

public class Start {

	static UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.B);
	static UnregulatedMotor motorC = new UnregulatedMotor(MotorPort.C);
	// static TouchSensor touch= new TouchSensor(SensorPort.S1);

	public static void main(String[] args) {

		// Aus der Klasse Color wird ein Objekt namens Color erzeugt.
		Color color = new Color();
		Distance distance = new Distance();
		
		// Start des Programms, es wird mit color.brightness jeweils die Helligkeit vom
		// Tisch
		// und von der Linie gemessen und in einen float abgespeichert
		Sound.beepSequence();
		System.out.println("Press any button");
		Button.waitForAnyPress();
		float tisch = color.getbrightness();
		Sound.beepSequence();
		Button.waitForAnyPress();
		float linie = color.getbrightness();
		Sound.beepSequence();

		
		float mid = (tisch + linie) / 2;

		System.out.println(tisch);
		System.out.println(linie);
		System.out.println(mid);

		Button.waitForAnyPress();
		System.out.println("i am driving");

		int speed_max = 25;
		int max_distance = 100;
		
		
		int error_count = 0;
		float error_sum = 0;
		float error_average = 1;
		int speed_int_right;
		int speed_int_left;

		while (Button.ESCAPE.isUp()) {
			
			float spacing = distance.getdistance();
			
			if (spacing < (max_distance * 0.3) ){
				speed_max= speed_max * 0;
			} else if ( spacing < max_distance ) {
				float anteil = (spacing/max_distance) * speed_max;
				speed_max = (int) anteil;
			}
			
			

			// reset bei 10000 Messungen
			if (error_count > 10000) {
				error_count = 0;
				error_sum = 0;
			}

			float actually = color.getbrightness();

			if (actually < mid) {

				float basis = linie;
				float max_error = mid - linie;
				float faktor = (actually - basis) / max_error;
				float error = (max_error - (actually - max_error)) / max_error;

				error_average = error_sum / error_count;

				float error_check = error / error_average;

				if (error_check > 2) {
					speed_int_right = -10;
				} else {
					++error_count;
					error_sum += error;
					float speed = faktor * speed_max;
					speed_int_right = (int) speed;
				}
				motorB.setPower(speed_max);
				motorC.setPower(speed_int_right);

			} else if (actually > mid) {

				float basis = mid;
				float max_error = tisch - mid;
				float faktor = (actually - basis) / max_error;
				float error = 1 - ((max_error - (actually - max_error)) / max_error);

				error_average = error_sum / error_count;

				float error_check = error / error_average;

				if (error_check > 2) {
					speed_int_left = -10;
				} else {
					++error_count;
					error_sum += error;
					float speed = speed_max - (faktor * speed_max);
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

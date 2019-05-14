import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;

public class Start {
	
	// RemoteEv3 ev3 = new RemoteEv3("192.168.0.223");
	// ev3.seDefault();
	// RemoteEv3 importieren
	
	static UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.B);
	static UnregulatedMotor motorC = new UnregulatedMotor(MotorPort.C);

	public static void main(String[] args) {

		Color color = new Color();
		Distance distance = new Distance();

		Sound.beepSequence();
		System.out.println("Press any button");
		Button.waitForAnyPress();
		float black = color.getbrightness();
		Sound.beepSequence();
		Button.waitForAnyPress();
		float table = color.getbrightness();
		Sound.beepSequence();
		
		float grey = (black+table) / 2;
		
		Button.waitForAnyPress();
		System.out.println("i hate my life");

		float[] error_list = new float [8];
	
		float now; 
		int count =0;
		
		float summe=0;
		float differenz=0;
		float error=0;
		
		double k_p = 100;
		double k_i = 0;
		double k_d = 0;
	
		int v_schnitt = 35;
		
		int min_speed=-30;
		int max_speed=35;
		
		
		now = color.getbrightness();
		float lasterror = now - grey;
		
		while (Button.ESCAPE.isUp()) {
			++count;
		
			now = color.getbrightness();
			
			motorB.forward();
			motorC.forward();
			
			
			// PID
			
			//P
			
			error = now - grey;
			error_list[count%error_list.length]= error;
			
			
			//
			summe=0;
			for (int i=0; i<error_list.length;++i) {
				summe+=error_list[i];
				
				
				//TEST2 
			//D
			differenz = error - lasterror;
				
				
			//
			double regler = k_p * error + k_i * summe + k_d * differenz;
			System.out.println(regler);
			int links = (int) ((regler) + v_schnitt);
			int rechts = (int) ( -1 * (regler) + v_schnitt);
			
			
			if (links < min_speed) {
				links =min_speed;
			}
			if (links > max_speed) {
				links = max_speed;
			}
			if (rechts < min_speed) {
				rechts =min_speed;
			}
			if (rechts > max_speed) {
				rechts = max_speed;
			}
			
			motorB.setPower(links);
			motorC.setPower(rechts);
			
			
			lasterror= error;
			
			
			}
		}
		
		motorB.stop();
		motorC.stop();
		Sound.beepSequence();

	}
}

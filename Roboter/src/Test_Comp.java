
public class Test_Comp {
	static  float last_error = 0;
	
	public static void main(String[] args) {

		float tisch = 0.69f;
		float linie = 0.11f;
		float mid = (tisch + linie) / 2;

		
		drive(tisch,linie,mid,0.4f);
		drive(tisch,linie,mid,0.35f);
		drive(tisch,linie,mid,0.6f);
	
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
	
	
	public static void drive(float tisch, float linie, float mid, float actually) {
		float error = get_error(tisch,linie,mid,actually);
		float error_diff= error - last_error;
		float next_error= error + error_diff;
		float reverse_value=0;
		
		if(error < 0.2) {
			last_error=error;
		}
//		System.out.println();
//		System.out.println("Letzter Error:" + last_error);
//		System.out.println("Jetziger Error:" + error);
//		System.out.println("Nächster Error" + next_error);
//		System.out.println(error_diff);
//		
		
		if( next_error > 0.6f) {
			System.out.print("ERROR FOUND   ");
			reverse_value=next_error*20;
		//	System.out.println(reverse_value);
		}
		
		if (reverse_value >35) {
			reverse_value=35;
		}
		
		float basis;
		float max_error;
		int speed_max = 40;

		if (actually <= mid) {
			basis = linie;
			max_error = mid - linie;
		} else {
			basis = mid;
			max_error = tisch - mid;
		}

		float faktor = (actually - basis) / max_error;
		float speed = (faktor * speed_max);
		if (actually > mid) {
			speed = speed_max - speed;
			int int_speed = (int) speed - (int) reverse_value;
			System.out.print("MotorB links" + (int_speed)+"  ");
			System.out.println("MotorC rechts" + speed_max);
		} else {
			int int_speed = (int) speed - (int) reverse_value;
			System.out.print("MotorB links" + speed_max+"  ");
			System.out.println("MotorC rechts" + (int_speed));
		}
	}

}

package game;

public class Tools {
	/**
	 * Returns a random boolean
	 * @return
	 */
	public static boolean randomBool(){
		if(Math.random()<.5){
			return false;
		}
		return true;
	}
	/**
	 * Returns a random integer between min and max.
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomInt(int min, int max){
		int center = (min+max-1)/2;
		if(center==0){
			center = 1;
		}
		if(randomBool()){
			return center - (int)(Math.random()*center);
		}
		return center + (int)(Math.random()*center);
	}
	public static int randomDir(){
		double t = Math.random();
		double test = t/3;
		if(t<=test){
			return -1;
		}
		if(t>test&&t<=test*2){
			return 0;
		}
		return 1;
	}
	public static float randomfloat(float min, float max){
		float center = (min+max)/2;
		if(center==0){
			center = 1;
		}
		if(randomBool()){
			return center - (float)(Math.random()*center);
		}
		return center + (float)(Math.random()*center);
	}
}

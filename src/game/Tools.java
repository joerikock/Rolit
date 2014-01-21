package game;

/**
 * Class containing various tools that are used throughout our entire 
 * program. Mostly for generating random values.
 * 
 * @author Max Messerich en Joeri Kock
 */

public class Tools {
	/**
	 * Returns a random boolean.
	 * 
	 * @return true of false.
	 */
	public static boolean randomBool(){
		if(Math.random()<.5){
			return false;
		}
		return true;
	}
	/**
	 * Returns a random integer between min and max.
	 * 
	 * @param min
	 * 			the minimum integer.
	 * @param max
	 * 			the maximum integer.
	 * @return a random integer between min and max.
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
	
	/**
	 * Method for generating a random double.
	 * 
	 * @return random double.
	 */
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
	
	/**
	 * Method for generating a random float between min and max.
	 * 
	 * @param min
	 * 			the minimum float.
	 * @param max
	 * 			the maximum float.
	 * @return a random float between min and max.
	 */
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

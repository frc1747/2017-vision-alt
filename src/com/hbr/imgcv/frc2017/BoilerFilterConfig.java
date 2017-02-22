package com.hbr.imgcv.frc2017;

public interface BoilerFilterConfig {

	interface Camera {
		public final double FOV_X_DEGREES       = 67; 
		public final double FOV_Y_DEGREES       = 51;
		public final double FOV_X_RADIANS       = Math.toRadians(FOV_X_DEGREES);
		public final double FOV_Y_RADIANS       = Math.toRadians(FOV_Y_DEGREES);
		public final double RESOLUTION_X_PIXELS = 800;
		public final double RESOLUTION_Y_PIXELS = 600;
		
	}
	
	interface ImageFiltering{
		//public final int[] COLOR_MIN = {140, 65, 40};
		//public final int[] COLOR_MAX = {190, 100, 100};
		
		public final int[] COLOR_MIN = {74, 115, 114}; //TODO: these values were obtained from test images and MSPaint, need to get real data
		public final int[] COLOR_MAX = {109, 255, 255};
		//the original mins/maxs were in RGB and were min: {60,255,120} and max: {120,100,71}
		public final int EROSION_SIZE = 10; //TODO: choose actual size
		public final int DILATION_SIZE = 10; //TODO: choose actual size
	}
	
	interface Analyze{
		public final double EPSILON = 5.0;
		public final double MAX_RATIO = 3.0;
		public final double MIN_RATIO = 1.0;
		public final double MAX_Y = 200.0;
		public final double MIN_Y = 40.0;
		public final double MIN_AREA = 20;
		
		public final double X_TARGETED_RANGE = 10;
		public final float X_TURNING_THRESHOLD = 80;
	}
	
	interface Rating{
		public final double RATIO_WEIGHT = 40.0;
		public final double POSITION_WEIGHT = 30.0;
		public final double HEIGHT_WEIGHT = 40.0;
		public final double WIDTH_WEIGHT = 50.0;
		public final double SIZE_WEIGHT = 6000.0;
		public final double IDEAL_RATIO = 4.0;
		public final double IDEAL_POSITION = 120.0;
		public final double IDEAL_HEIGHT = 60.0;
		public final double IDEAL_WIDTH = 120.0;
		public final double IDEAL_SIZE = 70.0;
	}
}

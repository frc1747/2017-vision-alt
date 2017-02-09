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
	
	interface ImageProcessing{
		public final int[] COLOR_MIN = {138, 76, 100}; //TODO: these values are probably close, but wrong because they were not obtained from an actual target
		public final int[] COLOR_MAX = {120, 100, 71};
		//the original mins/maxs were in RGB and were min: {60,255,120} and max: {120,100,71}
		public final int EROSION_SIZE = 0; //TODO: choose actual size
		public final int DILATION_SIZE = 0; //TODO: choose actual size
	}
}

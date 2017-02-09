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
		public final int[] COLOR_MIN = {60, 255, 120}; //TODO: these values are approximate and should be adjusted
		public final int[] COLOR_MAX = {0, 180, 0}; //TODO: these values are approximate and should be adjusted
	}
}

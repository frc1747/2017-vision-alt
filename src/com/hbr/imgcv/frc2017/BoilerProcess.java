package com.hbr.imgcv.frc2017;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.hbr.imgcv.PolygonCv;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class BoilerProcess implements BoilerFilterConfig{
	
	NetworkTable networkTable;
	
	public BoilerProcess(){
		 NetworkTable.setClientMode();
	     NetworkTable.setIPAddress("10.17.47.2");
	     NetworkTable.initialize();
	     networkTable = NetworkTable.getTable("SmartDashboard");
	}
	
	public Mat analyze(Mat src){
		Mat analysis = src.clone();
		ArrayList<MatOfPoint> contours =  new ArrayList<>();
		ArrayList<PolygonCv> polygons =  new ArrayList<>();
		Imgproc.findContours(analysis, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		if(contours.size() == 0){
			networkTable.putString("Targeted", "Target Not Found");
		}
			
		PolygonCv currentTarget;
		
		
		for(int i = 0; i < contours.size(); i++){
			currentTarget = PolygonCv.fromContour(contours.get(i), Analyze.EPSILON);

			//currently returns if targeted for each detected contour, might be fine for boiler
			if(currentTarget.getBoundingArea() > Analyze.MIN_AREA){
				if(Math.abs((currentTarget.getCenterX()) - 320) < Analyze.X_RANGE){
					System.out.println("Targeted");
					networkTable.putString("Targeted", "Targeted");
				}else if(currentTarget.getCenterX() < 320){
					System.out.print("Turn Left");
					networkTable.putString("Targeted", "Turn Left");
				}else if(currentTarget.getCenterX() > 320){
					System.out.print("Turn Right");
					networkTable.putString("Targeted", "Turn Right");
				}
				
			}
			
			Imgproc.drawContours(src, contours, i, new Scalar(255, 255, 255), 1);
		}
		
		
		return src;
	}
	
	protected double getTargetRating(PolygonCv inputTarget){
		 double targetRating = 1000000;
		 targetRating -= Rating.RATIO_WEIGHT * inputTarget.getBoundingAspectRatio() - Rating.IDEAL_RATIO;
		 targetRating -= Rating.POSITION_WEIGHT * ((inputTarget.getMaxY()-inputTarget.getMinY())/2) - Rating.IDEAL_POSITION;
		 targetRating -= Rating.HEIGHT_WEIGHT * (inputTarget.getHeight()) - Rating.IDEAL_HEIGHT;
		 targetRating -= Rating.WIDTH_WEIGHT * (inputTarget.getWidth()) - Rating.IDEAL_WIDTH;
		 targetRating -= Rating.SIZE_WEIGHT * (inputTarget.size()) - Rating.IDEAL_SIZE;
		 
		return targetRating;
	}
}
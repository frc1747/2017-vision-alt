package com.hbr.imgcv.frc2017;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.hbr.imgcv.PolygonCv;

public class BoilerProcess {
	
	private final double EPSILON = 5.0;
	private final double MAX_RATIO = 3.0;
	private final double MIN_RATIO = 1.0;
	private final double MAX_Y = 200.0;
	private final double MIN_Y = 40.0;
	
	private final double RATIO_WEIGHT = 40.0;
	private final double POSITION_WEIGHT = 30.0;
	private final double HEIGHT_WEIGHT = 40.0;
	private final double WIDTH_WEIGHT = 50.0;
	private final double SIZE_WEIGHT = 6000.0; //size does matter
	
	private final double IDEAL_RATIO = 4.0;
	private final double IDEAL_POSITION = 120.0;
	private final double IDEAL_HEIGHT = 60.0;
	private final double IDEAL_WIDTH = 120.0;
	private final double IDEAL_SIZE = 70.0;
	
	public Mat analyze(Mat src){
		Mat analysis = src.clone();
		ArrayList<MatOfPoint> contours =  new ArrayList<>();
		ArrayList<PolygonCv> polygons =  new ArrayList<>();
		Imgproc.findContours(analysis, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		PolygonCv currentTarget;
		
		for(int i = 0; i < contours.size(); i++){
			currentTarget = PolygonCv.fromContour(contours.get(i), EPSILON);
			
			if(MAX_RATIO > currentTarget.getBoundingAspectRatio() &&
					currentTarget.getBoundingAspectRatio() > MIN_RATIO &&
					!(MAX_Y > currentTarget.getMaxY()) &&
					!(MIN_Y < currentTarget.getMinY())
					){
				
				polygons.add(currentTarget);
				
			}
			
			Imgproc.drawContours(src, contours, i, new Scalar(255, 255, 255), 1);
		}
		
		return src;
	}
	
	protected double getTargetRating(PolygonCv inputTarget){
		 double targetRating = 1000000;
		 targetRating -= RATIO_WEIGHT * inputTarget.getBoundingAspectRatio() - IDEAL_RATIO;
		 targetRating -= POSITION_WEIGHT * ((inputTarget.getMaxY()-inputTarget.getMinY())/2) - IDEAL_POSITION;
		 targetRating -= HEIGHT_WEIGHT * (inputTarget.getHeight()) - IDEAL_HEIGHT;
		 targetRating -= WIDTH_WEIGHT * (inputTarget.getWidth()) - IDEAL_WIDTH;
		 targetRating -= SIZE_WEIGHT * (inputTarget.size()) - IDEAL_SIZE;
		 
		return targetRating;
	}
}
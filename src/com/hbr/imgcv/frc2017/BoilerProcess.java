package com.hbr.imgcv.frc2017;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.hbr.imgcv.PolygonCv;

public class BoilerProcess implements BoilerFilterConfig{
	
	public Mat analyze(Mat src){
		Mat analysis = src.clone();
		ArrayList<MatOfPoint> contours =  new ArrayList<>();
		ArrayList<PolygonCv> polygons =  new ArrayList<>();
		Imgproc.findContours(analysis, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		PolygonCv currentTarget;
		
		for(int i = 0; i < contours.size(); i++){
			currentTarget = PolygonCv.fromContour(contours.get(i), Analyze.EPSILON);
			
			if(Analyze.MAX_RATIO > currentTarget.getBoundingAspectRatio() &&
					currentTarget.getBoundingAspectRatio() > Analyze.MIN_RATIO &&
					!(Analyze.MAX_Y > currentTarget.getMaxY()) &&
					!(Analyze.MIN_Y < currentTarget.getMinY())
					){
				
				polygons.add(currentTarget);
				
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
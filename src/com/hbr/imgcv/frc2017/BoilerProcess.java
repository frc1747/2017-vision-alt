package com.hbr.imgcv.frc2017;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

import com.hbr.imgcv.PolygonCv;

public class BoilerProcess {
	
	private final double EPSILON = 5.0;
	private final double MAX_RATIO = 5.0;
	private final double MIN_RATIO = 3.0;
	private final double MAX_Y = 200.0;
	private final double MIN_Y = 40.0;
	
	public void analyze(Mat src){
		
		ArrayList<MatOfPoint> contours =  new ArrayList<>();
		ArrayList<PolygonCv> polygons =  new ArrayList<>();
		Imgproc.findContours(src, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		PolygonCv currentTarget;
		
		
		for(int i = 0; i < contours.size(); i++){
			currentTarget = PolygonCv.fromContour(contours.get(i), EPSILON);
			
			if(MAX_RATIO > currentTarget.getBoundingAspectRatio() &&
					currentTarget.getBoundingAspectRatio() > MIN_RATIO &&
					!(MAX_Y > currentTarget.getMaxY()) &&
					!(MIN_Y < currentTarget.getMinY())
					//currentTarget.getBoundingAspectRatio()
					){
				
				polygons.add(currentTarget);
				
			}
			
		}
	}
}
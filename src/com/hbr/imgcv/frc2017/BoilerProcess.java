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
	static final double BALL_AREA_THRESHOLD = 20;
	
	public BoilerProcess(){
		 NetworkTable.setClientMode();
	     NetworkTable.setIPAddress("10.17.47.2");
	     NetworkTable.initialize();
	     networkTable = NetworkTable.getTable("SmartDashboard");
	}
	
	public Mat analyze(Mat src){
		Mat analysis = src.clone();
		ArrayList<MatOfPoint> contours =  new ArrayList<>();
		PolygonCv largestTarget = null;
		Imgproc.findContours(analysis, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		

		if(contours.size() == 0){
			networkTable.putString("Targeted", "Target Not Found");
			System.out.println("Target Not Found");
		}
			
		PolygonCv currentTarget;
		
		
		for(int i = 0; i < contours.size(); i++){
			currentTarget = PolygonCv.fromContour(contours.get(i), Analyze.EPSILON);
			double radius = (currentTarget.getMaxY() - currentTarget.getMinY())/2;
			
			if(currentTarget.getBoundingArea() > Analyze.MIN_AREA){
				if(Math.abs(currentTarget.getBoundingArea() - Math.PI * radius*radius) < BALL_AREA_THRESHOLD){
					//will hopefully eliminate balls from consideration
					//will still display balls, though
				}
				else if(largestTarget == null){
					largestTarget = currentTarget;
				}else if(currentTarget.getBoundingArea() > largestTarget.getBoundingArea()){
					largestTarget = currentTarget;
				}				
			}
			
			Imgproc.drawContours(src, contours, i, new Scalar(255, 255, 255), 1);
		}
		
		//needs correction for camera offset, the method already exists, but is commented out (CameraCorrection)
		networkTable.putNumber("Boiler Targeted", targetOffset(largestTarget.getCenterX(), Analyze.X_TARGETED_RANGE, Analyze.X_TURNING_THRESHOLD));
		System.out.println(targetOffset(largestTarget.getCenterX(), Analyze.X_TARGETED_RANGE, Analyze.X_TURNING_THRESHOLD));
		
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
	
	private double targetOffset(double TargetCenter, double TargetedRange, double TurningThreshold){
		if(Math.abs(TargetCenter - 320) < TargetedRange){
			return 0;
		}else if(Math.abs(TargetCenter - 320) > TurningThreshold){
			return (TargetCenter-320)/Math.abs(TargetCenter - 320);
		}else{
			return (TargetCenter - 320)/TurningThreshold;
		}
	}
	
	//returns angle that camera would return if at center of robot
	/*private double CameraCorrection(double rawAngle, double cameraOffsetDistance, double distanceToTarget){
		double angleDegrees = Math.toRadians(rawAngle);
		return Math.toDegrees(Math.atan(cameraOffsetDistance/distanceToTarget + Math.tan(angleDegrees)));
	}*/
}
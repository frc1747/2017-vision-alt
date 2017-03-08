package com.hbr.imgcv.frc2017;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.hbr.imgcv.PolygonCv;
import com.hbr.imgcv.utils.DrawTool;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class BoilerProcess implements BoilerFilterConfig{
	
	NetworkTable networkTable;
	static final double BALL_AREA_THRESHOLD = 20;
	DrawTool draw;
	static final double HORIZONTAL_FOV = 47;
	static final double HORIZONTAL_RESOLUTION = 320;
	static final double VERTICAL_RESOLUTION = 240;
	static final double VERTICAL_FOV = HORIZONTAL_FOV * VERTICAL_RESOLUTION/HORIZONTAL_RESOLUTION;
	
	public BoilerProcess(){
	     draw = new DrawTool();
	}
	
	public Mat analyze(Mat src){
		Mat analysis = src.clone();
		ArrayList<MatOfPoint> contours =  new ArrayList<>();
		PolygonCv largestTarget = null;
		Imgproc.findContours(analysis, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		

		if(contours.size() == 0){
			//networkTable.putString("Targeted", "Target Not Found");
			//System.out.println("Target Not Found");
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
		//networkTable.putNumber("Test", 3.14);
		if(!(networkTable == null) && !(largestTarget == null) && contours.size() == 2){
			networkTable.putNumber("Boiler Horizontal", targetOffset(Analyze.X_TARGET, largestTarget.getCenterX(), Analyze.X_TARGETED_RANGE, Analyze.X_TURNING_THRESHOLD, HORIZONTAL_RESOLUTION, HORIZONTAL_FOV));
			networkTable.putNumber("Boiler Vertical", targetOffset(Analyze.Y_TARGET, largestTarget.getCenterY(), Analyze.Y_TARGETED_RANGE, Analyze.Y_MOVING_THRESHOLD, VERTICAL_RESOLUTION, VERTICAL_FOV));
			//System.out.println(targetOffset(Analyze.X_TARGET, largestTarget.getCenterX(), Analyze.X_TARGETED_RANGE, Analyze.X_TURNING_THRESHOLD, HORIZONTAL_RESOLUTION, HORIZONTAL_FOV));
			networkTable.putNumber("COUNTER", counter++);
		}else{
			System.out.println("Network Table Not Found");
		}
		
		//System.out.println(targetOffset(320, largestTarget.getCenterX(), Analyze.X_TARGETED_RANGE, Analyze.X_TURNING_THRESHOLD));
		
		return src;
	}
	int counter = 7;
	
	protected double getTargetRating(PolygonCv inputTarget){
		 double targetRating = 1000000;
		 targetRating -= Rating.RATIO_WEIGHT * inputTarget.getBoundingAspectRatio() - Rating.IDEAL_RATIO;
		 targetRating -= Rating.POSITION_WEIGHT * ((inputTarget.getMaxY()-inputTarget.getMinY())/2) - Rating.IDEAL_POSITION;
		 targetRating -= Rating.HEIGHT_WEIGHT * (inputTarget.getHeight()) - Rating.IDEAL_HEIGHT;
		 targetRating -= Rating.WIDTH_WEIGHT * (inputTarget.getWidth()) - Rating.IDEAL_WIDTH;
		 targetRating -= Rating.SIZE_WEIGHT * (inputTarget.size()) - Rating.IDEAL_SIZE;
		 
		return targetRating;
	}
	
	public void setNetworkTable(NetworkTable nt){
		networkTable = nt;
	}

	
	private double targetOffset(double desiredGoalLocation, double boilerCenter, double targetedRange, double movingThreshold, double resolution, double fov){
		/*if(Math.abs(targetCenter - targetLocation) < targetedRange){
			return 0;
		}else if(Math.abs(targetCenter - targetLocation) > movingThreshold){
			//System.out.println("Not Targeted");
			return (targetCenter - targetLocation)/Math.abs(targetCenter - targetLocation);
		}else{
			//System.out.println("Not Targeted");
			return (targetCenter - targetLocation)/movingThreshold;
		}*/
		return fov * (boilerCenter - desiredGoalLocation) / resolution;
		//negative means turn left
		//positive means back up
	}
	

	
	//returns angle that camera would return if at center of robot
	/*private double CameraCorrection(double rawAngle, double cameraOffsetDistance, double distanceToTarget){
		double angleDegrees = Math.toRadians(rawAngle);
		return Math.toDegrees(Math.atan(cameraOffsetDistance/distanceToTarget + Math.tan(angleDegrees)));
	}*/
}
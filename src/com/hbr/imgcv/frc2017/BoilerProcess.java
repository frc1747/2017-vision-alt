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
	
	static final double CAMERA_HEIGHT_FEET = 23./12;
	static final double BOILER_TARGET_HEIGHT_FEET = 7 + 2./12;
	static final double VERTICAL_DISTANCE = BOILER_TARGET_HEIGHT_FEET - CAMERA_HEIGHT_FEET;
	static final double TARGETED_OFFSET_DISTANCE_FEET = 91.5/12 - 2.44;
	static final double CAMERA_ANGLE = 45;//Math.atan(VERTICAL_DISTANCE/TARGETED_OFFSET_DISTANCE_INCHES);
	
	public BoilerProcess(){
	     draw = new DrawTool();
	}
	
	public Mat analyze(Mat src){
		Mat analysis = src.clone();
		ArrayList<MatOfPoint> contours =  new ArrayList<>();
		PolygonCv largestTarget = null;
		PolygonCv secondLargest = null;
		Imgproc.findContours(analysis, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		

		if(contours.size() == 0){
			//networkTable.putString("Targeted", "Target Not Found");
			////System.out.println("Target Not Found");
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
					secondLargest = largestTarget;
					largestTarget = currentTarget;
				}
			}
			
			Imgproc.drawContours(src, contours, i, new Scalar(255, 255, 255), 1);
		}
		
		PolygonCv highestTarget = null;
		
		if(secondLargest != null && highestTarget != null && (largestTarget.getCenterY() > secondLargest.getCenterY())){
			highestTarget = secondLargest;
		}else{
			highestTarget = largestTarget;
		}
		
		//needs correction for camera offset, the method already exists, but is commented out (CameraCorrection)
		//networkTable.putNumber("Test", 3.14);
		if(!(networkTable == null) && !(highestTarget == null)){
			networkTable.putNumber("Boiler Horizontal", targetOffset(Analyze.X_TARGET, highestTarget.getCenterX(), Analyze.X_TARGETED_RANGE, Analyze.X_TURNING_THRESHOLD, HORIZONTAL_RESOLUTION, HORIZONTAL_FOV));
			networkTable.putNumber("Boiler Vertical", offsetDistance(-targetOffset(Analyze.Y_TARGET, highestTarget.getCenterY(), Analyze.Y_TARGETED_RANGE, Analyze.Y_MOVING_THRESHOLD, VERTICAL_RESOLUTION, VERTICAL_FOV)));
			////System.out.println(targetOffset(Analyze.X_TARGET, largestTarget.getCenterX(), Analyze.X_TARGETED_RANGE, Analyze.X_TURNING_THRESHOLD, HORIZONTAL_RESOLUTION, HORIZONTAL_FOV));
			//networkTable.putNumber("COUNTER", counter++);
		}else{
			//System.out.println("Network Table Not Found");
		}
		
		////System.out.println(targetOffset(320, largestTarget.getCenterX(), Analyze.X_TARGETED_RANGE, Analyze.X_TURNING_THRESHOLD));
		
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
			////System.out.println("Not Targeted");
			return (targetCenter - targetLocation)/Math.abs(targetCenter - targetLocation);
		}else{
			////System.out.println("Not Targeted");
			return (targetCenter - targetLocation)/movingThreshold;
		}*/
		return fov * (boilerCenter - desiredGoalLocation) / resolution;
		//negative means turn left
		//positive means back up
	}
	
	private double offsetDistance(double offsetAngle){
//		return  Math.signum(offsetAngle) * (1/(Math.tan(CAMERA_ANGLE - offsetAngle) * VERTICAL_DISTANCE) - TARGETED_OFFSET_DISTANCE_INCHES);
		return -(VERTICAL_DISTANCE/Math.tan(((CAMERA_ANGLE - offsetAngle) * 2 * Math.PI)/360) - TARGETED_OFFSET_DISTANCE_FEET) / .6; //.8
	}
	
	private double accurateTargetOffset(double fov, double resolution, double boilerCenter, double lensWidthMillimeters, double desiredGoalLocation){
		double center = resolution / 2;
		double focalLength = lensWidthMillimeters / (2 * Math.tan(fov / 2)); //lensWidthMillimeters might be able to just be resolution, should test w/ robot
		double angleCurrentToCenter =  Math.atan((boilerCenter - center)/focalLength);
		double angleGoalToCenter = Math.atan((center - desiredGoalLocation)/focalLength); //will need to worry about sign of this too
		return angleCurrentToCenter - angleGoalToCenter; //might need to worry about addition vs. subtraction here
	}
	

	
	//returns angle that camera would return if at center of robot
	/*private double CameraCorrection(double rawAngle, double cameraOffsetDistance, double distanceToTarget){
		double angleDegrees = Math.toRadians(rawAngle);
		return Math.toDegrees(Math.atan(cameraOffsetDistance/distanceToTarget + Math.tan(angleDegrees)));
	}*/
}
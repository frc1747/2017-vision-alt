package com.hbr.imgcv.frc2017;

import org.opencv.core.Mat;

import com.hbr.imgcv.PolygonCv;
import com.hbr.imgcv.filters.BlackWhite;
import com.hbr.imgcv.filters.ColorRange;
import com.hbr.imgcv.filters.ColorSpace;
import com.hbr.imgcv.filters.Dilate;
import com.hbr.imgcv.filters.Erode;
import com.hbr.imgcv.filters.GrayScale;
import com.hbr.imgcv.filters.MatFilter;
import com.hbr.imgcv.utils.FovCalculator;

public class GearFilter extends Filter implements GearFilterConfig, MatFilter{

	private FovCalculator fov;
	private double distance; //distance from camera to wall
	
	private final ColorRange colorRange = new ColorRange(ImageFiltering.COLOR_MIN, ImageFiltering.COLOR_MAX, true);
	private final ColorSpace colorSpace = new ColorSpace(org.opencv.imgproc.Imgproc.COLOR_BGR2HSV);
	private final Erode erode = new Erode(ImageFiltering.EROSION_SIZE);
	private final Dilate dilate = new Dilate(ImageFiltering.DILATION_SIZE); //TODO: could also use better parameters than just a size
	private final GrayScale grayScale = new GrayScale();
	private final BlackWhite blackWhite = new BlackWhite();
	
	public PolygonCv bestTarget;
	
	public GearFilter(){
		fov = new FovCalculator(Camera.FOV_X_DEGREES, Camera.RESOLUTION_X_PIXELS, distance);
	}
	
	@Override
	public Mat process(Mat srcImage) {

		Mat outputImage = srcImage.clone();
		
		colorSpace.process(outputImage);
		colorRange.process(outputImage);
		erode.process(outputImage);
		dilate.process(outputImage);
		grayScale.process(outputImage);
		blackWhite.process(outputImage);
		
		outputImage = (new GearProcess()).analyze(outputImage);
		
		return outputImage;
	}
		
	public void targetAnalysis(PolygonCv target){
		
	}
}
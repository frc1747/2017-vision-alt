package com.hbr.imgcv.frc2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;

import com.hbr.imgcv.PolygonCv;
import com.hbr.imgcv.filters.BlackWhite;
import com.hbr.imgcv.filters.ColorRange;
import com.hbr.imgcv.filters.ColorSpace;
import com.hbr.imgcv.filters.Dilate;
import com.hbr.imgcv.filters.Erode;
import com.hbr.imgcv.filters.GrayScale;
import com.hbr.imgcv.filters.MatFilter;
import com.hbr.imgcv.frc2016.TargetFilterConfig.Imgproc;
import com.hbr.imgcv.utils.FovCalculator;

public class BoilerFilter extends Filter implements MatFilter, BoilerFilterConfig{

	private FovCalculator fov;
	private double distance; //distance from camera to wall
	
	private final ColorRange colorRange = new ColorRange(ImageProcessing.COLOR_MIN, ImageProcessing.COLOR_MAX, true);
	//private final ColorSpace colorSpace = new ColorSpace(org.opencv.imgproc.Imgproc.COLOR_BGR2HSV);
	private final Erode erode = new Erode(ImageProcessing.EROSION_SIZE);
	private final Dilate dilate = new Dilate(ImageProcessing.DILATION_SIZE); //TODO: could also use better parameters than just a size
	private final GrayScale grayScale = new GrayScale();
	private final BlackWhite blackWhite = new BlackWhite();
	
	public PolygonCv bestTarget;
	
	public BoilerFilter(){
		fov = new FovCalculator(Camera.FOV_X_DEGREES, Camera.RESOLUTION_X_PIXELS, distance);
	}
	
	@Override
	public Mat process(Mat srcImage) {
		
		List<PolygonCv> targets  = new ArrayList<>();
	     PolygonCv  bestTarget;
	    
		Mat outputImage = srcImage.clone();
		
		//colorSpace.process(outputImage);
		//colorRange.process(outputImage);
		erode.process(outputImage);
		dilate.process(outputImage);
		grayScale.process(outputImage);
		//blackWhite.process(outputImage);
		
		//(new BoilerProcess()).analyze(outputImage);
		
		return outputImage;
	}
		
	public void targetAnalysis(PolygonCv target){
		
	}
	
	//copied from frc2016.TargetFilter
	public void setColorRangeConfig(File configFile) {
		try {
			FileReader configFileReader = new FileReader(configFile);
			BufferedReader  configFileBReader = new BufferedReader(configFileReader);
			
			for(int i = 0; i < Imgproc.COLOR_MAX.length; i++) {
				Imgproc.COLOR_MAX[i] = Integer.parseInt(configFileBReader.readLine());
				System.out.println("Read & Set " + Imgproc.COLOR_MAX[i]);
				Imgproc.COLOR_MIN[i] = Integer.parseInt(configFileBReader.readLine());
				System.out.println("Read & Set " + Imgproc.COLOR_MIN[i]);

			}
			
			colorRange.setRanges(ImageProcessing.COLOR_MIN, ImageProcessing.COLOR_MAX, true);
			
			configFileReader.close();
			configFileBReader.close();

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

}

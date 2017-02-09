package com.hbr.imgcv.frc2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;

import com.hbr.imgcv.PolygonCv;
import com.hbr.imgcv.filters.ColorRange;
import com.hbr.imgcv.filters.MatFilter;
import com.hbr.imgcv.frc2016.TargetFilterConfig.Imgproc;
import com.hbr.imgcv.utils.FovCalculator;

public class BoilerFilter extends Filter implements MatFilter, BoilerFilterConfig{

	private FovCalculator fov;
	private double distance; //distance from camera to wall
	
	private final ColorRange colorRange = new ColorRange(ImageProcessing.COLOR_MIN, ImageProcessing.COLOR_MAX, true);
	
	public PolygonCv bestTarget;
	
	public BoilerFilter(){
		fov = new FovCalculator(Camera.FOV_X_DEGREES, Camera.RESOLUTION_X_PIXELS, distance);
	}
	
	@Override
	public Mat process(Mat srcImage) {
		
		List<PolygonCv> targets  = new ArrayList<>();
	     PolygonCv  bestTarget;
	     
		Mat outputImage = srcImage.clone();
		
		colorRange.process(outputImage);
		
		targets = findTargets(outputImage);
		
		if(targets.size() > 0) {
        	bestTarget = findBestTarget(targets);
        	        	
        	if(networkTable != null) { 
        		targetAnalysis(bestTarget); //no return as it simply writes data to netTables 
        		networkTable.putNumber("FrameCount", frameCount++); 
        		
        	}
		}
		
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

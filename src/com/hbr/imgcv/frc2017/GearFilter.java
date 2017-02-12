package com.hbr.imgcv.frc2017;

import org.opencv.core.Mat;

import com.hbr.imgcv.PolygonCv;
import com.hbr.imgcv.filters.Dilate;
import com.hbr.imgcv.filters.Erode;
import com.hbr.imgcv.filters.MatFilter;
import com.hbr.imgcv.filters.Otsu;
import com.hbr.imgcv.filters.SingleColor;


public class GearFilter extends Filter implements GearFilterConfig, MatFilter{
	
	private final Erode erode = new Erode(ImageFiltering.EROSION_SIZE);
	private final Dilate dilate = new Dilate(ImageFiltering.DILATION_SIZE); //TODO: could also use better parameters than just a size
	private final SingleColor singleColor = new SingleColor();
	private final Otsu otsu = new Otsu();
	
	public PolygonCv bestTarget;
	
	@Override
	public Mat process(Mat srcImage) {

		Mat outputImage = srcImage.clone();
		
		erode.process(outputImage);
		dilate.process(outputImage);
		outputImage = singleColor.process(outputImage);
		outputImage = otsu.process(outputImage);
		
		outputImage = (new GearProcess()).analyze(outputImage);
		
		return outputImage;
	}
		
	public void targetAnalysis(PolygonCv target){
		
	}
}

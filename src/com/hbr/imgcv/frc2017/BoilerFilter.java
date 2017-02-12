package com.hbr.imgcv.frc2017;

import org.opencv.core.Mat;

import com.hbr.imgcv.PolygonCv;
import com.hbr.imgcv.filters.SingleColor;
import com.hbr.imgcv.filters.MatFilter;
import com.hbr.imgcv.filters.Otsu;

public class BoilerFilter extends Filter implements MatFilter, BoilerFilterConfig{

	//private final Erode erode = new Erode(ImageFiltering.EROSION_SIZE);
	//private final Dilate dilate = new Dilate(ImageFiltering.DILATION_SIZE); //TODO: could also use better parameters than just a size
	private final SingleColor alternateColorFilter = new SingleColor(SingleColor.GREEN);
	private final Otsu otsu = new Otsu();
	
	public PolygonCv bestTarget;
	
	@Override
	public Mat process(Mat srcImage) {

		Mat outputImage = srcImage.clone();
		
		outputImage = alternateColorFilter.process(outputImage);
		outputImage = otsu.process(outputImage);
		//erode.process(outputImage);
		//dilate.process(outputImage);
		
		outputImage = (new BoilerProcess()).analyze(outputImage);
		
		return outputImage;
	}
		
	public void targetAnalysis(PolygonCv target){
		
	}
	
	public static String getURL(){
		return "10.17.47.16";
	}
}

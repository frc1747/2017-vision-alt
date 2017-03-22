package com.hbr.imgcv.frc2017;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import com.hbr.imgcv.PolygonCv;
import com.hbr.imgcv.filters.SingleColor;
import com.hbr.imgcv.utils.DrawTool;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

import com.hbr.imgcv.filters.MatFilter;
import com.hbr.imgcv.filters.Otsu;

public class BoilerFilter extends Filter implements MatFilter, BoilerFilterConfig{

	//private final Erode erode = new Erode(ImageFiltering.EROSION_SIZE);
	//private final Dilate dilate = new Dilate(ImageFiltering.DILATION_SIZE); //TODO: could also use better parameters than just a size
	private final SingleColor alternateColorFilter = new SingleColor(SingleColor.GREEN);
	private final Otsu otsu = new Otsu();
	private final BoilerProcess process = new BoilerProcess();
	
	public PolygonCv bestTarget;
	
	@Override
	public Mat process(Mat srcImage) {		
		Mat outputImage = alternateColorFilter.process(srcImage);
		outputImage = otsu.process(outputImage);
		//erode.process(outputImage);
		//dilate.process(outputImage);
		
		outputImage = process.analyze(outputImage);
		addRectangle(srcImage);
//		return outputImage;
		return srcImage;
	}
		
	public void targetAnalysis(PolygonCv target){
		
	}
	
	public static String getURL(){
		return "10.17.47.16";
	}
	@Override
    public void setNetworkTable(NetworkTable nt) {
    	networkTable = nt;
    	process.setNetworkTable(nt);
    }
	public void addRectangle(Mat src){
		////System.out.println("running");
		DrawTool draw = new DrawTool();
		draw.setImage(src);
		draw.setThickness(3);
		draw.setColor(new Scalar(18, 120, 255));
		Point topLeft = new Point(120-5, 140-5);
		Point bottomRight = new Point(172+5,151+5);
		draw.drawRectangle(topLeft, bottomRight);
	}
	
}

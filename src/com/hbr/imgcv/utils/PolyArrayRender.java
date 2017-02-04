package com.hbr.imgcv.utils;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Scalar;

import com.hbr.imgcv.PolygonCv;

import org.opencv.core.Mat;

public class PolyArrayRender extends PolygonRender {
	
	List<PolygonCv> polygons = new ArrayList<>();
	
	public PolyArrayRender(Scalar colors, int thickness) {
		super(colors, thickness);
	}
	
	public void setPolygon(List<PolygonCv> polygons) {
		this.polygons = polygons;
	}
	
	public Mat process(Mat srcImage) {
		for(int i = 0; i < polygons.size(); i ++) {
			super.setPolygon(polygons.get(i));
			super.process(srcImage);
		}
		return srcImage;
	}
}

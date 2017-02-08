package com.hbr.imgcv.frc2017;

import org.opencv.core.Mat;

import com.hbr.imgcv.filters.MatFilter;
import com.hbr.imgcv.utils.FovCalculator;

public class BoilerFilter extends Filter implements MatFilter, BoilerFilterConfig{

	private FovCalculator fov;
	
	@Override
	public Mat process(Mat srcImage) {
		return null;
	}

}

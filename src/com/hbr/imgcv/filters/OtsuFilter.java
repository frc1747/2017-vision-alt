package com.hbr.imgcv.filters;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public final class OtsuFilter implements MatFilter{

	@Override
	public Mat process(Mat srcImage) {
		Mat outputImage = srcImage.clone();
		Imgproc.threshold(srcImage, outputImage, 0, 255, Imgproc.THRESH_OTSU);
		return outputImage;
	}

}

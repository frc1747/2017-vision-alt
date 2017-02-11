package com.hbr.imgcv.filters;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public final class AlternateColorFilter implements MatFilter{

	//Note to self: Default is in BGR
	@Override
	public Mat process(Mat srcImage) {
		Mat workingImage = srcImage.clone(); 
		
		int nchannels = workingImage.channels();
		int nrows = workingImage.rows();
        int ncols = workingImage.cols();
        
        Mat outputImage = new Mat(nrows, ncols, CvType.CV_8UC1);
        
        byte[] colors = new byte[nchannels];
        
        for (int row = 0; row < nrows; row++) {
        	for (int col = 0; col < ncols; col++) {
        		workingImage.get(row, col, colors);
        		int red = Byte.toUnsignedInt(colors[2]);
        		int green = Byte.toUnsignedInt(colors[1]);
        		int blue = Byte.toUnsignedInt(colors[0]);
        	
        		if(green - red/2 - blue/2 <= 0){
        			outputImage.put(row, col, 0);
        		}else{
        			outputImage.put(row, col, (green - red/2 - blue/2));
        			//System.out.println(( (int)green - (int)red/2 - (int)blue/2));
        		}
        	}
        }
        
		return outputImage;
	}

}

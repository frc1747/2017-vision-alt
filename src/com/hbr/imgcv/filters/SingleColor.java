package com.hbr.imgcv.filters;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * An image filter which keeps areas with high values of the specified channel.
 * The default blue, green, and red channel values are for BGR images.
 * 
 * <p>The resulting image will be an 8-bit, single channel, unsigned matrix.
 * 
 * @author Jon Hillery
 *
 */
public final class SingleColor implements MatFilter{
	
	/**
	 * Channel to keep.
	 */
	private int keptChannel;
	
	/**
	 * One of the channels to filter.
	 */
	private int filteredChannel1;
	
	/**
	 * One of the channels to filter.
	 */
	private int filteredChannel2;
	
	public static final int
		BLUE = 0,
		GREEN = 1,
		RED = 2;
	
	/**
	 * Creates a new instance of the single color filter with the specified color kept.
	 * @param keptChannel The channel to keep.
	 */
	public SingleColor(int keptChannel){
		this.keptChannel = keptChannel;
		filteredChannel1 = (keptChannel+1)%3;
		filteredChannel2 = (keptChannel+2)%3;
	}
	
	/**
	 * Creates a new instance of the single color filter with channel 1 (green for BGR images) as the kept channel.
	 */
	public SingleColor(){
		keptChannel = GREEN;
		filteredChannel1 = BLUE;
		filteredChannel2 = RED;
	}
	
	@Override
	public Mat process(Mat srcImage) {
		int nchannels = srcImage.channels();
		int nrows = srcImage.rows();
        int ncols = srcImage.cols();
        
        Mat outputImage = new Mat(nrows, ncols, CvType.CV_8UC1);
        
        byte[] colors = new byte[nchannels];
        
        for (int row = 0; row < nrows; row++) {
        	for (int col = 0; col < ncols; col++) {
        		srcImage.get(row, col, colors);
        		int keep = Byte.toUnsignedInt(colors[keptChannel]);
        		int filter1 = Byte.toUnsignedInt(colors[filteredChannel1]);
        		int filter2 = Byte.toUnsignedInt(colors[filteredChannel2]);
        	
        		if(keep - filter1/2 - filter2/2 <= 0){
        			outputImage.put(row, col, 0);
        		}else{
        			outputImage.put(row, col, (keep - filter1/2 - filter2/2));
        		}
        	}
        }
        
		return outputImage;
	}

}

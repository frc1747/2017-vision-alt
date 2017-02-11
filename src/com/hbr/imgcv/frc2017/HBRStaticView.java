package com.hbr.imgcv.frc2017;

import com.hbr.imgcv.FilterToolGuiOpenCv;

public final class HBRStaticView extends FilterToolGuiOpenCv{

    private HBRStaticView() {
		super("Static Vision");
		// TODO Auto-generated constructor stub
	}
    
    @Override
    protected void addControls(){
    	super.addControls();
    	super.addImageProcessingButton("Boiler Filter", new BoilerFilter());
    	super.addImageProcessingButton("Gear Filter", new GearFilter());
    	//If we want to add buttons for filters to the sidebar, put that here
    	//Otherwise, this method can be deleted
    }

    
    public static void main(String[] args) {
        // Create the GUI application and then start it's main routine
    	final FilterToolGuiOpenCv frame = new HBRStaticView();
        frame.main();
    }
}

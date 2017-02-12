package com.hbr.imgcv.frc2017;

import java.awt.HeadlessException;

import org.opencv.core.Core;

import com.hbr.imgcv.LiveViewGui;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class HBRLiveView extends LiveViewGui{
	
	private static NetworkTable networkTable;
	static final String SHOOTER_CAMERA_ENABLED_KEY = "ShooterCameraEnabled";

	BoilerFilter filter = new BoilerFilter();

	public HBRLiveView(String title) throws HeadlessException {
		super(title);
		setFilter(filter); //this sets default filter, change if necessary
	}
	
	protected void addMenuItems(){
		super.addMenuItems();
	}
	
	
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				
		NetworkTable.setClientMode();
        NetworkTable.setIPAddress("10.17.47.2");
        NetworkTable.initialize();
        networkTable = NetworkTable.getTable("SmartDashboard");
        
		final HBRLiveView frame = new HBRLiveView("Live Vision");
		
        frame.filter.setNetworkTable(networkTable);        
        frame.main();
        
        boolean lastState = false;
        while(true) { //hangs when NOT cameraEnable
        	boolean enable = !networkTable.getBoolean(SHOOTER_CAMERA_ENABLED_KEY, false);
        	if (enable != lastState) {
        		lastState = enable;
        		if (enable) {
            		frame.startVideoFeed();
        		} else {
                	frame.stopVideoFeed();       			
        		}
        		System.out.println("Camera feed toggled to: " + enable);
        	}
        	
        	try{
        		Thread.sleep(10);
        	} catch (Exception e) {	
        	}
        }
	}

}

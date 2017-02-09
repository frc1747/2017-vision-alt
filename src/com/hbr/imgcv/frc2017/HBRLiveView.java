package com.hbr.imgcv.frc2017;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;

import org.opencv.core.Core;

import com.hbr.imgcv.LiveViewGui;
import com.hbr.imgcv.frc2016.LiveView2016;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class HBRLiveView extends LiveViewGui{
	
	private static NetworkTable networkTable;
	static final String SHOOTER_CAMERA_ENABLED_KEY = "ShooterCameraEnabled";
    private String gameState;
	private static final String GAME_STATE = "GameState";
	
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
		
		final HBRLiveView frame = new HBRLiveView("Live Vision");
		
		NetworkTable.setClientMode();
        NetworkTable.setIPAddress("10.17.47.2");
        NetworkTable.initialize();
        networkTable = NetworkTable.getTable("SmartDashboard");
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

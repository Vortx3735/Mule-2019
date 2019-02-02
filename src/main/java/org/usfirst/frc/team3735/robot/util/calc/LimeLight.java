/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3735.robot.util.calc;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Add your docs here.
 */
public class LimeLight {

    NetworkTable table;
    NetworkTableEntry tx; 
    NetworkTableEntry ty; 
    NetworkTableEntry ta; 
    NetworkTableEntry ts; 
    NetworkTableEntry tl; 
    NetworkTableEntry getpipe;
    
    //setter dec
    NetworkTableEntry ledMode;
    NetworkTableEntry camMode;
    NetworkTableEntry pipeline;
    NetworkTableEntry stream;
    NetworkTableEntry snapshot;


    public LimeLight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");  
        tx = table.getEntry("tx"); 
        ty = table.getEntry("ty");
        ta = table.getEntry("ta");
        ts = table.getEntry("ts");
        tl = table.getEntry("tl");
        getpipe = table.getEntry("getpipe");
        ledMode = table.getEntry("ledMode");
        camMode = table.getEntry("camMode");
        pipeline = table.getEntry("pipeline");
        stream = table.getEntry("stream");
        snapshot = table.getEntry("snapshot");
    }

    // setters
    

    public void setLedMode(double ledType) {
        ledMode.setNumber(ledType);
    }

    public void setCamMode(double CamType) {
       camMode.setNumber(CamType);
    }

    public void setPipeline(double Pipeline) {
        pipeline.setNumber(Pipeline);
    }

    public void setStreamMode(double Stream) {
        stream.setNumber(Stream);
    }

    public void setSnapshot(double Snapshot) {
        snapshot.setNumber(Snapshot);
    }

    public void log() {
       System.out.println("tx: " + tx + " ty: " + ty + " ta: " + ta + " ts: " + ts + " tl: " + tl ); 
    }

}

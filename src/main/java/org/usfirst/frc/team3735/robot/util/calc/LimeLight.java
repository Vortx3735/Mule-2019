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

    double horiDis;
    double mountAngle;
    double mountHeight;
    double targetHeight;

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
        mountAngle = 0;
        mountHeight = 0;
        targetHeight = 0;
        setCamMode(0);
        setLedMode(0);
        setPipeline(0);
        setStreamMode(0);
    }


    //works better if target is above LimeLight
    public double getHoriDis(){
        double a = (getTargetHeight()-getMountHeight()) / 
            (Math.tan( getMountAngle() )+ty.getDouble(0.0));
            return a;
    }
    public void setTargetHeight(double a){
        targetHeight = a;
    }
    public double getTargetHeight(){
        return targetHeight;
    }
    public void setMountAngle(double a){
        mountAngle = Math.toRadians(a);
    }
    
    public double getMountAngle(){
        return mountAngle;
    }

    public void setMountHeight(double a){
        mountHeight = a;
    }
    public double getMountHeight(){
        return mountHeight;
    }

        // setters
    /**
     * @return the tx
     */
    public double getTx() {
       return tx.getDouble(0.0);
    }

    /**
     * @return the ty
     */
    public double getTy() {
        return ty.getDouble(0.0);
    }

    /**
     * @return the ta
     */
    public double getTa() {
        return ta.getDouble(0.0);
    }

    /**
     * @return the ts
     */
    public double getTs() {
        return ts.getDouble(0.0);
    }

    /**
     * @return the tl
     */
    public double getTl() {
        return tl.getDouble(0.0);
    }

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
       System.out.println("tx: " + getTx() + " ty: " + getTy() + " ta: " + getTa() + " ts: " + getTs() + " tl: " + getTl() ); 
    }

}

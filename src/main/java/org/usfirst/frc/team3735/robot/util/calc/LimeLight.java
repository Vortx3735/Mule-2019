/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3735.robot.util.calc;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Add your docs here.
 */
public class LimeLight extends Subsystem {

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
    NetworkTableEntry tv;
    NetworkTableEntry getpipe;
    
    //setter dec
    NetworkTableEntry ledMode;
    NetworkTableEntry camMode;
    NetworkTableEntry pipeline;
    NetworkTableEntry stream;
    NetworkTableEntry snapshot;

    double txValue;
    double taValue;
    double distance;


    public LimeLight() {
        table = NetworkTableInstance.getDefault().getTable("");  
        tx = table.getEntry("tx"); 
        ty = table.getEntry("ty");
        ta = table.getEntry("ta");
        ts = table.getEntry("ts");
        tl = table.getEntry("tl");
        tv = table.getEntry("tv");
        getpipe = table.getEntry("getpipe");
        ledMode = table.getEntry("ledMode");
        camMode = table.getEntry("camMode");
        pipeline = table.getEntry("pipeline");
        stream = table.getEntry("stream");
        snapshot = table.getEntry("snapshot");

        //change these values when testing.
        mountAngle = 0;
        mountHeight = 0;
        targetHeight = 0;
        //

        setCamMode(0);
        setLedMode(0);
        setPipeline(1);
        setStreamMode(0);
    }


    //works better if target is above LimeLight
    public double getHoriDis(){
        double a = (getTargetHeight()-getMountHeight()) / 
            (Math.tan( getMountAngle()+ ty.getDouble(0.0) ));
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
        if(tv.getDouble(0.0)==1) {
            txValue = tx.getDouble(txValue);
        }
       return txValue;
    }

    /**
     * @return the ty
     */
    public double getTy() {
        return ty.getDouble(0.0);
    }

    public double getTv() {
        return tv.getDouble(0.0);
    }

    /**
     * @return the ta
     */
    public double getTa() {
        if(tv.getDouble(0.0)==1) {
            taValue = ta.getDouble(taValue);
        }
       return taValue;
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

    public double getDistance() {
        return 75.315 * Math.pow(getTa(), -.484);
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

    @Override
    protected void initDefaultCommand() {

    }

    public void log() {
        SmartDashboard.putNumber("tx", getTx());
        SmartDashboard.putNumber("ty", getTy());
        SmartDashboard.putNumber("ta", getTa());
        SmartDashboard.putNumber("tv" , getTv());
        SmartDashboard.putNumber("Distance", getDistance());
    }

   

}

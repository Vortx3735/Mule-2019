/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3735.robot.util.calc;

import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Add your docs here.
 */
public class LimeLight {

    public void log()
    {
        System.out.println(getValidTarget());
        System.out.println(getHorOffset());
        System.out.println(getVerOffset());
        System.out.println(getTargetArea());
        System.out.println(getSkewRotation());
        System.out.println(getLatency());
        System.out.println(getSideShortest());
        System.out.println(getSideLongest());
        System.out.println(getHorSidelength());
        System.out.println(getVerSidelength());
        System.out.println(getPipelineIndex());
    }

    // getters

    public double getValidTarget() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    }

    public double getHorOffset() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    }

    public double getVerOffset() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    }

    public double getTargetArea() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    }

    public double getSkewRotation() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ts").getDouble(0);
    }

    public double getLatency() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tl").getDouble(0);
    }

    public double getSideShortest() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tshort").getDouble(0);
    }

    public double getSideLongest() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tlong").getDouble(0);
    }

    public double getHorSidelength() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("thor").getDouble(0);
    }

    public double getVerSidelength() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tvert").getDouble(0);
    }

    public double getPipelineIndex() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("getpipe").getDouble(0);
    }

    // setters
    

    public void setLedMode(double ledMode) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(ledMode);
    }

    public void setCamMode(double CamMode) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(CamMode);
    }

    public void setPipeline(double Pipeline) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(Pipeline);
    }

    public void setStreamMode(double Stream) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("stream").setNumber(Stream);
    }

    public void setSnapshot(double Snapshot) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("snapshot").setNumber(Snapshot);
    }

}

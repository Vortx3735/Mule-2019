package org.usfirst.frc.team3735.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team3735.robot.Robot;
import org.usfirst.frc.team3735.robot.util.calc.*;
import org.usfirst.frc.team3735.robot.util.hardware.VortxAhrs;
import org.usfirst.frc.team3735.robot.util.profiling.*;

//import Robot.Side;


public class Navigation extends Subsystem {
	private static final int BUMP_THRESHOLD = 1;

	private VortxAhrs ahrs;

	Position pos = new Position(0,0,0);
	private Object posLock = new Object();
	
	NetworkTable table;

	private double prevLeft = 0;
	private double prevRight = 0;
	private double curLeft;
	private double curRight;
	
	public Navigation(){
		table = NetworkTableInstance.getDefault().getTable("MAP");
		
		ahrs = new VortxAhrs(SPI.Port.kMXP);

		setPosition(new Position(0,0,0));

    	
		curLeft = Robot.drive.getLeftPosition();
    	curRight = Robot.drive.getRightPosition();
    	prevLeft = curLeft;
    	prevRight = curRight;
	}

	public synchronized void setPosition(Position p){
		synchronized(posLock){
			pos = p;
		}
		ahrs.setYaw(p.yaw);
	}
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
    }
    
    public synchronized void integrate(){
    	synchronized(posLock){
    		curLeft = Robot.drive.getLeftInches();
        	curRight = Robot.drive.getRightInches();
        	
        	double dd = ((curLeft-prevLeft) + (curRight-prevRight)) * .5;
        	
        	pos.yaw = getYaw();
        	double angle = VortxMath.swapYawAngle(pos.yaw);

    		pos.x += Math.cos(Math.toRadians(angle)) * dd;
    		pos.y += Math.sin(Math.toRadians(angle)) * dd;
    		
        	prevLeft = curLeft;
        	prevRight = curRight;
        	
    	}
    	
    	
    }
    
    public double getYaw(){
    	return ahrs.getYaw();
    }
    
    public void setYaw(double offset) {
    	ahrs.setYaw(offset);
    }

    
    public void zeroYaw(){
    	ahrs.zeroYaw();
    }
    public void resetAhrs(){
    	ahrs.reset();
    }
    public double getRate(){
    	return ahrs.getRate();
    }
    
    public AHRS getAHRS(){
    	return ahrs;
    }
    public void log(){
    	SmartDashboard.putNumber("Robot Yaw", getYaw());
    	SmartDashboard.putNumber("Nav Loc X inches", pos.x);
    	SmartDashboard.putNumber("Nav Loc Y inches", pos.y);
    	SmartDashboard.putNumber("Nav Acc", this.getXYAcceleration());
//    	SmartDashboard.putNumber("Gyro Acceleration X", ahrs.getWorldLinearAccelX());
//    	SmartDashboard.putNumber("Gyro Acceleration Y", ahrs.getWorldLinearAccelY());
//    	SmartDashboard.putNumber("Gyro Accel XY Vector", getXYAcceleration());

 //     displayDebugGyroData();

    }
    
    public void displayPosition(){
    	table.getEntry("CenterX").setDoubleArray(new double[]{pos.x});
    	table.getEntry("CenterY").setDoubleArray(new double[]{pos.y});
    	table.getEntry("Yaw").setDoubleArray(new double[]{pos.yaw});

    }
	public boolean isBumped() {
		return getXYAcceleration() > BUMP_THRESHOLD;
	}
	
	public double getXYAcceleration(){
		return Math.hypot(ahrs.getWorldLinearAccelY(), ahrs.getWorldLinearAccelX());
	}
	


	public void debugLog() {
		
	}

	public synchronized void resetPosition(Position p) {
		Robot.drive.resetEncodersPositions();
		setPosition(p);
		
		prevLeft = 0;
		prevRight = 0;
		
		System.out.println("Reseting Position...");
	}

	public synchronized void resetPosition() {
		
	}
	
	public synchronized Position getPosition() {
		return pos;
	}
	
	/**
	 * 
	 * @return	the quadrant the robot is in
	 * 			follows the quadrant naming system of algebra, looking at the field from Field Map.PNG
	 */

	public Location getClosestLocation(Location[] locs) {
		if(locs != null && locs.length > 0) {
			Location curPos = getPosition();
			double least = curPos.distanceFrom(locs[0]);
			int best = 0;
			for(int i = 1; i < locs.length; i++) {
				double dist = curPos.distanceFrom(locs[i]);
				if(dist < least) {
					least = dist;
					best = i;
				}
			}
			return locs[best];
		}else {
			System.out.println("Error in getting closest location");
			return new Location(0,0);
		}
	}
	
	public double getAngleToLocation(Location loc) {
		return VortxMath.swapYawAngle(getYawToLocation(loc));
	}
	
	public double getYawToLocation(Location loc) {
		return pos.yawTo(loc);
	}
	    
}

package org.usfirst.frc.team3735.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team3735.robot.Robot;
import org.usfirst.frc.team3735.robot.util.calc.*;
import org.usfirst.frc.team3735.robot.util.hardware.VortxAhrs;
import org.usfirst.frc.team3735.robot.util.profiling.*;
import org.usfirst.frc.team3735.robot.util.settings.Setting;

//import Robot.Side;


public class Navigation extends Subsystem implements PIDSource, PIDOutput {
	private static final int BUMP_THRESHOLD = 1;

	private VortxAhrs ahrs;

	Position pos = new Position(0,0,0);
	private Object posLock = new Object();
	
	NetworkTable table;

	private double prevLeft = 0;
	private double prevRight = 0;
	private double curLeft;
	private double curRight;

	PIDController controller;

	public static Setting navCo = new Setting("Nav Assist Coeffecient", 7, false);
	public static Setting navVisCo = new Setting("Nav Vision Assist Coeffecient", 5, false);



	
	public Navigation(){
		table = NetworkTableInstance.getDefault().getTable("MAP");
		
		ahrs = new VortxAhrs(SPI.Port.kMXP);

		setPosition(new Position(0,0,0));

    	
		curLeft = Robot.drive.getLeftPosition();
    	curRight = Robot.drive.getRightPosition();
    	prevLeft = curLeft;
		prevRight = curRight;
		
		controller = new PIDController(.05, .001, .02, this, this);
		controller.setInputRange(-180, 180);
		controller.setOutputRange(-.7, .7);
    	controller.setContinuous();
		controller.setAbsoluteTolerance(1);
	}

	public synchronized void setPosition(Position p){
		synchronized(posLock){
			pos = p;
		}
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
		//ystem.out.println("got yaw");

		return ahrs.getYaw();
    }
    
    public void setYaw(double offset) {
    	ahrs.setYaw(offset);
    }

    
    public void zeroYaw(){
		ahrs.zeroYaw();
		System.out.println("Yaw Zerod");
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
		SmartDashboard.putNumber("PID Setpoint", controller.getSetpoint());
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

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		return getYaw();
	}

	@Override
	public void pidWrite(double output) {
		// if(output>=.01) {
		// 	output+=.18;
		// } else if (output<=-.01) {
		// 	output-=.18;
		// }
		output = VortxMath.limit(output, -0.3, .3);
		Robot.drive.setLeftRight(-output, output);
		SmartDashboard.putNumber("PID Output", output);
	}

	public PIDController getController() {
		return controller;
	}
	    
}


package org.usfirst.frc.team3735.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import org.usfirst.frc.team3735.robot.Constants;
import org.usfirst.frc.team3735.robot.Robot;
import org.usfirst.frc.team3735.robot.commands.drive.DDxDrive;
import org.usfirst.frc.team3735.robot.RobotMap;
import org.usfirst.frc.team3735.robot.util.hardware.VortxTalon;
import org.usfirst.frc.team3735.robot.util.settings.BooleanSetting;
import org.usfirst.frc.team3735.robot.util.settings.Setting;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;


public class Drive extends Subsystem {

	private ShuffleboardTab tab = Shuffleboard.getTab("Drive");	
	private NetworkTableEntry rightDistanceEntry = tab.add("Distance Driven Right", 0).withWidget(BuiltInWidgets.kTextView).getEntry();	
	private NetworkTableEntry leftDistanceEntry = tab.add("Distance Driven Left", 0).withWidget(BuiltInWidgets.kTextView).getEntry();
	private NetworkTableEntry leftVelocity = tab.add("Left Velocity", 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min",-75,"max",75)).getEntry();
	private NetworkTableEntry rightVelocity = tab.add("Right Velocity", 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min",-75,"max",75)).getEntry();
	private NetworkTableEntry leftAccel = tab.add("Left Accel", 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min",-75,"max",75)).getEntry();
	private NetworkTableEntry rightAccel = tab.add("Right Accel", 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min",-75,"max",75)).getEntry();
	private NetworkTableEntry leftJerk = tab.add("Left Jerk", 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min",-75,"max",75)).getEntry();
	private NetworkTableEntry rightJerk = tab.add("Right Jerk", 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min",-75,"max",75)).getEntry();
	private NetworkTableEntry leftOutput = tab.add("Right Drive % Output", 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min",0,"max",1)).getEntry();
	private NetworkTableEntry rightOutput = tab.add("Right Drive % Output", 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min",0,"max",1)).getEntry();

	
	public static Setting moveExponent = new Setting("Move Exponent",3);
	public static Setting turnExponent = new Setting("Turn Exponent", 3);
	public static Setting scaledMaxMove = new Setting("Scaled Max Move", .8); //changes speed of motor (IMPORTANT)
	public static Setting scaledMaxTurn = new Setting("Scaled Max Turn", .5); // changes speed of turn motor (IMPORTANT)
	
	private VortxTalon l1;
	
	private VortxTalon r1;
	
	public static BooleanSetting brakeEnabled = new BooleanSetting("Brake Mode On", false) {

		@Override
		public void valueChanged(boolean val) {
			if(Robot.drive != null) {
				Robot.drive.setEnableBrake(val);
				System.out.println("Brake mode " + val);

			}
		}
		
	};
	

	public Drive() {
		l1 = new VortxTalon(RobotMap.Drive.leftMotors, "Left Drive Motors");

		r1 = new VortxTalon(RobotMap.Drive.rightMotors, "Right Drive Motors");
		
		initSensors();
		setEnableBrake(true);
	}
	//
	/*******************************
	 * Default Command For Driving
	 *******************************/
	public void initDefaultCommand() {
		setDefaultCommand(new DDxDrive());
	}

	public void setupDriveForSpeedControl() {
		//setEnableBrake(false);
	}


	public void initSensors() {
		
		l1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);	
		l1.setSelectedSensorPosition(0);
		l1.setSensorPhase(true);
		
		
		r1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		r1.setSelectedSensorPosition(0);
		r1.setSensorPhase(true);
		System.out.println("added sensors");
	}
	
	public void setLeftPeakVoltage(double vol){
		//l1.configPeakOutputVoltage(vol, -vol);
		l1.configPeakOutputForward(vol, 0);
		l1.configPeakOutputReverse(-vol, 0);

	}

	public void setRightPeakVoltage(double vol){
		//r1.configPeakOutputVoltage(vol, -vol);
		r1.configPeakOutputForward(vol, 0);
		r1.configPeakOutputReverse(-vol, 0);
	}


	
	public double getCurrentPercent(){
		return (r1.getMotorOutputPercent() + l1.getMotorOutputPercent())/2;	
	}
	
	/*******************************
	 * Drive Functions
	 *******************************/
	
	/**
	 * Standard arcade drive from wpi's RobotDrive class
	 * @param move
	 * @param rotate
	 */
	public void arcadeDrive(double move, double rotate) {
		//copied from RobotDrive class. essentially lowers the speed of one motor first, rather than increases
		//one and decreases the other at the same time.
		
		double leftMotorSpeed;
		double rightMotorSpeed;
		
		double moveValue = move;
		double rotateValue = rotate;
	    if (moveValue > 0.0) {
	        if (rotateValue < 0.0) {
	          leftMotorSpeed = moveValue + rotateValue;
	          rightMotorSpeed = Math.max(moveValue, -rotateValue);
	        } else {
	          leftMotorSpeed = Math.max(moveValue, rotateValue);
	          rightMotorSpeed = moveValue - rotateValue;
	        }
	      } else {
	        if (rotateValue < 0.0) {
	          leftMotorSpeed = -Math.max(-moveValue, -rotateValue);
	          rightMotorSpeed = moveValue - rotateValue;
	        } else {
	          leftMotorSpeed = moveValue + rotateValue;
	          rightMotorSpeed = -Math.max(-moveValue, rotateValue);
	        }
	      }
	      setLeftRight(leftMotorSpeed, rightMotorSpeed);
	}
	
	
	public void normalDrive(double move, double rotate){
		double rotateValue = rotate;
		setLeftRight(move + rotateValue, move - rotateValue);
	}
	

	/*******************************
	 * Brake Mode
	 *******************************/
	public void setEnableBrake(boolean b) {
		if(b) {
			l1.setNeutralMode(NeutralMode.Brake);
			r1.setNeutralMode(NeutralMode.Brake);
		}else {
			l1.setNeutralMode(NeutralMode.Coast);
			r1.setNeutralMode(NeutralMode.Coast);
		}
		brakeEnabled.setValue(b);
	}
	
	

	public void setLeftRight(double left, double right) {
		l1.set(ControlMode.PercentOutput, left);
		r1.set(ControlMode.PercentOutput, right);
	}
	
double pastLeftInches;
double pastRightInches;	
double pastLeftVel;
double pastRightVel;
double pastLeftAccel;
double pastRightAccel;

	/******************************************
	 * The Logs
	 ******************************************/
	public void log() {
		
		double leftInches = l1.getSelectedSensorPosition()*Constants.Drive.InchesPerTick;
		double rightInches = r1.getSelectedSensorPosition()*Constants.Drive.InchesPerTick;

		// SmartDashboard.putNumber("Left Drive Inches", leftInches);
		// SmartDashboard.putNumber("Right Drive Inches", rightInches);

		rightDistanceEntry.setDouble(rightInches);
		leftDistanceEntry.setDouble(leftInches);



		double leftVelocity = (leftInches - pastLeftInches)/Constants.dt;
		double rightVelocity = (rightInches - pastRightInches)/Constants.dt;
		
		// SmartDashboard.putNumber("Left Drive Velocity", leftVelocity);
		// SmartDashboard.putNumber("Right Drive Velocity", rightVelocity);

		this.leftVelocity.setDouble(leftVelocity);
		this.rightVelocity.setDouble(rightVelocity);


		double leftAccel = (leftVelocity-pastLeftVel)/Constants.dt;
		double rightAccel = (rightVelocity-pastRightVel)/Constants.dt;

		// SmartDashboard.putNumber("Left Drive Acceleration", leftAccel);
		// SmartDashboard.putNumber("Right Drive Acceleration", rightAccel);

		this.leftAccel.setDouble(leftAccel);
		this.rightAccel.setDouble(rightAccel);

		double leftJerk = (leftAccel - pastLeftAccel)/Constants.dt;
		double rightJerk = (rightAccel - pastRightAccel)/Constants.dt;

		// SmartDashboard.putNumber("Left Drive Jerk", leftJerk);
		// SmartDashboard.putNumber("Right Drive Jerk", rightJerk);

		this.leftJerk.setDouble(leftJerk);
		this.rightJerk.setDouble(rightJerk);

		pastRightInches = rightInches;
		pastLeftInches = leftInches;

		pastLeftVel = leftVelocity;
		pastRightVel = rightVelocity;

		pastLeftAccel = leftAccel;
		pastRightAccel = rightAccel;
		
		// SmartDashboard.putNumber("Left Drive Percent Output", l1.getMotorOutputPercent());
		// SmartDashboard.putNumber("Right Drive Percent Output", r1.getMotorOutputPercent());

		this.rightOutput.setDouble(r1.getMotorOutputPercent());
		this.leftOutput.setDouble(l1.getMotorOutputPercent());

		
		//SmartDashboard.putNumber("Left Target", l1.getClosedLoopTarget(0));
		//SmartDashboard.putNumber("Right Target", r1.getClosedLoopTarget(0));

	}

	public void debugLog() {

	}
	public double getLeftPosition() {
		return l1.getSelectedSensorPosition(0);
	}

	public double getRightPosition() {
		return r1.getSelectedSensorPosition(0);
	}

	public double getLeftInches() {
		return getLeftPosition()*Constants.Drive.InchesPerTick;
	}

	public double getRightInches() {
		return getRightPosition() *Constants.Drive.InchesPerTick;
	}
	public void resetEncodersPositions() {
		l1.setSelectedSensorPosition(0);
		r1.setSelectedSensorPosition(0);
	}
	



}


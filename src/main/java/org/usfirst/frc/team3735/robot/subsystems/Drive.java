package org.usfirst.frc.team3735.robot.subsystems;


import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team3735.robot.Constants;
import org.usfirst.frc.team3735.robot.Robot;
import org.usfirst.frc.team3735.robot.commands.drive.DDxDrive;
import org.usfirst.frc.team3735.robot.RobotMap;
import org.usfirst.frc.team3735.robot.util.hardware.VortxTalon;
import org.usfirst.frc.team3735.robot.util.settings.BooleanSetting;
import org.usfirst.frc.team3735.robot.util.settings.Setting;
import org.usfirst.frc.team3735.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;




public class Drive extends Subsystem {

	public static Setting moveExponent = new Setting("Move Exponent",3);
	public static Setting turnExponent = new Setting("Turn Exponent", 3);
	public static Setting scaledMaxMove = new Setting("Scaled Max Move", .8); //changes speed of motor (IMPORTANT)
	public static Setting scaledMaxTurn = new Setting("Scaled Max Turn", .5); // changes speed of turn motor (IMPORTANT)
	
	private VortxTalon l1;
	
	private VortxTalon r1;

	private double leftAddTurn = 0;
	private double rightAddTurn = 0;
	private double visionAssist = 0;
	private double navxAssist = 0;
	
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

		this.setNavxAssist(0);
		this.setVisionAssist(0);
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
	 * Additive setters
	 *******************************/
	public void setLeftTurn(double turn){
    	leftAddTurn = turn;
    }
    public void setRightTurn(double turn){
    	rightAddTurn = turn;
    }
	public void setVisionAssist(double error) {
		visionAssist = (error * Navigation.navVisCo.getValue());
	}	
	public void setNavxAssist(double error) {
		this.navxAssist = (error/180.0) * Navigation.navCo.getValue();
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

		SmartDashboard.putNumber("Left Drive Inches", leftInches);
		SmartDashboard.putNumber("Right Drive Inches", rightInches);

		double leftVelocity = (leftInches - pastLeftInches)/Constants.dt;
		double rightVelocity = (rightInches - pastRightInches)/Constants.dt;
		
		SmartDashboard.putNumber("Left Drive Velocity", leftVelocity);
		SmartDashboard.putNumber("Right Drive Velocity", rightVelocity);

		double leftAccel = (leftVelocity-pastLeftVel)/Constants.dt;
		double rightAccel = (rightVelocity-pastRightVel)/Constants.dt;

		SmartDashboard.putNumber("Left Drive Acceleration", leftAccel);
		SmartDashboard.putNumber("Right Drive Acceleration", rightAccel);

		double leftJerk = (leftAccel - pastLeftAccel)/Constants.dt;
		double rightJerk = (rightAccel - pastRightAccel)/Constants.dt;

		SmartDashboard.putNumber("Left Drive Jerk", leftJerk);
		SmartDashboard.putNumber("Right Drive Jerk", rightJerk);

		pastRightInches = rightInches;
		pastLeftInches = leftInches;

		pastLeftVel = leftVelocity;
		pastRightVel = rightVelocity;

		pastLeftAccel = leftAccel;
		pastRightAccel = rightAccel;
		
		SmartDashboard.putNumber("Left Drive Percent Output", l1.getMotorOutputPercent());
		SmartDashboard.putNumber("Right Drive Percent Output", r1.getMotorOutputPercent());

		
		
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


package org.usfirst.frc.team3735.robot.subsystems;


import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team3735.robot.Constants;
import org.usfirst.frc.team3735.robot.Robot;
import org.usfirst.frc.team3735.robot.commands.drive.DDxDrive;
import org.usfirst.frc.team3735.robot.RobotMap;
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
	
	private WPI_TalonSRX l1;
	private WPI_TalonSRX l2;
	
	private WPI_TalonSRX r1;
	private WPI_TalonSRX r2;
	
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
		l1 = new WPI_TalonSRX(RobotMap.Drive.leftMotor1);
		l2 = new WPI_TalonSRX(RobotMap.Drive.leftMotor2);

		r1 = new WPI_TalonSRX(RobotMap.Drive.rightMotor1);
		r2 = new WPI_TalonSRX(RobotMap.Drive.rightMotor2);
		
		l1.setInverted(true);
		l2.setInverted(true);
		
		setupSlaves();
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



	/*******************************
	 * Slaves Setup
	 *******************************/
	public void setupSlaves() {
		l2.follow(l1);
		r2.follow(r1);
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
			l2.setNeutralMode(NeutralMode.Brake);
			r1.setNeutralMode(NeutralMode.Brake);
			r2.setNeutralMode(NeutralMode.Brake);
		}else {
			l1.setNeutralMode(NeutralMode.Coast);
			l2.setNeutralMode(NeutralMode.Coast);
			r1.setNeutralMode(NeutralMode.Coast);
			r2.setNeutralMode(NeutralMode.Coast);
		}
		brakeEnabled.setValue(b);
	}
	
	

	public void setLeftRight(double left, double right) {
		//System.out.println("Left: " + left + " Right:" + right);
		//l1.set(left); 
		l1.set(ControlMode.PercentOutput, left);
		
		
		//r1.set(-1 * right);
		r1.set(ControlMode.PercentOutput, right);
	}

	


	/******************************************
	 * The Logs
	 ******************************************/
	public void log() {
		SmartDashboard.putNumber("Left Drive Position", l1.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Drive Position", r1.getSelectedSensorPosition(0));
		
		//SmartDashboard.putNumber("Left Drive Velocity", l1.getSelectedSensorVelocity(0));
		//SmartDashboard.putNumber("Right Drive Velocity", r1.getSelectedSensorVelocity(0));
		
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
	public void resetEncodersPositions() {
		l1.setSelectedSensorPosition(0);
		r1.setSelectedSensorPosition(0);
	}
	



}


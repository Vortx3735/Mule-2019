package org.usfirst.frc.team3735.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.usfirst.frc.team3735.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {
	TalonSRX l1;
	TalonSRX r1;
	//
	public Intake()
	{
		super();
		l1 = new TalonSRX(RobotMap.Intake.leftMotor);
		r1 = new TalonSRX(RobotMap.Intake.rightMotor);
	}
	
//
	
	public void setMotor(double d)
	{
		
		l1.set(ControlMode.PercentOutput, d);
		r1.set(ControlMode.PercentOutput, -d);
	}

	@Override
	protected void initDefaultCommand() {
		
	}
	
	
}

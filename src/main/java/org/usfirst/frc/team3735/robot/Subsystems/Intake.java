package org.usfirst.frc.team3735.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import org.usfirst.frc.team3735.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {

	WPI_VictorSPX motor;

	//
	public Intake()	{
		super();
		motor = new WPI_VictorSPX(RobotMap.Intake.motor);

	}
	
//
	
	public void setMotor(double d) {
		motor.set(ControlMode.PercentOutput, d);
	}

	@Override
	protected void initDefaultCommand() {
		
	}
	
	
}

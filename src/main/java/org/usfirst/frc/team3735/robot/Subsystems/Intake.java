package org.usfirst.frc.team3735.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {
	TalonSRX l1;
	TalonSRX R1;
	//
	public Intake()
	{
		super();
		l1 = new TalonSRX(4);
		R1 = new TalonSRX(5);
	}
	
//
	
	public void setMotor(double d)
	{
		
		l1.set(ControlMode.PercentOutput, d);
		R1.set(ControlMode.PercentOutput, -d);
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
	
	
}

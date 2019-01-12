package org.usfirst.frc.team3735.robot.subsystems;

import org.usfirst.frc.team3735.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Hatch extends Subsystem {

	Solenoid solenoid;
	//Solenoid solenoid2;
	public Hatch()
	{
		super();
		solenoid = new Solenoid(RobotMap.Hatch.solenoid);
		//solenoid2 = new Solenoid(RobotMap.Hatch.solenoid2);
	}
	
//
	
	public void setSolenoid(boolean b)
	{
		solenoid.set(b);
		//solenoid2.set(b);
	}

	@Override
	protected void initDefaultCommand() {
		
	}
	
	
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3735.robot;


import org.usfirst.frc.team3735.robot.util.oi.XboxController;
import org.usfirst.frc.team3735.robot.commands.drive.profiling.DriveToTargetP;
import org.usfirst.frc.team3735.robot.commands.drive.profiling.DriveToTargetPID;
import org.usfirst.frc.team3735.robot.commands.drive.profiling.FollowTarget;
import org.usfirst.frc.team3735.robot.commands.drive.profiling.MoveToTarget;
import org.usfirst.frc.team3735.robot.commands.limelight.FlipPipeline;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public XboxController main;
	public XboxController co;
	
	public OI() {

		main = new XboxController(0);
		co = new XboxController(1);
		main.rb.get();
		main.x.whileHeld(new FollowTarget());
		main.y.whileHeld(new MoveToTarget());
		main.b.whileHeld(new DriveToTargetPID());
		main.a.whileHeld(new DriveToTargetP());

		main.lb.whenPressed(new FlipPipeline());
	}
	//
	public double getDriveMove() {
		return (main.getRightTrigger() - main.getLeftTrigger());
		//return main.getLeftY();
	}

	public double getDriveTurn() {
		return -1*main.getLeftX();
		//return main.getRightX();
	}
	
	public double getFODMag() {
		//return main.getRightMagnitude();
		return 0;
	}
	
	public double getFODAngle(){
		//return main.getRightAngle()
		return 0;
	}
	
}

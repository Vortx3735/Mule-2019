package org.usfirst.frc.team3735.robot;

import org.usfirst.frc.team3735.robot.commands.auto.DistanceStraight;
import org.usfirst.frc.team3735.robot.commands.drive.FollowTarget;

import edu.wpi.first.wpilibj.command.Command;

public class Autonomous {

	private Command firstCommand;

	public Autonomous() {
		//firstCommand = new DistanceStraight(100);
		firstCommand = new FollowTarget();
		System.out.println("new autonomous created");
	}

	public void startCommand() {
		System.out.println("startCommand called");
		firstCommand.start();
	}

	public void cancel() {
	
	}

}

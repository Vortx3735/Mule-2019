package org.usfirst.frc.team3735.robot;

import org.usfirst.frc.team3735.robot.commands.auto.MeterStraight;

import edu.wpi.first.wpilibj.command.Command;

public class Autonomous {

	private Command firstCommand;

	public Autonomous() {
		firstCommand = new MeterStraight();
		System.out.println("new autonomous created");
	}

	public void startCommand() {
		System.out.println("startCommand called");
		firstCommand.start();
	}

	public void cancel() {
	
	}

}

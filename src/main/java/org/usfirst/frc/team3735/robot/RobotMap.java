/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3735.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static class Drive{
		public static int leftMotor1 =		3; //3
		public static int leftMotor2 = 		6; //6
		
		public static int rightMotor1 = 	1; //1
		public static int rightMotor2 = 	8; //8

	}

	public static class Intake {
		public static int leftMotor = 4;
		public static int rightMotor = 5;
	}

	public static class Hatch {
		public static int solenoid = 4;
	}
}

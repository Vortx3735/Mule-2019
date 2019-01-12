/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3735.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
//import com.sun.org.apache.xpath.internal.operations.Bool;

import org.usfirst.frc.team3735.robot.Robot;
import org.usfirst.frc.team3735.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class Pivot extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  Solenoid pivot;
  
  public Pivot()
  {
    pivot = new Solenoid(RobotMap.Hatch.pivot);
    
  }

  public void setPivot(Boolean b)
  {
    pivot.set(b);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}

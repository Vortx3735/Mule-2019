/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3735.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class ArduinoCo extends Subsystem {

  double distance;
  Thread nThread;
  public static SerialPort sp;
  //TODO: Make a tv for the arudino

  public ArduinoCo() {
    sp = new SerialPort(9600, Port.kUSB);
  }

  public double getDistance() {
    return distance;
  }

  public void update() {
    // System.out.println("Update was called");

    try {
      if (sp.getBytesReceived() > 0) {
        String[] input = sp.readString().trim().split("\n");

        if (!input[0].equals("")) {
          distance = Integer.parseInt(input[input.length - 1])/25.4;
        }
      }
    } catch (NumberFormatException e) {
    }

  }


  public void log() {
    SmartDashboard.putNumber("Arduino Distance", distance);
  }
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}

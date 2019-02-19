package org.usfirst.frc.team3735.robot.commands.hatch;

import org.usfirst.frc.team3735.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
//
/**
 *
 */
public class HatchSet extends Command {
    
    double speed;
    
    public HatchSet(double speed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        this.speed = speed;
    	requires(Robot.hatch);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.hatch.setSpeed(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.hatch.setSpeed(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
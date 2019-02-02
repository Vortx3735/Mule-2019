package org.usfirst.frc.team3735.robot.commands.drive;

import org.usfirst.frc.team3735.robot.Robot;
import org.usfirst.frc.team3735.robot.util.calc.VortxMath;
import org.usfirst.frc.team3735.robot.util.settings.Func;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class FollowTarget extends Command {
	
	// private double finishTime = .3;
	// private double timeOnTarget = 0;

	Func getAngle;
	int count;
	
	public FollowTarget() {
    	this(new Func(){
			@Override
			public double getValue() {
				return VortxMath.navLimit(Robot.navigation.getYaw()+Robot.limelight.getTx());
			}
    	});
    }

	public FollowTarget(Func fun) {
    	requires(Robot.drive);
		requires(Robot.navigation);
		requires(Robot.limelight);
		getAngle = fun;
		count = 0;
	}
	

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.navigation.getController().setSetpoint(getAngle.getValue());
    	Robot.navigation.getController().enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {   
		count++;
		if(count%5==0) {
			System.out.println("Tx is " + Robot.limelight.getTx());
			System.out.println("Current angle is " + Robot.navigation.getYaw());
			System.out.println("Set point is " + Robot.navigation.getYaw()+Robot.limelight.getTx());
			Robot.navigation.getController().setSetpoint(getAngle.getValue());	
		} 
    	// if(Robot.navigation.getController().onTarget()){
    	// 	timeOnTarget += .02;
    	// }else{
    	// 	timeOnTarget = 0;
    	// }
    }

	// Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
		return false;
		//return timeOnTarget >= finishTime;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.navigation.getController().disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }

}
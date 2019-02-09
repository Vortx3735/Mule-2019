package org.usfirst.frc.team3735.robot.assists;

import org.usfirst.frc.team3735.robot.Robot;
import org.usfirst.frc.team3735.robot.util.cmds.ComAssist;

public class LimelightAssist extends ComAssist{
	
	private double prevWorking = 0;

	public LimelightAssist(){
		requires(Robot.limelight);
	}

	@Override
	public void initialize() {
	}

	@Override
	public void execute() {
		Double in = Robot.limelight.getTx();
    	if(in == null){
    		Robot.drive.setVisionAssist(0);
    	}else{
    		prevWorking = in;
        	Robot.drive.setVisionAssist(in * .0025);
    	}
	}
	
	
}
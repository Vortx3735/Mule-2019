package org.usfirst.frc.team3735.robot.commands.auto;

import org.usfirst.frc.team3735.robot.commands.drive.profiling.PathFollower;

import edu.wpi.first.wpilibj.command.CommandGroup;
import jaci.pathfinder.Waypoint;

public class MiddleRightHatch extends CommandGroup{


    public MiddleRightHatch() {
        Waypoint[] waypoints = new Waypoint[2];
        waypoints[0] = new Waypoint(0, 0, 0);
        waypoints[1] = new Waypoint(160, 20, 0);

        addSequential(new PathFollower(waypoints));

        //addSequential(new DriveToTargetP(), 2);
    }
}
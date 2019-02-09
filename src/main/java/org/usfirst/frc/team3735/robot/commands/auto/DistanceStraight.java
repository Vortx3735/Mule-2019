package org.usfirst.frc.team3735.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team3735.robot.commands.drive.profiling.*;
import jaci.pathfinder.Waypoint;

public class DistanceStraight extends CommandGroup{

    public DistanceStraight(double d) {
        System.out.println("Meter straight called");
        Waypoint[] waypoints = new Waypoint[2];
        waypoints[0] = new Waypoint(0, 0, 0);
        waypoints[1] = new Waypoint(100, 20, 0);
        System.out.println("Creating new waypoints");
        addSequential(new PathFollower(waypoints));
    }

}
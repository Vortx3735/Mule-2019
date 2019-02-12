package org.usfirst.frc.team3735.robot.commands.drive.profiling;

import org.usfirst.frc.team3735.robot.Robot;
import org.usfirst.frc.team3735.robot.Constants;
import org.usfirst.frc.team3735.robot.util.cmds.VortxCommand;
import org.usfirst.frc.team3735.robot.util.profiling.Location;
import org.usfirst.frc.team3735.robot.util.profiling.Position;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;


public class PathFollower extends VortxCommand {

    public EncoderFollower lFollower;
    public EncoderFollower rFollower;

    // public DistanceFollower lFollower;
    // public DistanceFollower rFollower;

    public Trajectory leftTraj;
    public Trajectory rightTraj;

    public double left;
    public double right;
    public double angle;
    public double desiredAngle;
    public double angleDifference;
    public double turn;

    //@param: array of waypoint that have (x,y,0)
    public PathFollower(Waypoint[] waypoints) {
        System.out.println("Pathfollwer called");
        long startTime = System.currentTimeMillis();

        //FitMethod fit, int samples, double dt, double max_velocity, double max_acceleration, double max_jerk
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, //Fit method
        Trajectory.Config.SAMPLES_FAST //How many samples to use to refine the path (higher = smoother, lower = faster)
        , Constants.dt, Constants.Drive.maxVelocity, Constants.Drive.maxAccel, Constants.Drive.maxJerk); // dt, max_velocity,  max_acceleration,  max_jerk

        Trajectory trajectory = Pathfinder.generate(waypoints, config);

        // Wheelbase Width = 0.5m
        TankModifier modifier = new TankModifier(trajectory).modify(Constants.Drive.wheelBase); //wheel base in meters

        // Do something with the new Trajectories...
        leftTraj = modifier.getLeftTrajectory();
        rightTraj = modifier.getRightTrajectory();

        lFollower = new EncoderFollower(leftTraj);
        rFollower = new EncoderFollower(rightTraj);

        
        lFollower.configureEncoder((int)(Math.round(Robot.drive.getLeftPosition())), Constants.Drive.ticksPerRotation, Constants.Drive.wheelDiam);
        rFollower.configureEncoder((int)(Math.round(Robot.drive.getRightPosition())), Constants.Drive.ticksPerRotation, Constants.Drive.wheelDiam);

        lFollower.configurePIDVA(.0005, 0, 0, 1/Constants.Drive.maxVelocity, 1/Constants.Drive.maxAccel*.01);//1/Constants.Drive.maxAccel*.08);
        rFollower.configurePIDVA(.0005, 0, 0, 1/Constants.Drive.maxVelocity, 1/Constants.Drive.maxAccel*.01);//1/Constants.Drive.maxAccel*.08);

        long timeTake = System.currentTimeMillis()-startTime;

        System.out.println("Set trajectories in " + timeTake  + "millis");
        for(int i=0; i<leftTraj.length(); i++) {
            System.out.println(i + "Left pos " + leftTraj.segments[i].x + " right pos: " + rightTraj.segments[i].x + " angle wanted: " + leftTraj.segments[i].heading);
        }

        Robot.navigation.resetPosition(new Position(new Location(0,0), 0));

        requires(Robot.drive);            
        
    }

    @Override
    protected void execute () {
            left = lFollower.calculate((int)Robot.drive.getLeftPosition()); 
            right = rFollower.calculate((int)Robot.drive.getRightPosition());

            angle = Robot.navigation.getYaw();
            desiredAngle = Pathfinder.r2d(lFollower.getHeading());
            angleDifference = Pathfinder.boundHalfDegrees(desiredAngle - angle);

            turn = .9 * (1.0/80) * angleDifference;

            System.out.println(" Left: " + Robot.drive.getLeftInches() + " Right: " + Robot.drive.getRightInches() + " Turn: " + turn);


            Robot.drive.setLeftRight((left-turn), (right+turn));
    }

    

    @Override
    protected boolean isFinished() {
        return rFollower.isFinished() && lFollower.isFinished();
    }

}
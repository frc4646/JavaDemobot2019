package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class TrackReflectiveTape extends Command
{
    public TrackReflectiveTape() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
        requires(Robot.m_visionSystem);
        requires(Robot.m_drivetrain); //For following track
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        Robot.m_visionSystem.trackVisionTarget();

        //TODO: Make the code below into a method  in the subsystem for abstraction purposes.
        double drivesped = Robot.m_visionSystem.returnPanValue(0)/90.0;
        if (Math.abs(drivesped) > 0.15)
        {
            Robot.m_drivetrain.driveByPercent(-drivesped,drivesped);
        }
        else if (Robot.m_visionSystem.returnWidthValue() < 100 && Robot.m_visionSystem.returnWidthValue() != 0)
        {
            Robot.m_drivetrain.driveByPercent(0.3,0.3);
        }
        else
        {
            Robot.m_drivetrain.driveByPercent(0, 0);
        }
        if (Robot.m_visionSystem.returnWidthValue() % 10 == 0)
        {
            System.out.println("Width: " + Robot.m_visionSystem.returnWidthValue() + "\n"); //Usually returns 20-90
            System.out.println("Height: " + Robot.m_visionSystem.returnHeightValue() + "\n");
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
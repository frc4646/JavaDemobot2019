/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class FaceAngle extends Command {

  int desiredAngle;

  public FaceAngle(int angle) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.m_drivetrain);
    desiredAngle = angle;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double gyroAngle = Robot.m_drivetrain.getAngle();

    if (gyroAngle < desiredAngle) //If gyro angle is less than the wanted angle:
    {
      Robot.m_drivetrain.driveByPercent(0.5, -0.5); //Turn left
    }
    else if (gyroAngle > desiredAngle) //If gyro angle is less than the wanted angle:
    {
      Robot.m_drivetrain.driveByPercent(-0.5, 0.5); //Turn right
    }

    //TODO: Implement a range of values and cleanup. Also return check for isFinished.
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
  }
}

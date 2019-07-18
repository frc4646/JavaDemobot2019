/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;
   
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class DrivetrainSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

//  final VictorSPX frontLeftDrive;
//  final TalonSRX frontRightDrive;
//  final TalonSRX backLeftDrive;
//  final VictorSPX backRightDrive;
  final AnalogGyro gyro;
  final Encoder driveEncoder;
  final int encoderCountsPerInch = 0;

  public DrivetrainSubsystem()
  {/*
    frontLeftDrive = new VictorSPX(RobotMap.frontLeftDrivePort);
    frontRightDrive = new TalonSRX(RobotMap.frontRightDrivePort);
    backLeftDrive = new TalonSRX(RobotMap.backLeftDrivePort);
    backRightDrive = new VictorSPX(RobotMap.backRightDrivePort);

    frontLeftDrive.setInverted(true);
    backLeftDrive.setInverted(true);
  */
    gyro = new AnalogGyro(RobotMap.analogGyroPort);

    driveEncoder = new Encoder(RobotMap.driveEncoderA, RobotMap.driveEncoderB);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    //setDefaultCommand(new GamepadDriveTeleOp());
  }

  public void driveByPercent(double leftSpeed, double rightSpeed)
  {/*
      frontLeftDrive.set(ControlMode.PercentOutput, leftSpeed);
      frontRightDrive.set(ControlMode.PercentOutput, rightSpeed);
      backLeftDrive.set(ControlMode.PercentOutput, leftSpeed);
      backRightDrive.set(ControlMode.PercentOutput, rightSpeed);
*/
  }

  public void resetGyro()
  {
    gyro.calibrate();
  }

  public double getAngle()
  {
    return gyro.getAngle();
  }

  public void resetDriveEncoderCount()
  {
    driveEncoder.reset();
  }

  public double getDriveEncoderCount()
  {
    return driveEncoder.get();
  }
}

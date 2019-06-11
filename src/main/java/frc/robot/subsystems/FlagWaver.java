/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.Spark;
import frc.robot.commands.FlagWaveTeleop;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class FlagWaver extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private final Spark flagSpark;
  private final double flagSpeed;

  public FlagWaver()
  {
    flagSpark = new Spark(RobotMap.flagMotorPort);
    flagSpeed = 0.25;
  }


  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    setDefaultCommand(new FlagWaveTeleop());
  }

  public void setMotorSpeed(double speed)
  {
    flagSpark.setSpeed(speed);
  }

  public void flagRun(double flagSpeed, boolean flagStartButton, boolean flagStopButton)
  {
    boolean shouldFlagWave = false;

    if (flagStartButton)
    {
      shouldFlagWave = true;
    }
    else if (flagStopButton)
    {
      shouldFlagWave = false;
    }

    if (shouldFlagWave)
    {
      setMotorSpeed(flagSpeed);
    }
    else if (!shouldFlagWave)
    {
      setMotorSpeed(0.0);
    }
  }
}
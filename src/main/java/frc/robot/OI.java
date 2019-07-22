/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import frc.robot.commands.FlagWave;
import frc.robot.commands.FlagStop;

public class OI 
{
  public OI()
  {
    // Put button mapped commands here.
    // Example: Robot.m_IO.mechButton1.whenPressed(new exampleCommand1());

    Robot.m_io.aButton.whenPressed(new FlagWave());
    Robot.m_io.bButton.whenPressed(new FlagStop());
  }
}

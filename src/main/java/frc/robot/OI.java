/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import frc.robot.commands.FlagWave;
import frc.robot.commands.FlagStop;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.FaceAngle;
//import frc.robot.commands.TrackReflectiveTape;

public class OI 
{
  public OI()
  {
    // Put button mapped commands here.
    // Example: Robot.m_IO.mechButton1.whenPressed(new exampleCommand1());

    Robot.m_io.aButton.whenPressed(new FlagWave());
    Robot.m_io.bButton.whenPressed(new FlagStop());
    //Robot.m_io.xButton.whenPressed(new TrackReflectiveTape());
    if (Robot.m_io.getDpadClicked() != -1) new ResetGyro();
    if (Robot.m_io.getDpadClicked() == 0) new FaceAngle(0);
    if (Robot.m_io.getDpadClicked() == 45) new FaceAngle(45);
    if (Robot.m_io.getDpadClicked() == 90) new FaceAngle(90);
    if (Robot.m_io.getDpadClicked() == 135) new FaceAngle(135);
    if (Robot.m_io.getDpadClicked() == 180) new FaceAngle(180);
    if (Robot.m_io.getDpadClicked() == 225) new FaceAngle(225);
    if (Robot.m_io.getDpadClicked() == 270) new FaceAngle(270);
    if (Robot.m_io.getDpadClicked() == 315) new FaceAngle(315);
  }
}

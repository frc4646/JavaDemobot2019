//
// begin license header
//
// This file is part of Pixy CMUcam5 or "Pixy" for short
//
// All Pixy source code is provided under the terms of the
// GNU General Public License v2 (http://www.gnu.org/licenses/gpl-2.0.html).
// Those wishing to use Pixy source code, software and/or
// technologies under different licensing terms should contact us at
// cmucam@cs.cmu.edu. Such licensing terms are available for
// all portions of the Pixy codebase presented here.
//
// end license header
//
package frc.robot.pixy2;

//#include <TPixy2.h>
import frc.robot.pixy2.Pixy2; //This is the Java version of TPixy2.h

public class PIDLoop
{
    private static final int PID_MAX_INTEGRAL = 2000;
    private static final int ZUMO_BASE_DEADBAND = 20;

    public int m_command; //Roba: was private

    public int m_pgain;
    public int m_igain;
    public int m_dgain;
    
    public int m_prevError;
    public int m_integral;
    public boolean m_servo;

  public PIDLoop(int pgain, int igain, int dgain, boolean servo)
  {
    m_pgain = pgain;
    m_igain = igain;
    m_dgain = dgain;
    m_servo = servo;

    reset();
  }

  public void reset() //Roba: was private
  {
    if (m_servo)
      m_command = Pixy2.PIXY_RCS_CENTER_POS;
    else
      m_command = 0;
      
    m_integral = 0;
    m_prevError = 0x80000000;
  }  
  
  public void update(int error) //Roba: was private
  {
    int pid;
  
    if (m_prevError!=0x80000000)
    { 
      // integrate integral
      m_integral += error;
      // bound the integral
      if (m_integral>PID_MAX_INTEGRAL)
        m_integral = PID_MAX_INTEGRAL;
      else if (m_integral<-PID_MAX_INTEGRAL)
        m_integral = -PID_MAX_INTEGRAL;

      // calculate PID term
      pid = (error*m_pgain + ((m_integral*m_igain)>>4) + (error - m_prevError)*m_dgain)>>10;
    
      if (m_servo)
      {
        m_command += pid; // since servo is a position device, we integrate the pid term
        if (m_command>Pixy2.PIXY_RCS_MAX_POS) 
          m_command = Pixy2.PIXY_RCS_MAX_POS; 
        else if (m_command<Pixy2.PIXY_RCS_MIN_POS) 
          m_command = Pixy2.PIXY_RCS_MIN_POS;
      }
      else
      {
        // Deal with Zumo base deadband
        if (pid>0)
          pid += ZUMO_BASE_DEADBAND;
        else if (pid<0)
          pid -= ZUMO_BASE_DEADBAND;
         m_command = pid; // Zumo base is velocity device, use the pid term directly  
      }
    }

    // retain the previous error val so we can calc the derivative
    m_prevError = error; 
  }
};

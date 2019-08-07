/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.pixy2.Pixy2;
import frc.robot.commands.TrackReflectiveTape;
import edu.wpi.first.wpilibj.Servo;
import frc.robot.pixy2.PIDLoop;
import frc.robot.pixy2.Pixy2CCC.Block;

//README: For those who are wondering at what numBlocks are
//in the previous C++ code, Roba has discovered it is 
//the same as numBlockspixy.getCCC().getBlocks().size().
//Your welcome.

public class VisionSystem extends Subsystem {

    private Pixy2 pixy;
    private Servo panServo;
    private Servo tiltServo;
    private int width;
    private int height;
    private PIDLoop panLoop;
    private PIDLoop tiltLoop;
    private short index; //short is the java version of int16_t

    public VisionSystem()
    {
        pixy = new Pixy2();
        panServo = new Servo(RobotMap.pixyPanPort);
        tiltServo = new Servo(RobotMap.pixyTiltPort);
        pixy.init();
        index -= 1;
        panLoop = new PIDLoop(400, 0, 400, true);
        tiltLoop = new PIDLoop(500, 0, 500, true);
        System.out.println("Pixy initialized successfully!");
        panServo.set(.5);
        tiltServo.set(.5);
        pixy.changeProg("color_connected_components".toCharArray());

    }

    @Override
    public void initDefaultCommand()
    {
        setDefaultCommand(new TrackReflectiveTape());
    }

    public Pixy2 getPixy(){
        return pixy;
    }

    // Take the blockNum-th block (blocks[blockNum]) that's been around for at least 30 frames (1/2 second)
    // and return its index, otherwise return -1
    private short acquireBlock(int blockNum)
    {
        if (pixy.getCCC().getBlocks().size() > blockNum && pixy.getCCC().getBlocks().get(blockNum).getAge()>30)
            return (short)pixy.getCCC().getBlocks().get(blockNum).getIndex();

        else return -1;
    }

    private Block trackBlock(short ind)
    {
        byte i;

        for (i=0; i<pixy.getCCC().getBlocks().size(); i++)
        {
            if (ind==pixy.getCCC().getBlocks().get(i).getIndex())
            return pixy.getCCC().getBlocks().get(i);
        }
        return null;
    }

    public void trackOrangeBall() {
        // use ccc program to track objects
        Block block = null;
        int panOffset, tiltOffset;
        
        
        pixy.getCCC().getBlocks(false, 2, 1);
        if (index == -1){
            index = acquireBlock(0);
        }
        // If we've found a block, find it, track it
        if (index>=0)
          block = trackBlock(index);
        
        if (block != null)
        {   
          panOffset = (int)pixy.getFrameWidth()/2 - (int)block.getX();
          tiltOffset = (int)block.getY() - (int)pixy.getFrameHeight()/2;  
      
          panLoop.update(panOffset);
          tiltLoop.update(tiltOffset);
        
          panServo.set(panLoop.m_command/1000.0);
          tiltServo.set(tiltLoop.m_command/1000.0);
    
          block.print();

          width = block.getWidth();
          height = block.getHeight();
        }
        else // no object detected, go into reset state
        {
          panLoop.reset();
          tiltLoop.reset();
          panServo.set(0.5);
          tiltServo.set(0.5);
          index = -1;
          width = -1; //int cannot be NULL in java
          height = -1; //int cannot be NULL in java
        }
    }
    
    public void trackVisionTarget() {
      // use ccc program to track objects
      //two pieces of tape to track
      Block block1 = null;
      Block block2 = null;
      int panOffset, tiltOffset;
      
      pixy.getCCC().getBlocks(false, 1, 2);
      if (index == -1){
        index = acquireBlock(0);
      }
      // If we've found the first block, find it, track it
      if (index>=0)
        block1 = trackBlock(index);
      
        index = acquireBlock(1);
      // After we've found the second block, find it, track it
      if (index>=0)
        block2 = trackBlock(index);
      
      if (block1 != null && block2 != null)
      {   
        panOffset = (int)pixy.getFrameWidth()/2 - ((int)block1.getX() + (int)block2.getX())/2;
        tiltOffset = (int)block1.getY() - (int)pixy.getFrameHeight()/2;  
    
        panLoop.update(panOffset);
        tiltLoop.update(tiltOffset);
      
        panServo.set(panLoop.m_command/1000.0);
        tiltServo.set(tiltLoop.m_command/1000.0);
    
        width = block1.getWidth();
        height = block1.getHeight();
      }
      else // no object detected, go into reset state
      {
        panLoop.reset();
        tiltLoop.reset();
        panServo.set(0.5);
        tiltServo.set(0.5);
        index = -1;
        width = -1; //int cannot be NULL in java
        height = -1;  //int cannot be NULL in java
      }
    }
    
    
    public double returnPanValue(int heading)
    {
      return panServo.getAngle() + heading;
    }

    public double returnTiltValue(int heading)
    {
      return tiltServo.getAngle() + heading;
    }
    
    public int returnWidthValue()
    {
      return width;
    }
    
    public int returnHeightValue()
    {
      return height;
    }
}
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.auto.commands;

import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class DriveStraight extends Command {

  double distance;
  boolean isEnabled;
  Drive drive;
  private PIDSubsystem distancePID, turnPID;
  double speed, turn;
  
  public DriveStraight() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  public DriveStraight(Drive drive, double distance) {
    this.drive = drive;
    this.distance = distance;

    distancePID = new PIDSubsystem(0.003, 0, 0.017) {
    
      @Override
      protected void initDefaultCommand() {
        
      }
    
      @Override
      protected void usePIDOutput(double output) {
        speed = output;
      }
    
      @Override
      protected double returnPIDInput() {
        return drive.getAverage();
        // return 0;
      }
  
      @Override
      public void enable() {
        //Enabled PID
        super.enable();
        //Set enabled variable to true
        isEnabled = true;
      }
  
      @Override
      public void disable() {
        //Disable PID
        super.disable();
        //Set enabled variable to false
        isEnabled = false;
      }
    };

    turnPID = new PIDSubsystem(-0.0195, -0.0, -0.06) {
    
      @Override
      protected void initDefaultCommand() {
        
      }
    
      @Override
      protected void usePIDOutput(double output) {
        turn = output;
      }
    
      @Override
      protected double returnPIDInput() {
        return drive.getNavX();
        // return 0;
      }
  
      @Override
      public void enable() {
        //Enabled PID
        super.enable();
        //Set enabled variable to true
        isEnabled = true;
      }
  
      @Override
      public void disable() {
        //Disable PID
        super.disable();
        //Set enabled variable to false
        isEnabled = false;
      }
    };
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    drive.encoderReset();
    distancePID.setOutputRange(-1,1);
    distancePID.setAbsoluteTolerance(30);

    turnPID.setOutputRange(-1, 1);
    // turnPID.setAbsoluteTolerance(2);

    distancePID.setSetpoint(distance);
    turnPID.setSetpoint(drive.getNavX());

    distancePID.enable();
    turnPID.enable();
    
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    drive.arcadeDrive(speed, turn, false);
    drive.encoderPrint();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return distancePID.onTarget();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() { 
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    distancePID.disable();
  }
}

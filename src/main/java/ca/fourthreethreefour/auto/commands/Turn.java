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

public class Turn extends Command {

  double angle;
  boolean isEnabled;
  Drive drive;
  private PIDSubsystem turnPID;
  double turn;

  public Turn() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  public Turn(Drive drive, double angle) {
    this.drive = drive;
    this.angle = angle;

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
    
    turnPID.setOutputRange(-1, 1);
    // turnPID.setAbsoluteTolerance(2);

    turnPID.setSetpoint(angle);
    turnPID.enable();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    drive.arcadeDrive(0, turn, false);
    drive.encoderPrint();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return turnPID.onTarget();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    turnPID.disable();
    drive.arcadeDrive(0, 0, false);
    drive.encoderReset();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    turnPID.disable();
  }
}

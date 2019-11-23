/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.auto.commands;

import ca.fourthreethreefour.teleop.Teleop;
import ca.fourthreethreefour.teleop.subsystems.Drive;
import ca.fourthreethreefour.teleop.subsystems.Encoder;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class DriveStraight extends Command {

  double distance;
  Encoder encoder;
  boolean isEnabled;
  Drive drive;
  private PIDSubsystem distancePID;
  double speed;
  

  public DriveStraight() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  public DriveStraight(Drive drive, Encoder encoder, double distance) {
    this.drive = drive;
    this.distance = distance;
    this.encoder = encoder;

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
        return Encoder.getAverage();
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
    System.out.println("hit");
    distancePID.setOutputRange(-0.5,0.5);
    distancePID.setAbsoluteTolerance(30);
    Encoder.reset();

    distancePID.setSetpoint(distance);
    distancePID.enable();
    
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Drive.extArcadeDrive(speed, 0);
    Encoder.print();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return distancePID.onTarget();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    distancePID.disable();
    Drive.extArcadeDrive(0, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    distancePID.disable();
  }
}

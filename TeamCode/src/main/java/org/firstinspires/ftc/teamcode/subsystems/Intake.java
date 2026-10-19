package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.pedropathing.ivy.Command;
import static com.pedropathing.ivy.commands.Commands.instant;

public class Intake {

  // Names must match the robot configuration exactly
  private static final String MAIN_NAME  = "intakemain";
  private static final String LEFT_NAME  = "leftintake";
  private static final String RIGHT_NAME = "rightintake";

  // Power levels (0 to 1). Main starts below full; raise it after testing if needed.
  public static double MAIN_POWER  = 0.8;
  public static double SERVO_POWER = 1.0;

  private final DcMotorEx main;
  private final CRServo left;
  private final CRServo right;

  public Intake(HardwareMap hardwareMap) {
    main  = hardwareMap.get(DcMotorEx.class, MAIN_NAME);
    left  = hardwareMap.get(CRServo.class, LEFT_NAME);
    right = hardwareMap.get(CRServo.class, RIGHT_NAME);

    // From Intake Test: right servo spins out on positive power, so flip it.
    // After this, positive power = "in" for all three parts.
    right.setDirection(DcMotorSimple.Direction.REVERSE);

    stop();
  }

  // ---- Simple actions (usable in TeleOp) ----

  public void in() {
    main.setPower(MAIN_POWER);
    left.setPower(SERVO_POWER);
    right.setPower(SERVO_POWER);
  }

  public void out() {
    main.setPower(-MAIN_POWER);
    left.setPower(-SERVO_POWER);
    right.setPower(-SERVO_POWER);
  }

  public void stop() {
    main.setPower(0);
    left.setPower(0);
    right.setPower(0);
  }

  // ---- Ivy commands (usable in autos) ----

  /** Main intake motor speed in encoder ticks per second (motor shaft). */
  public double getMainTicksPerSec() {
    return main.getVelocity();
  }
  public Command inCommand() {
    return instant(this::in);
  }

  public Command outCommand() {
    return instant(this::out);
  }

  public Command stopCommand() {
    return instant(this::stop);
  }
}
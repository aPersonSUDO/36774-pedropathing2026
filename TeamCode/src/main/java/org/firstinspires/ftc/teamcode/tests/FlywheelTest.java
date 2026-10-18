package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Flywheel Test")
public class FlywheelTest extends OpMode {

  // Must match the robot configuration exactly (check capitalization)
  private static final String FLYWHEEL_NAME = "Flywheel";

  private DcMotorEx flywheel;
  private double power = 0.3;          // starting test power
  private boolean prevUp = false;
  private boolean prevDown = false;
  private double maxSeen = 0;

  @Override
  public void init() {
    flywheel = hardwareMap.get(DcMotorEx.class, FLYWHEEL_NAME);
    flywheel.setDirection(DcMotor.Direction.REVERSE);
    flywheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
  }

  @Override
  public void loop() {
    // D-pad up/down: change power by 0.1 per press (0.0 to 1.0)
    if (gamepad1.dpad_up && !prevUp) power = Math.min(1.0, power + 0.01);
    if (gamepad1.dpad_down && !prevDown) power = Math.max(0.0, power - 0.01);
    prevUp = gamepad1.dpad_up;
    prevDown = gamepad1.dpad_down;

    // Hold Cross (A) to spin at the chosen power
    boolean running = gamepad1.a;
    flywheel.setPower(running ? power : 0);

    double velocity = flywheel.getVelocity(); // encoder ticks per second
    if (Math.abs(velocity) > Math.abs(maxSeen)) maxSeen = velocity;

    telemetry.addData("Power (D-pad up/down)", "%.2f", power);
    telemetry.addData("Running (hold Cross)", running);
    telemetry.addData("Velocity (ticks/sec)", "%.0f", velocity);
    telemetry.addData("Max velocity seen", "%.0f", maxSeen);
    telemetry.update();
  }

  @Override
  public void stop() {
    flywheel.setPower(0);
  }
}
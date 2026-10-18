package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Transfer Test")
public class TransferTest extends OpMode {

  // Must match the robot configuration exactly
  private static final String TRANSFER_NAME = "transfer";

  private DcMotorEx transfer;
  private double speed = 0.5;          // default 50%
  private boolean prevUp = false;
  private boolean prevDown = false;

  @Override
  public void init() {
    transfer = hardwareMap.get(DcMotorEx.class, TRANSFER_NAME);
    transfer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  }

  @Override
  public void loop() {
    // D-pad up/down: change speed by 10% per press (10% to 100%)
    if (gamepad1.dpad_up && !prevUp) speed = Math.min(1.0, speed + 0.1);
    if (gamepad1.dpad_down && !prevDown) speed = Math.max(0.1, speed - 0.1);
    prevUp = gamepad1.dpad_up;
    prevDown = gamepad1.dpad_down;

    // Hold Cross (A) = toward flywheel, hold Circle (B) = reverse
    double power = 0;
    if (gamepad1.a) power = speed;
    if (gamepad1.b) power = -speed;
    transfer.setPower(power);

    telemetry.addData("Speed (D-pad up/down)", "%.0f%%", speed * 100);
    telemetry.addData("Power (Cross fwd / Circle rev)", "%.2f", power);
    telemetry.addData("Encoder position", transfer.getCurrentPosition());
    telemetry.addData("Velocity (ticks/sec)", "%.0f", transfer.getVelocity());
    telemetry.update();
  }

  @Override
  public void stop() {
    transfer.setPower(0);
  }
}
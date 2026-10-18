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

  @Override
  public void init() {
    transfer = hardwareMap.get(DcMotorEx.class, TRANSFER_NAME);
    transfer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  }

  @Override
  public void loop() {
    // Hold Cross (A) = forward, hold Circle (B) = reverse, half power
    double power = 0;
    if (gamepad1.a) power = 0.5;
    if (gamepad1.b) power = -0.5;
    transfer.setPower(power);

    telemetry.addData("Power (Cross fwd / Circle rev)", power);
    telemetry.addData("Encoder position", transfer.getCurrentPosition());
    telemetry.addData("Velocity (ticks/sec)", "%.0f", transfer.getVelocity());
    telemetry.update();
  }

  @Override
  public void stop() {
    transfer.setPower(0);
  }
}
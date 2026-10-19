package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.subsystems.Intake;

@TeleOp(name = "Intake + Transfer Test")
public class IntakeTransferTest extends OpMode {

  // Must match the robot configuration exactly
  private static final String TRANSFER_NAME = "transfer";

  private Intake intake;
  private DcMotorEx transfer;

  private double transferSpeed = 1.0;   // default 100%
  private boolean prevUp = false;
  private boolean prevDown = false;

  @Override
  public void init() {
    intake = new Intake(hardwareMap);

    transfer = hardwareMap.get(DcMotorEx.class, TRANSFER_NAME);
    transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  }

  @Override
  public void loop() {
    // ---- Intake: hold Cross = in, hold Circle = out ----
    String intakeState;
    if (gamepad1.a) {
      intake.in();
      intakeState = "IN";
    } else if (gamepad1.b) {
      intake.out();
      intakeState = "OUT";
    } else {
      intake.stop();
      intakeState = "stopped";
    }

    // ---- Transfer speed: D-pad up/down, 10% per press (10% to 100%) ----
    if (gamepad1.dpad_up && !prevUp) transferSpeed = Math.min(1.0, transferSpeed + 0.1);
    if (gamepad1.dpad_down && !prevDown) transferSpeed = Math.max(0.1, transferSpeed - 0.1);
    prevUp = gamepad1.dpad_up;
    prevDown = gamepad1.dpad_down;

    // ---- Transfer: hold Square = toward flywheel, hold Triangle = reverse ----
    double transferPower = 0;
    String transferState = "holding";
    if (gamepad1.x) {
      transferPower = transferSpeed;
      transferState = "UP (to flywheel)";
    } else if (gamepad1.y) {
      transferPower = -transferSpeed;
      transferState = "BACK";
    }
    transfer.setPower(transferPower);

    telemetry.addData("Intake (Cross in / Circle out)", intakeState);
    telemetry.addData("Transfer (Square up / Triangle back)", transferState);
    telemetry.addData("Transfer speed (D-pad)", "%.0f%%", transferSpeed * 100);
    telemetry.update();
  }

  @Override
  public void stop() {
    intake.stop();
    transfer.setPower(0);
  }
}
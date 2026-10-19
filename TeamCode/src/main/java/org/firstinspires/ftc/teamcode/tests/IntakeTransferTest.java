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

  // goBILDA encoders count 28 ticks per motor-shaft revolution.
  // Set GEAR_RATIO from the intake motor's sticker: 6000 RPM = 1, 1620 = 3.7, 1150 = 5.2, 435 = 13.7, 312 = 19.2
  private static final double GEAR_RATIO = 50.9;

  private Intake intake;
  private DcMotorEx transfer;

  private double originalMainPower;     // restored on stop
  private double intakePower;
  private double transferSpeed = 1.0;   // default 100%

  private boolean prevUp = false, prevDown = false, prevLeft = false, prevRight = false;

  @Override
  public void init() {
    intake = new Intake(hardwareMap);
    originalMainPower = Intake.MAIN_POWER;
    intakePower = Intake.MAIN_POWER;    // start at the class default (0.8)

    transfer = hardwareMap.get(DcMotorEx.class, TRANSFER_NAME);
    transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  }

  @Override
  public void loop() {
    // ---- D-pad up/down: intake power, 10% per press (10% to 100%) ----
    if (gamepad1.dpad_up && !prevUp) intakePower = Math.min(1.0, intakePower + 0.1);
    if (gamepad1.dpad_down && !prevDown) intakePower = Math.max(0.1, intakePower - 0.1);
    prevUp = gamepad1.dpad_up;
    prevDown = gamepad1.dpad_down;
    Intake.MAIN_POWER = intakePower;

    // ---- D-pad right/left: transfer speed, 10% per press (10% to 100%) ----
    if (gamepad1.dpad_right && !prevRight) transferSpeed = Math.min(1.0, transferSpeed + 0.1);
    if (gamepad1.dpad_left && !prevLeft) transferSpeed = Math.max(0.1, transferSpeed - 0.1);
    prevRight = gamepad1.dpad_right;
    prevLeft = gamepad1.dpad_left;

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

    double motorRpm = intake.getMainTicksPerSec() / 28.0 * 60.0;

    telemetry.addData("Intake (Cross in / Circle out)", intakeState);
    telemetry.addData("Intake power (D-pad up/down)", "%.0f%%", intakePower * 100);
    telemetry.addData("Intake motor RPM (shaft)", "%.0f", motorRpm);
    telemetry.addData("Intake output RPM", "%.0f", motorRpm / GEAR_RATIO);
    telemetry.addData("Transfer (Square up / Triangle back)", transferState);
    telemetry.addData("Transfer speed (D-pad left/right)", "%.0f%%", transferSpeed * 100);
    telemetry.update();
  }

  @Override
  public void stop() {
    intake.stop();
    transfer.setPower(0);
    Intake.MAIN_POWER = originalMainPower;  // don't leak the test value into other OpModes
  }
}
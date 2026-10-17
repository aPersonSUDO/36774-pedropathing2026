package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("leftfront");
        c.frontRightName.set("rightfront");
        c.backLeftName.set("leftback");
        c.backRightName.set("rightback");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.manualBrakeMode.set(true);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("odomcomp");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(2.85);
        c.yPodOffset.set(-1.7525500199926183);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static Follower create(HardwareMap h) {
        return new Follower(
            new PinpointLocalizer(h, localizerConfig),
            new Mecanum(h, drivetrainConfig),
            new Foresight(foresightConfig)
        );
    }

    public static ForesightConfig foresightConfig = new ForesightConfig(
        c -> {
            Controller primaryTranslationalForward = Controller.proportional(0.19413400497965824);
            Controller secondaryTranslationalForward = Controller.proportional(0.07172732748383374);
            Controller primaryTranslationalLateral = Controller.proportional(0.23764279539719071);
            Controller secondaryTranslationalLateral = Controller.proportional(0.08780266296682057);

            c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
            c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

            c.coast.set(Controller.proportionalFeedforward(0.017389356677845302));
            c.brake.set(Controller.proportionalFeedforward(0.014780953176168506));

            c.headingFeedback.set(Controller.proportional(3.04943011525379));
            c.headingBrakeCoefficients.set(Vector2D.cartesian(0.044347804237630274, 0.005030203878913554));

            c.linearBrakeCoefficients.set(Matrix.diag(0.08660325064525407, 0.06663123813222216));
            c.quadraticBrakeCoefficients.set(Matrix.diag(5.83618534644232E-4, 8.069577156433441E-4));

            c.maxAchievableForwardVelocity.set(58.424670577668905);
            c.maxAchievableStrafeVelocity.set(49.014295988583314);
            c.naturalForwardDeceleration.set(30.551663298245547);
            c.naturalStrafeDeceleration.set(59.38720787324426);
        }
    );

}
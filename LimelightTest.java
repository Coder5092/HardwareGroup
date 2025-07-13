package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@TeleOp(group = "test")
public class LimelightTest extends LinearOpMode {
  private Limelight3A limelight;
  private Servo light;

  @Override
  public void runOpMode() {
    limelight = hardwareMap.get(Limelight3A.class, "limelight");
    light = hardwareMap.get(Servo.class, "light");

    telemetry.setMsTransmissionInterval(11);

    limelight.pipelineSwitch(0);

    waitForStart();

    limelight.start();

    while (opModeIsActive()) {
      LLResult result = limelight.getLatestResult();
      if (result != null) {
        if (result.isValid()) {
          Pose3D botpose = result.getBotpose();
          telemetry.addData("tx", result.getTx());
          telemetry.addData("ty", result.getTy());
          telemetry.addData("Botpose", botpose.toString());
        }
      }
    }
  }

  private void setColor(@ NonNull LLResult result) {
    if (result == null) throw new RuntimeException();
    else if (! result.isValid()) light.setPosition(0);
    List<LLResultTypes.DetectorResult> results = result.getDetectorResults();
    for (LLResultTypes.DetectorResult detection : results) {
      String name = detection.getClassName();
      if (name == "yellowsample") {
        light.setPosition(0.388);
        return;
      } else if (name == "bluesample") {
        light.setPosition(0.611);
        return;
      } else if (name == "redsample") {
        light.setPosition(0.279);
        return;
      }
    }
  }
}
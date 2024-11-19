package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Allows the identification and testing of each drive motor.
 */
@TeleOp(name="Motor Test")
public class TestMotorOpMode extends LinearOpMode {
  private List<String> motorNames = Arrays.asList(
    "left_front_drive",
    "left_back_drive",
    "right_front_drive",
    "right_back_drive"
    );
  private int[] encoderReadings;
  private int[] deltaNanos;
  private int recordedEncoderReadings = 0;
  private int lastEncoderReading = 0;
  private long lastNanoTime = 0;
  private int encoderReadingIdx = 0;
  private static final int ENCODER_READING_COUNT = 100000;
  private ArrayList<DcMotor> motors = new ArrayList<DcMotor>();
  private DcMotor currentMotor = null;
  private int currentMotorIdx = 0;
  private boolean didChangeMotor = false;
  private boolean didChangeMode = false;
  private boolean encoderTestMode = false;
  private boolean didResetEncReads = false;
  private static final float EPSILON = 0.001f;
  @Override
  public void runOpMode() {
    encoderReadings = new int[ENCODER_READING_COUNT];
    deltaNanos = new int[ENCODER_READING_COUNT];
    for (int i = 0; i < motorNames.size(); ++i) {
      motors.add(hardwareMap.get(DcMotor.class, motorNames.get(i)));
      motors.get(i).setDirection(DcMotorSimple.Direction.FORWARD);
      motors.get(i).setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    updateCurrentMotor();
    waitForStart();
    while (opModeIsActive()) {
      boolean shouldChangeMotor = Math.abs(gamepad1.left_stick_y) > EPSILON;
      if (shouldChangeMotor && !didChangeMotor) {
        currentMotorIdx = (currentMotorIdx + 1) % motors.size();
        currentMotor.setPower(0.0f);
        updateCurrentMotor();
        resetEncoderReadings();
      }
      didChangeMotor = shouldChangeMotor;
      boolean shouldChangeMode = Math.abs(gamepad1.left_trigger) > EPSILON; // why is it a float
      if (shouldChangeMode && !didChangeMode) {
        encoderTestMode = !encoderTestMode;
        currentMotor.setPower(0.0f);
        resetEncoderReadings();
      }
      didChangeMode = shouldChangeMode;

      telemetry.addData("cmidx", "Current motor index: %d", currentMotorIdx);
      telemetry.addData("Current motor name", motorNames.get(currentMotorIdx));
      telemetry.addData("Zero power behavior", motors.get(currentMotorIdx).getZeroPowerBehavior());
      telemetry.addData("Run mode", motors.get(currentMotorIdx).getMode());
      if (encoderTestMode) {
        currentMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        telemetry.addData("Current motor encoder", currentMotor.getCurrentPosition());
      } else {
        int encoderReading = currentMotor.getCurrentPosition();
        long nanoTime = System.nanoTime();
        if (!didResetEncReads) {
          encoderReadings[encoderReadingIdx] = encoderReading - lastEncoderReading;
          deltaNanos[encoderReadingIdx] = (int)(nanoTime - lastNanoTime);
          encoderReadingIdx = (encoderReadingIdx + 1) % ENCODER_READING_COUNT;
          if (recordedEncoderReadings < ENCODER_READING_COUNT) {
            ++recordedEncoderReadings;
          }
        }
        lastEncoderReading = encoderReading;
        lastNanoTime = nanoTime;
        didResetEncReads = false;
        currentMotor.setPower(Math.abs(gamepad1.right_stick_y) > EPSILON ? 1.0f : 0.0f);

        telemetry.addData("Current motor speed", "%4.2f ticks/sec", computeMotorSpeed());
        telemetry.addData("Sampled encoder readings", "%d", recordedEncoderReadings);
      }
      telemetry.update();
    }
  }

  private void updateCurrentMotor() {
    currentMotor = motors.get(currentMotorIdx);
  }
  private void resetEncoderReadings() {
    recordedEncoderReadings = 0;
    encoderReadingIdx = 0;
    didResetEncReads = true;
  }
  private double computeMotorSpeed() {
    if (recordedEncoderReadings == 0) {
      return 0;
    }
    long total = 0;
    long totalTimeNano = 0;
    for (int i = 0; i < recordedEncoderReadings; ++i) {
      total += encoderReadings[i];
      totalTimeNano += deltaNanos[i];
    }
    return (double)total / (double)recordedEncoderReadings * Math.pow(10, 9) / totalTimeNano;
  }
}

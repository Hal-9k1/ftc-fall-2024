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
  /**
   * The names of motors to cycle through testing.
   */
  private List<String> motorNames = Arrays.asList(
    "left_front_drive",
    "left_back_drive",
    "right_front_drive",
    "right_back_drive"
    );
  /**
   * History of encoder readings, kept to calculate average motor speed.
   */
  private int[] encoderReadings;
  /**
   * History of times between successive encoder readings, kept to calculate average motor speed.
   */
  private int[] deltaNanos;
  /**
   * The number of meaningful encoder readings and delta times at the start of the encoderReadings
   * and deltaNanos arrays respectively.
   * Values past this are either 0 if the arrays have never been filled before or remnants from the
   * last time the encoder readings were reset, because the arrays are never actually cleared.
   * Capped at ENCODER_READING_COUNT.
   */
  private int recordedEncoderReadings = 0;
  /**
   * The last recorded encoder reading.
   */
  private int lastEncoderReading = 0;
  /**
   * The timestamp of the last recorded encoder reading.
   */
  private long lastNanoTime = 0;
  /**
   * The next index in encoderReadings to write to.
   */
  private int encoderReadingIdx = 0;
  /**
   * The maximum number of encoder readings to take before old ones are overwritten.
   */
  private static final int ENCODER_READING_COUNT = 100000;
  /**
   * The motors to cycle through testing.
   */
  private ArrayList<DcMotor> motors = new ArrayList<DcMotor>();
  /**
   * The motor currently being tested.
   * Equivalent to motors.get(currentMotorIdx) after {@link #updateCurrentMotor} is called.
   */
  private DcMotor currentMotor = null;
  /**
   * The index in {@link #motors} of the currently tested motor.
   */
  private int currentMotorIdx = 0;
  /**
   * Debounce for the input to test the next motor in the list.
   */
  private boolean didChangeMotor = false;
  /**
   * Debounce for the input to toggle the encoder recording mode between absolute and average speed.
   */
  private boolean didChangeMode = false;
  /**
   * The encoder testing mode.
   * If true, the current encoder reading is output to telemetry. Otherwise, the average motor
   * speed in encoder ticks per second is calculated and output.
   */
  private boolean encoderTestMode = false;
  /**
   * Debounce for the input to reset encoder reading history.
   */
  private boolean didResetEncReads = false;
  /**
   * The minimum input strength for joysticks and triggers to activate digital inputs.
   */
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

  /**
   * Updates {@link #currentMotor} to match the {@link currentMotorIdx}'th motor in the list.
   */
  private void updateCurrentMotor() {
    currentMotor = motors.get(currentMotorIdx);
  }
  /**
   * Resets encoder readings.
   * Does not actually fill any arrays with zeroes; by setting recordedEncoderReadings to 0 the old
   * measurements are disregarded.
   */
  private void resetEncoderReadings() {
    recordedEncoderReadings = 0;
    encoderReadingIdx = 0;
    didResetEncReads = true;
  }
  /**
   * Computes the average motor speed over all recorded encoder readings.
   * @return the average motor speed in encoder ticks per second, or 0 if no readings have been
   * recorded.
   */
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

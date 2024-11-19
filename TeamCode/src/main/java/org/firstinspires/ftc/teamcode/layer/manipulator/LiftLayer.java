package org.firstinspires.ftc.teamcode.layer.manipulator;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.CircularBuffer;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.mechanism.Wheel;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.LiftTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import org.firstinspires.ftc.teamcode.Units;

/**
 * Controls an extendable lift operated by a pulley.
 */
public class LiftLayer implements Layer {
    /**
     * Height above zeroDist in meters of the lift when fully extended.
     */
    private static final double RAISE_HEIGHT = Units.convert(46.375 - 16.875, Units.Distance.IN, Units.Distance.M);
    /**
     * Height just above zeroDist to meters of the lift to move to when lowering.
     * After lowering to this distance, the motor is disengaged and gravity lowers the lift the rest
     * of the way.
     */
    private static final double LOWER_HEIGHT = Units.convert(10, Units.Distance.CM, Units.Distance.M);
    /**
     * Displacing the string translates the lift by the amount string displaced times this number.
     */
    private static final double STRING_FAC = 2;
    /**
     * Maximum number of pulley goal deltas to track for integral calculation.
     * Increasing this reduces the calculation's sensitivity to recent rapid changes in goal error.
     */
    private static final int DELTA_HISTORY_COUNT = 2000;
    /**
     * Weight of proportional component in pulley velocity calculation.
     */
    private static final double PULLEY_P_COEFF = 0.05;
    /**
     * Weight of integral component in pulley velocity calculation.
     */
    private static final double PULLEY_I_COEFF = 0.05;
    /**
     * Radius of the pulley in meters.
     */
    private static final double PULLEY_RADIUS = 0.42;

    /**
     * The motor used to power the lift, only retained to change zero power behavior.
     */
    private DcMotor pulleyMotor;
    /**
     * The pulley powering the lift.
     */
    private Wheel pulley;
    /**
     * The "distance" reported by the pulley when the lift is at its minimum height.
     */
    private double zeroDist;
    /**
     * The pulley distance measured at the beginning of the current task.
     */
    private double startDist;
    /**
     * Whether the lift was last set to raise.
     */
    private boolean raising;
    /**
     * Whether the goal set for the current task has been achieved.
     */
    private boolean goalAchieved;
    /**
     * The recent history of goal deltas.
     */
    private CircularBuffer<Double> deltaHistory;

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        pulleyMotor = setupInfo.getHardwareMap().get(DcMotor.class, "lift_motor");
        pulley = new Wheel(pulleyMotor, PULLEY_RADIUS);
        TouchSensor zeroSwitch = setupInfo.getHardwareMap().get(TouchSensor.class,
            "lift_zero_switch");
        zeroDist = pulley.getDistance();
        startDist = zeroDist;
        raising = false;
        goalAchieved = true;
        deltaHistory = new CircularBuffer<>(DELTA_HISTORY_COUNT);
        setupInfo.addUpdateListener((isTeardown) -> {
            if (zeroSwitch.isPressed() && !isTeardown) {
                zeroDist = pulley.getDistance();
            }
        });
    }

    @Override
    public boolean isTaskDone() {
        return goalAchieved;
    }

    @Override
    public Task update() {
        pulleyMotor.setZeroPowerBehavior(raising ? DcMotor.ZeroPowerBehavior.BRAKE
            : DcMotor.ZeroPowerBehavior.FLOAT);
        double goalDist = (raising ? RAISE_HEIGHT : LOWER_HEIGHT) / STRING_FAC;
        double remainingDelta = goalDist - getPulleyDistance();
        double startDelta = goalDist - startDist;
        if ((remainingDelta < 0) != (startDelta < 0)) {
            goalAchieved = true;
        }
        if (!raising && goalAchieved) {
            pulley.setVelocity(0);
        } else if (!raising) {
            pulley.setVelocity(-1);
        } else {
            double proportional = PULLEY_P_COEFF * remainingDelta;
            double integral = PULLEY_I_COEFF * deltaHistory
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
            deltaHistory.add(remainingDelta);
            double velocity = Math.max(-1.0, Math.min(1.0, proportional + integral));
            pulley.setVelocity(velocity);
        }
        return null;
    }

    @Override
    public void acceptTask(Task task) {
        if (task instanceof LiftTask) {
            LiftTask castedTask = (LiftTask)task;
            goalAchieved = false;
            startDist = getPulleyDistance();
            deltaHistory.clear();
            if (castedTask.raise) {
                raising = true;
            } else if (castedTask.lower) {
                raising = false;
            }
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }

    /**
     * Calculates the current distance from the base to the top of the lift.
     */
    private double getPulleyDistance() {
        return pulley.getDistance() - zeroDist;
    }
}

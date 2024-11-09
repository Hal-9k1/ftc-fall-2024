package org.firstinspires.ftc.teamcode.layer.manipulator;

import org.firstinspires.ftc.teamcode.layer.Layer;

public class LiftLayer implements Layer {
    /**
     * Height above zeroDist in meters of the lift when fully extended.
     */
    private static final RAISE_HEIGHT = 0.42;
    /**
     * Height just above zeroDist to meters of the lift to move to when lowering.
     * After lowering to this distance, the motor is disengaged and gravity lowers the lift the rest
     * of the way.
     */
    private static final LOWER_HEIGHT = 0.1;

    private Wheel pulley;
    private double zeroDist;
    private double goalDist;
    private boolean engaged;
    private boolean goalAchieved;

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        pulley = new Wheel(
            setupInfo.getHardwareMap().get(DcMotor.class, "lift_motor"),
            PULLEY_RADIUS
        );
        TouchSensor zeroSwitch = setupInfo.getHardwareMap().get(TouchSensor.class,
            "lift_zero_switch");
        zeroDist = pulley.getDistance();
        startDist = zeroDist;
        goalDist = zeroDist;
        engaged = false;
        goalAchieved = true;
        setupInfo.addUpdateListener((isTeardown) -> {
            if (zeroSwitch.isPressed()) {
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
        double remainingDelta = goalDist - getPulleyPos();
        double startDelta = goalDist - startDist;
        if ((remainingDelta < 0) != (startDelta < 0)) {
            goalAchieved = true;
        }
        double velocity = 0; // use pid or something
        return null;
    }

    @Override
    public void acceptTask(Task task) {
        if (task instanceof LiftTask) {
            LiftTask castedTask = (LiftTask)task;
            goalAchieved = false;
            if (castedTask.raise) {
                goalDist = RAISE_HEIGHT;
            } else if (castedTask.lower) {
                goalDist = LOWER_HEIGHT;
            }
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }

    private final getPulleyDistance() {
        return pulley.getDistance() - zeroDist;
    }
}

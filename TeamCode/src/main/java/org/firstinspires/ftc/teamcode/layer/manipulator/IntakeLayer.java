package org.firstinspires.ftc.teamcode.layer.manipulator;

import java.util.Iterator;

import com.qualcomm.robotcore.hardware.CRServo;
//import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.Units;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.IntakeTask;
import org.firstinspires.ftc.teamcode.task.IntakeTeleopTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Controls a roller intake that can acquire, hold, and eject samples.
 */
public final class IntakeLayer implements Layer {
    /**
     * An action the layer can take.
     */
    private enum State {
        /**
         * The intake is currently attempting to acquire a sample.
         */
        INTAKING,
        /**
         * The intake is currently ejecting a sample.
         */
        EJECTING,
        /**
         * The intake is idle.
         */
        IDLE
    }

    /**
     * Duration in nanoseconds of intake.
     */
    private static final double INTAKE_DURATION = Units.convert(1.5, Units.Time.SEC, Units.Time.NANO);

    /**
     * Duration in nanoseconds of ejection.
     */
    private static final double EJECT_DURATION = Units.convert(1.5, Units.Time.SEC, Units.Time.NANO);

    /**
     * Multiplier for intake actuator power.
     * Should be in the range [-1.0, 1.0].
     */
    private static final double INTAKE_SPEED = 1.0;

    /**
     * Servo spinning the intake mechanism.
     */
    private CRServo intake;

    /**
     * The current action the layer is taking.
     */
    private State state;

    /**
     * The nanosecond timestamp of the start of the last intake action.
     */
    private long intakeStart;

    /**
     * The nanosecond timestamp of the start of the last eject action.
     */
    private long ejectStart;

    /**
     * Telemetry.
     */
    //private Telemetry telemetry;

    /**
     * Constructs an IntakeLayer.
     */
    public IntakeLayer() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        intake = setupInfo.getHardwareMap().get(CRServo.class, "intake");
        state = State.IDLE;
        //TouchSensor loadSensor = setupInfo.getHardwareMap().get(TouchSensor.class, "intake_load_sensor");
        //telemetry = setupInfo.getTelemetry();
        setupInfo.addUpdateListener(() -> {
            //boolean intakeDone = state == State.INTAKING && loadSensor.isPressed();
            boolean intakeDone = state == State.INTAKING && (System.nanoTime() - intakeStart) > INTAKE_DURATION;
            boolean ejectDone = state == State.EJECTING && (System.nanoTime() - ejectStart) > EJECT_DURATION;
            if (intakeDone || ejectDone) {
                state = State.IDLE;
                intake.setPower(0);
            }
        });
        //telemetry = setupInfo.getTelemetry();
    }

    @Override
    public boolean isTaskDone() {
        return state == State.IDLE;
    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        return null;
    }

    @Override
    public void acceptTask(Task task) {
        if (task instanceof IntakeTask) {
            IntakeTask castedTask = (IntakeTask)task;
            if (castedTask.getAcquire()) {
                state = State.INTAKING;
                intake.setPower(-INTAKE_SPEED);
                intakeStart = System.nanoTime();
            } else if (castedTask.getEject()) {
                state = State.EJECTING;
                intake.setPower(INTAKE_SPEED);
                ejectStart = System.nanoTime();
            }
        } else if (task instanceof IntakeTeleopTask) {
            IntakeTeleopTask castedTask = (IntakeTeleopTask)task;
            if (castedTask.getAcquire()) {
                state = State.INTAKING;
                intake.setPower(-INTAKE_SPEED);
                intakeStart = System.nanoTime();
            } else if (castedTask.getTimedEject()) {
                state = State.EJECTING;
                intake.setPower(INTAKE_SPEED);
                ejectStart = System.nanoTime();
            } else {
                state = State.IDLE;
                intakeStart = 0;
                ejectStart = 0;
                //telemetry.addData("intake power", castedTask.getIntakePower());
                intake.setPower(castedTask.getIntakePower());
            }
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}

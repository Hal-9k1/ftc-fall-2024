package org.firstinspires.ftc.teamcode.layer.manipulator;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import java.util.Iterator;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.IntakeTask;
import org.firstinspires.ftc.teamcode.task.IntakeTeleopTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import org.firstinspires.ftc.teamcode.Units;

public class IntakeLayer implements Layer {
    private static enum State {
        INTAKING,
        EJECTING,
        IDLE
    }

    /**
     * Duration in nanoseconds of intake.
     */
    private final double INTAKE_DURATION = Units.convert(1.5, Units.Time.SEC, Units.Time.NANO);
    /**
     * Duration in nanoseconds of ejection.
     */
    private final double EJECT_DURATION = Units.convert(1.5, Units.Time.SEC, Units.Time.NANO);

    private final double INTAKE_SPEED = 1.0;

    /**
     * Servo spinning the intake mechanism.
     */
    private CRServo intake;
    private State state;
    private long intakeStart;
    private long ejectStart;

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        intake = setupInfo.getHardwareMap().get(CRServo.class, "intake");
        state = State.IDLE;
        //TouchSensor loadSensor = setupInfo.getHardwareMap().get(TouchSensor.class, "intake_load_sensor");
        setupInfo.addUpdateListener(() -> {
            //boolean intakeDone = state == State.INTAKING && loadSensor.isPressed();
            boolean intakeDone = state == State.INTAKING && (System.nanoTime() - intakeStart) > INTAKE_DURATION;
            boolean ejectDone = state == State.EJECTING && (System.nanoTime() - ejectStart) > EJECT_DURATION;
            if (intakeDone || ejectDone) {
                state = State.IDLE;
                intake.setPower(0);
            }
        });
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
            if (castedTask.acquire) {
                state = State.INTAKING;
                intake.setPower(-INTAKE_SPEED);
                intakeStart = System.nanoTime();
            } else if (castedTask.eject) {
                state = State.EJECTING;
                intake.setPower(INTAKE_SPEED);
                ejectStart = System.nanoTime();
            }
        } else if (task instanceof IntakeTeleopTask) {
            IntakeTeleopTask castedTask = (IntakeTeleopTask)task;
            if (castedTask.acquire) {
                state = State.INTAKING;
                intake.setPower(-INTAKE_SPEED);
                intakeStart = System.nanoTime();
            } else if (castedTask.timedEject) {
                state = State.EJECTING;
                intake.setPower(INTAKE_SPEED);
                ejectStart = System.nanoTime();
            } else {
                state = State.IDLE;
                intakeStart = 0;
                ejectStart = 0;
                intake.setPower(castedTask.intakePower);
            }
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}

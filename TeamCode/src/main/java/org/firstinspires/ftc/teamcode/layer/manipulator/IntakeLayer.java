package org.firstinspires.ftc.teamcode.layer.manipulator;

import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.Iterable;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.IntakeTask;
import org.firstinspires.ftc.teamcode.task.IntakeTeleopTask;
import org.firstinspires.ftc.teamcode.task.Task;

public class IntakeLayer implements Layer {
    private static enum State {
        INTAKING,
        EJECTING,
        IDLE
    }
    /**
     * Duration in nanoseconds of ejection.
     */
    private final double EJECT_DURATION = Units.convert(1.5, Units.Time.SEC, Units.Time.NANO);

    private final double INTAKE_SPEED = 1.0;

    /**
     * Motor spinning the intake mechanism.
     */
    private DcMotor intake;
    private State state;
    private long intakeStart;
    private long ejectStart;

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        intake = setupInfo.getHardwareMap().get(DcMotor.class, "intake");
        state = IDLE;
        TouchSensor loadSensor = setupInfo.getHardwareMap().get(TouchSensor.class, "intake_load_sensor");
        setup.addUpdateListener(() -> {
            //boolean intakeDone = state == INTAKING && loadSensor.isPressed();
            boolean intakeDone = state == INTAKING && (System.nanoTime() - intakeStart) > INTAKE_DURATION;
            boolean ejectDone = state == EJECTING && (System.nanoTime() - ejectStart) > EJECT_DURATION;
            if (intakeDone || ejectDone) {
                state = IDLE;
                intake.setPower(0);
            }
        });
    }
    @Override
    public boolean isTaskDone() {
        return state == IDLE;
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
                state = INTAKING;
                intake.setPower(-INTAKE_SPEED);
                intakeStart = System.nanoTime();
            } else if (castedTask.eject) {
                state = EJECTING;
                intake.setPower(INTAKE_SPEED);
                ejectStart = System.nanoTime();
            }
        } else if (task instanceof IntakeTeleopTask) {
            IntakeTeleopTask castedTask = (IntakeTeleopTask)task;
            if (castedTask.acquire) {
                state = INTAKING;
                intake.setPower(-INTAKE_SPEED);
                intakeStart = System.nanoTime();
            } else if (castedTask.timedEject) {
                state = EJECTING;
                intake.setPower(INTAKE_SPEED);
                ejectStart = System.nanoTime();
            } else {
                state = IDLE;
                intakeStart = 0;
                ejectStart = 0;
                intake.setPower(castedTask.intakePower);
            }
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}

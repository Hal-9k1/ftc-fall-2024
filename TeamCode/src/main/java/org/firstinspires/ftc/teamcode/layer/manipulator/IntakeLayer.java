package org.firstinspires.ftc.teamcode.layer.manipulator;

import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.Iterable;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.IntakeTask;
import org.firstinspires.ftc.teamcode.task.IntakeTeleopTask;
import org.firstinspires.ftc.teamcode.task.Task;

public class IntakeLayer implements Layer {
    /**
     * Duration in nanoseconds of ejection.
     */
    private final double EJECT_DURATION = Units.convert(1.5, Units.Time.SEC, Units.Time.NANO);

    private final double INTAKE_SPEED = 1.0;

    /**
     * Motor spinning the intake mechanism.
     */
    private DcMotor intake;
    private boolean isIntaking;
    private boolean isEjecting;
    private long ejectStart;

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        intake = setupInfo.getHardwareMap().get(DcMotor.class, "intake");
        TouchSensor loadSensor = setupInfo.getHardwareMap().get(TouchSensor.class, "intake_load_sensor");
        setup.addUpdateListener(() -> {
            if (isIntaking && loadSensor.isPressed()) {
                isIntaking = false;
                intake.setPower(0);
            } else if (isEjecting && (System.nanoTime() - ejectStart) > EJECT_DURATION) {
                isEjecting = false;
                intake.setPower(0);
            }
        });
    }
    @Override
    public boolean isTaskDone() {
        return !(isIntaking || isEjecting);
    }
    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        return null;
    }
    @Override
    public void acceptTask(Task task) {
        if (task instanceof IntakeTask) {
            IntakeTask castedTask = (IntakeTask)task;
            if (castedTask.intake) {
                isIntaking = true;
                intake.setPower(-INTAKE_SPEED);
            }
            if (castedTask.eject) {
                isEjecting = true;
                intake.setPower(INTAKE_SPEED);
            }
        } else if (task instanceof IntakeTeleopTask) {
            IntakeTeleopTask castedTask = (IntakeTeleopTask)task;
        }
    }
}

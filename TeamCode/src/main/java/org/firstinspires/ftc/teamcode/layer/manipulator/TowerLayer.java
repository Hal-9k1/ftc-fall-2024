package org.firstinspires.ftc.teamcode.layer.manipulator;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.Units;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.TowerInitTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TowerTeleopTask;
import org.firstinspires.ftc.teamcode.task.TowerTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import java.util.Iterator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class TowerLayer implements Layer {
    private final double FOREARM_INIT_ANGLE = Units.convert(
        0.25,
        Units.Angle.REV,
        Units.Angle.RAD
    );
    private final double CLAW_TOGGLE_DURATION = Units.convert(
        0.5,
        Units.Time.SEC,
        Units.Time.NANO
    );
    private DcMotor tower;
    private DcMotor forearm;
    /**
     * Claw for specimens.
     */
    private Servo claw;
    private boolean isInit;
    private boolean towerWorking;
    private double forearmZero;
    private double towerZero;
    private double towerStartPos;
    private double towerGoalAngle;
    private long clawStartTime;

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        isInit = false;
        tower = setupInfo.getHardwareMap().get(DcMotor.class, "tower_swing");
        tower.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        towerZero = tower.getCurrentPosition();
        forearm = setupInfo.getHardwareMap().get(DcMotor.class, "forearm_swing");
        forearm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        forearmZero = forearm.getCurrentPosition();
        //claw = setupInfo.getHardwareMap().get(Servo.class, "claw");
        clawStartTime = 0;
    }
    @Override
    public boolean isTaskDone() {
        if (isInit) {
            return checkDelta(getForearmAngle(), FOREARM_INIT_ANGLE);
        } else if (towerWorking) {
            return checkTowerDone();
        } else {
            return System.nanoTime() - clawStartTime >= CLAW_TOGGLE_DURATION;
        }
    }
    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        return null;
    }
    @Override
    public void acceptTask(Task task) {
        if (task instanceof TowerInitTask) {
            forearm.setPower(1);
            isInit = true;
        } else if (task instanceof TowerTask) {
            TowerTask castedTask = (TowerTask)task;

        } else if (task instanceof TowerTeleopTask) {
            TowerTeleopTask castedTask = (TowerTeleopTask)task;
            tower.setPower(castedTask.towerSwingPower);
            return;
        } else {
            throw new UnsupportedTaskException(this, task);
        }
        towerStartPos = tower.getCurrentPosition();
    }
    private boolean checkDelta(double delta, double goalDelta) {
        boolean magExceeded = Math.abs(delta) > Math.abs(goalDelta);
        boolean signMatches = (delta > 0) == (goalDelta > 0);
        return magExceeded && signMatches;
    }
    private double getForearmAngle() {
        double revs = (forearm.getCurrentPosition() - forearmZero)
            / forearm.getMotorType().getTicksPerRev();
        return Units.convert(revs, Units.Angle.REV, Units.Angle.RAD);
    }
    private boolean checkTowerDone() {
        double revs = (tower.getCurrentPosition() - towerStartPos)
            / tower.getMotorType().getTicksPerRev();
        double deltaAngle = Units.convert(revs, Units.Angle.REV, Units.Angle.RAD);
        double startAngle = towerStartPos / tower.getMotorType().getTicksPerRev();
        double goalDeltaAngle = towerGoalAngle - startAngle;
        return checkDelta(deltaAngle, goalDeltaAngle);
    }
}

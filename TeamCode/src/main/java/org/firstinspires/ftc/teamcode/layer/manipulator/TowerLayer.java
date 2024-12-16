package org.firstinspires.ftc.teamcode.layer.manipulator;

import java.util.Iterator;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.CircularBuffer;
import org.firstinspires.ftc.teamcode.Units;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TowerForearmTask;
import org.firstinspires.ftc.teamcode.task.TowerInitTask;
import org.firstinspires.ftc.teamcode.task.TowerTask;
import org.firstinspires.ftc.teamcode.task.TowerTeleopTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

public class TowerLayer implements Layer {
    /**
     * Maximum number of pulley goal deltas to track for integral calculation.
     * Increasing this reduces the calculation's sensitivity to recent rapid changes in goal error.
     */
    private static final int DELTA_HISTORY_COUNT = 2000;

    /**
     * Weight of proportional component in pulley velocity calculation.
     */
    private static final double TOWER_P_COEFF = 0.05;

    /**
     * Weight of integral component in pulley velocity calculation.
     */
    private static final double TOWER_I_COEFF = 0.05;

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

    /**
     * Fully raised angle for the tower.
     * Currently set to a placeholder value of 0.25 revolutions. This needs to be tested and changed
     * later.
     */
    private static final double FULL_RAISE_ANGLE = Units.convert(
        0.25,
        Units.Angle.REV,
        Units.Angle.RAD
    );

    /**
     * Motor used to swing the tower, zero power behavior is only brake.
     */
    private DcMotor tower;

    /**
     * Motor used to swing the forearm, zero power behavior is only brake.
     */
    private DcMotor forearm;

    /**
     * Claw for specimens.
     */
    private Servo claw;

    /**
     * Check to see if tower and forearm has moved to the initialized location.
     */
    private boolean isInit;

    /**
     * Check if tower is currently swinging.
     */
    private boolean isSwinging;

    /**
     * The distance reported by the forearm at minimum height.
     */
    private double forearmZero;

    /**
     * The "distance" reported by the tower when it is at minimum height.
     */
    private double towerZero;

    /**
     * Tower distance measured at the beginning of the current task.
     */
    private double towerStartPos;

    /**
     * The angle the tower needs to reach, using towerZero as reference.
     */
    private double towerGoalAngle;

    /**
     * Timestamp for when the claw last began moving.
     */
    private long clawStartTime;

    /**
     * The recent history of goal deltas.
     */
    private CircularBuffer<Double> deltaHistory;

    public TowerLayer() { }

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
        setupInfo.addUpdateListener(() -> {
            if (isSwinging && checkTowerDone()) {
                isSwinging = false;
                tower.setPower(0.0);
            }
        });
    }

    @Override
    public boolean isTaskDone() {
        if (isInit) {
            return checkDelta(getForearmAngle(), FOREARM_INIT_ANGLE);
        } else if (isSwinging) {
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
        if (task instanceof TowerForearmTask) {
            forearm.setPower(1);
            isInit = true;
        } else if (task instanceof TowerTask) {
            TowerTask castedTowerTask = (TowerTask)task;
            isSwinging = true;
            if (castedTowerTask.fullRaise) {
                towerGoalAngle = FULL_RAISE_ANGLE;
            } else if (castedTowerTask.fullLower) {
                towerGoalAngle = 0;
            }
            towerStartPos = tower.getCurrentPosition();
        } else if (task instanceof TowerTeleopTask) {
            TowerTeleopTask castedTask = (TowerTeleopTask)task;
            tower.setPower(castedTask.towerSwingPower);
        } else {
            throw new UnsupportedTaskException(this, task);
        }
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

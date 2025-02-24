package org.firstinspires.ftc.teamcode.layer.manipulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.CircularBuffer;
import org.firstinspires.ftc.teamcode.Units;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.logging.Logger;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TowerForearmTask;
import org.firstinspires.ftc.teamcode.task.TowerHangTask;
import org.firstinspires.ftc.teamcode.task.TowerTask;
import org.firstinspires.ftc.teamcode.task.TowerTeleopTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Controls a tower: a multi-jointed arm with a folding forearm and a claw that can hold specimens
 * if aligned by a human player.
 * The forearm should be "initialized" with a TowerForearmTask before it is first used. This unfolds
 * it from the position it starts in to fit in the sizing cube. The forearm remains in its unfolded
 * position throughout the lifetime of the program.
 */
public final class TowerLayer implements Layer {
    /**
     * Maximum number of tower goal deltas to track for integral calculation.
     * Increasing this reduces the calculation's sensitivity to recent rapid changes in goal error.
     */
    private static final int DELTA_HISTORY_COUNT = 2000;

    /**
     * Weight of proportional component in tower swing velocity calculation.
     */
    private static final double TOWER_P_COEFF = 0.05;

    /**
     * Weight of integral component in tower swing velocity calculation.
     */
    private static final double TOWER_I_COEFF = 0.05;

    /**
     * The angle between the forearm's initial position and the position it should take after
     * initialization.
     * The forearm will hold this angle throughout the match, never retracting to its initial
     * position.
     */
    private static final double FOREARM_INIT_ANGLE = Units.convert(
        90,
        Units.Angle.DEG,
        Units.Angle.RAD
    );

    /**
     * The number of revolutions of the forearm caused by one revolution of the drive shaft.
     */
    private static final double FOREARM_GEAR_RATIO = 1.0 / 480.0 * 8.0 * 140.0 / 90.0;

    /**
     * The maximum safe angle for the forearm from its resting position.
     */
    private static final double FOREARM_MAX_SAFE_ANGLE = Units.convert(
        0.5,
        Units.Angle.REV,
        Units.Angle.RAD
    );

    /**
     * An estimate of how long the claw servo takes to finish toggling between states.
     * Servos don't report on progress towards goals, so we use this to judge whether we should
     * report a claw manipulation task as being done.
     */
    private static final double CLAW_TOGGLE_DURATION = Units.convert(
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
     * The tower motor power to use when hanging mode is enabled.
     */
    private static final double HANG_POWER = 1.0;

    /**
     * The number of revolutions of the tower caused by one revolution of the drive shaft.
     */
    private static final double TOWER_GEAR_RATIO = 45.0 / 125.0;

    /**
     * Motor used to swing the tower.
     * The zero power behavior is set to brake.
     */
    private DcMotor tower;

    /**
     * Motor used to swing the forearm.
     * The zero power behavior is set to brake.
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
     * Whether the forearm has finished moving to its initialized location.
     */
    private boolean finishedInit;

    /**
     * Whether the tower is currently autonomously swinging to a goal.
     */
    private boolean isSwinging;

    /**
     * The angle reported by the forearm at its starting position, fully folded.
     */
    private double forearmZero;

    /**
     * The angle reported by the tower at its starting position, fully lowered.
     */
    private double towerZero;

    /**
     * Tower encoder position measured at the beginning of the current task.
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
     * The logger.
     */
    private Logger logger;

    /**
     * The recent history of tower goal deltas, used to control PID.
     */
    private CircularBuffer<Double> deltaHistory;

    /**
     * Whether the tower is currently hanging.
     * When hanging, teleop controls are overriden and the tower clamps downwards with maximum
     * power.
     */
    private boolean hanging;

    /**
     * Constructs a TowerLayer.
     */
    public TowerLayer() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        logger = setupInfo.getLogger("TowerLayer");
        isInit = false;
        hanging = false;
        finishedInit = false;
        tower = setupInfo.getHardwareMap().get(DcMotor.class, "tower_swing");
        tower.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        tower.setDirection(DcMotorSimple.Direction.FORWARD);
        towerZero = tower.getCurrentPosition();
        forearm = setupInfo.getHardwareMap().get(DcMotor.class, "forearm_swing");
        forearm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        forearm.setDirection(DcMotorSimple.Direction.REVERSE);
        forearmZero = forearm.getCurrentPosition();
        //claw = setupInfo.getHardwareMap().get(Servo.class, "claw");
        clawStartTime = 0;
        setupInfo.addUpdateListener(() -> {
            if (isSwinging && checkTowerDone()) {
                isSwinging = false;
                tower.setPower(0.0);
            }
            if (isInit && checkForearmDone()) {
                isInit = false;
                finishedInit = true;
                forearm.setPower(0.0);
                logger.log("Forearm done at angle " + getForearmAngle());
            } else if (isInit) {
                logger.update("forearm angle", getForearmAngle());
            }
        });
    }

    @Override
    public boolean isTaskDone() {
        if (isInit) {
            return checkForearmDone();
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
            isInit = false;
            if (castedTowerTask.getFullRaise()) {
                towerGoalAngle = FULL_RAISE_ANGLE;
            } else if (castedTowerTask.getFullLower()) {
                towerGoalAngle = 0;
            }
            towerStartPos = tower.getCurrentPosition();
        } else if (task instanceof TowerTeleopTask) {
            if (hanging) {
                // Disable input
                return;
            }
            TowerTeleopTask castedTask = (TowerTeleopTask)task;
            isInit = false;
            boolean isUnsafe = getForearmAngle() > FOREARM_MAX_SAFE_ANGLE
                && castedTask.getTowerSwingPower() > 1;
            tower.setPower(isUnsafe ? 0 : castedTask.getTowerSwingPower());
            forearm.setPower(isUnsafe ? 0 : castedTask.getForearmSwingPower());
            logger.update("tower swing power", castedTask.getTowerSwingPower());
        } else if (task instanceof TowerHangTask) {
            TowerHangTask castedTask = (TowerHangTask)task;
            tower.setPower(HANG_POWER);
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }

    /**
     * Checks whether a change in some measured parameter has exceeded a desired change.
     *
     * @param delta - the measured change in the tested parameter.
     * @param goalDelta - the desired change in the parameter.
     * @return Whether delta exceeds goalDelta in magnitude and matches it in direction.
     */
    private boolean checkDelta(double delta, double goalDelta) {
        boolean magExceeded = Math.abs(delta) > Math.abs(goalDelta);
        boolean signMatches = (delta > 0) == (goalDelta > 0);
        return magExceeded && signMatches;
    }

    /**
     * Calculates the angle of the forearm away from its starting position.
     *
     * @return The angle of the forearm relative to {@link #forearmZero}.
     */
    private double getForearmAngle() {
        double revs = (forearm.getCurrentPosition() - forearmZero)
            / forearm.getMotorType().getTicksPerRev() * FOREARM_GEAR_RATIO;
        return Units.convert(revs, Units.Angle.REV, Units.Angle.RAD);
    }

    /**
     * Calculates whether the tower has finished swinging to {@link #towerGoalAngle}.
     *
     * @return Whether the tower has finished its most recent autonomous swing action.
     */
    private boolean checkTowerDone() {
        double revs = (tower.getCurrentPosition() - towerStartPos)
            / tower.getMotorType().getTicksPerRev() * TOWER_GEAR_RATIO;
        double deltaAngle = Units.convert(revs, Units.Angle.REV, Units.Angle.RAD);
        double startAngle = towerStartPos / tower.getMotorType().getTicksPerRev();
        double goalDeltaAngle = towerGoalAngle - startAngle;
        return checkDelta(deltaAngle, goalDeltaAngle);
    }

    /**
     * Calculates whether the forearm has finished swinging to the initialized position.
     *
     * @return Whether the forearm angle has reached {@link #FOREARM_INIT_ANGLE}.
     */
    private boolean checkForearmDone() {
        return checkDelta(getForearmAngle(), FOREARM_INIT_ANGLE);
    }

    /**
     * A top-level layer that emits a TowerForearmTask, needed to prepare the tower for operation.
     * TODO: should be static. The tight connection between InitLayer and TowerLayer is a hacky fix
     * only.
     */
    public final class InitLayer implements Layer {
        /**
         * Whether the initiailization task has been emitted yet.
         */
        private boolean emitted;

        /**
         * Constructs an InitLayer.
         */
        public InitLayer() {
            emitted = false;
        }

        @Override
        public void setup(LayerSetupInfo setupInfo) { }

        @Override
        public boolean isTaskDone() {
            return finishedInit;
        }

        @Override
        public Iterator<Task> update(Iterable<Task> completed) {
            if (emitted) {
                return new ArrayList<Task>().iterator();
            }
            emitted = true;
            return Collections.singleton((Task)new TowerForearmTask()).iterator();
        }

        @Override
        public void acceptTask(Task task) {
            throw new UnsupportedTaskException(this, task);
        }
    }
}

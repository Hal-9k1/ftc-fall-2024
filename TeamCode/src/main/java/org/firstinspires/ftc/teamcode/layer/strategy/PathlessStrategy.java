package org.firstinspires.ftc.teamcode.layer.strategy;

import java.util.ArrayList;

import org.firstinspires.ftc.teamcode.Units;
import org.firstinspires.ftc.teamcode.layer.AbstractQueuedLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.LinearMovementTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TurnTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import org.firstinspires.ftc.teamcode.task.WinTask;

/**
 * Scores the alliance-neutral preset samples on our side of the field.
 * Emits LinearMovementTasks and TurnTasks directly instead of using any pathfinding. This means it
 * is only compatible with holonomic drive systems, subject to encoder drift, and cannot respond to
 * obstacles.
 */
public final class PathlessStrategy extends AbstractQueuedLayer {
    /**
     * The distance in tiles the robot must first travel forward to make it to the other side of the
     * preset samples.
     */
    private static final double FIRST_RUSH_DIST = 2.1;

    /**
     * The distance in tiles the robot must travel forward or backward thereafter to move back
     * almost to the starting wall or to return to the far side of the preset samples after doing
     * so.
     */
    private static final double RUSH_DIST = 2.0;

    /**
     * The distance in tiles the robot must travel sideways to position itself in front of the next
     * preset sample.
     */
    private static final double STRAFE_DIST = 0.25;

    /**
     * The distance in tiles the robot should stop short of RUSH_DIST because it will have to turn
     * and "shove" the sample into the net zone.
     * This extra movement is only needed for the first preset sample; the others will reach the net
     * zone if the robot simply drives backward over them.
     */
    private static final double FIRST_SHORT_STOP = 0.2;

    /**
     * The angle in revolutions the robot should turn before shoving the sample into the net zone.
     */
    private static final double FIRST_TURN_ANGLE = 0.125;

    /**
     * The distance in tiles the robot should "shove" the first preset sample into the net zone at
     * an angle.
     */
    private static final double FIRST_SHOVE_DIST = 0.2;

    /**
     * A list of actions to build.
     */
    private ArrayList<Task> queue;

    /**
     * Constructs a PathlessStrategy.
     */
    public PathlessStrategy() {
        queue = new ArrayList<>();
    }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public void acceptTask(Task task) {
        if (task instanceof WinTask) {
            // Spike closest to submersible
            queue.add(new LinearMovementTask(Units.convert(FIRST_RUSH_DIST, Units.Distance.TILE, Units.Distance.M), 0));
            strafeToNextSpike();
            shoveFirstSpike();
            // Second closest
            queue.add(new LinearMovementTask(Units.convert(RUSH_DIST - FIRST_SHORT_STOP, Units.Distance.TILE, Units.Distance.M), 0));
            strafeToNextSpike();
            queue.add(new LinearMovementTask(Units.convert(-RUSH_DIST, Units.Distance.TILE, Units.Distance.M), 0));
            // Third closest
            queue.add(new LinearMovementTask(Units.convert(RUSH_DIST, Units.Distance.TILE, Units.Distance.M), 0));
            strafeToNextSpike();
            queue.add(new LinearMovementTask(Units.convert(-RUSH_DIST, Units.Distance.TILE, Units.Distance.M), 0));
            setSubtasks(queue);
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }

    private void shoveFirstSpike() {
        queue.add(new LinearMovementTask(Units.convert(-RUSH_DIST + FIRST_SHORT_STOP, Units.Distance.TILE, Units.Distance.M), 0));
        queue.add(new TurnTask(Units.convert(-FIRST_TURN_ANGLE, Units.Angle.REV, Units.Angle.RAD)));
        queue.add(new LinearMovementTask(Units.convert(-FIRST_SHOVE_DIST, Units.Distance.TILE, Units.Distance.M), 0));
        queue.add(new LinearMovementTask(Units.convert(FIRST_SHOVE_DIST, Units.Distance.TILE, Units.Distance.M), 0));
        queue.add(new TurnTask(Units.convert(FIRST_TURN_ANGLE, Units.Angle.REV, Units.Angle.RAD)));
    }

    private void strafeToNextSpike() {
        //queue.add(new LinearMovementTask(0, Units.convert(-STRAFE_DIST, Units.Distance.TILE, Units.Distance.M)));
        queue.add(new TurnTask(Units.convert(-0.25, Units.Angle.REV, Units.Angle.RAD)));
        queue.add(new LinearMovementTask(Units.convert(-STRAFE_DIST, Units.Distance.TILE, Units.Distance.M), 0));
        queue.add(new TurnTask(Units.convert(0.22, Units.Angle.REV, Units.Angle.RAD)));
    }
}

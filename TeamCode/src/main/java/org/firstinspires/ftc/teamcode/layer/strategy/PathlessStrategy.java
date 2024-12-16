package org.firstinspires.ftc.teamcode.layer.strategy;

import java.util.Arrays;

import org.firstinspires.ftc.teamcode.Units;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.layer.QueuedLayer;
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
public final class PathlessStrategy extends QueuedLayer {
    private static final double FIRST_RUSH_DIST = 2.1;

    private static final double RUSH_DIST = 2.0;

    private static final double STRAFE_DIST = 0.25;

    private static final double FIRST_SHORT_STOP = 0.2;

    private static final double FIRST_TURN_ANGLE = 0.125;

    private static final double FIRST_SHOVE_DIST = 0.4;

    public PathlessStrategy() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public void acceptTask(Task task) {
        if (task instanceof WinTask) {
            setSubtasks(Arrays.asList(
                // Spike closest to submersible
                new LinearMovementTask(Units.convert(FIRST_RUSH_DIST, Units.Distance.TILE, Units.Distance.M), 0),
                new LinearMovementTask(0, Units.convert(-STRAFE_DIST, Units.Distance.TILE, Units.Distance.M)),
                new LinearMovementTask(Units.convert(-RUSH_DIST + FIRST_SHORT_STOP, Units.Distance.TILE, Units.Distance.M), 0),
                new TurnTask(Units.convert(-FIRST_TURN_ANGLE, Units.Angle.REV, Units.Angle.RAD)),
                new LinearMovementTask(Units.convert(-FIRST_SHOVE_DIST, Units.Distance.TILE, Units.Distance.M), 0),
                new LinearMovementTask(Units.convert(-FIRST_SHOVE_DIST, Units.Distance.TILE, Units.Distance.M), 0),
                new TurnTask(Units.convert(FIRST_TURN_ANGLE, Units.Angle.REV, Units.Angle.RAD)),
                // Second closest
                new LinearMovementTask(Units.convert(RUSH_DIST - FIRST_SHORT_STOP, Units.Distance.TILE, Units.Distance.M), 0),
                new LinearMovementTask(0, Units.convert(-STRAFE_DIST, Units.Distance.TILE, Units.Distance.M)),
                new LinearMovementTask(Units.convert(-RUSH_DIST, Units.Distance.TILE, Units.Distance.M), 0),
                // Third closest
                new LinearMovementTask(Units.convert(RUSH_DIST, Units.Distance.TILE, Units.Distance.M), 0),
                new LinearMovementTask(0, Units.convert(-STRAFE_DIST, Units.Distance.TILE, Units.Distance.M)),
                new LinearMovementTask(Units.convert(-RUSH_DIST, Units.Distance.TILE, Units.Distance.M), 0)
            ));
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}

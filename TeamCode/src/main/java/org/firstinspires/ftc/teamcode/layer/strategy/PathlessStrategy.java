package org.firstinspires.ftc.teamcode.layer.strategy;

import org.firstinspires.ftc.teamcode.Units;
import org.firstinspires.ftc.teamcode.layer.QueuedLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.LinearMovementTask;
import org.firstinspires.ftc.teamcode.task.TurnTask;
import org.firstinspires.ftc.teamcode.task.WinTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

import java.util.Arrays;

public class PathlessStrategy extends QueuedLayer {
    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public void acceptTask(Task task) {
        if (task instanceof WinTask) {
            double firstRushDist = 2.1;
            double rushDist = 2.0;
            double strafeDist = 0.25;
            double firstShortStop = 0.2;
            double firstTurnAngle = 0.125;
            double firstShoveDist = 0.4;
            setSubtasks(Arrays.asList(
                // Spike closest to submersible
                new LinearMovementTask(Units.convert(firstRushDist, Units.Distance.TILE, Units.Distance.M), 0),
                new LinearMovementTask(0, Units.convert(-strafeDist, Units.Distance.TILE, Units.Distance.M)),
                new LinearMovementTask(Units.convert(-rushDist + firstShortStop, Units.Distance.TILE, Units.Distance.M), 0),
                new TurnTask(Units.convert(-firstTurnAngle, Units.Angle.REV, Units.Angle.RAD)),
                new LinearMovementTask(Units.convert(-firstShoveDist, Units.Distance.TILE, Units.Distance.M), 0),
                new LinearMovementTask(Units.convert(firstShoveDist, Units.Distance.TILE, Units.Distance.M), 0),
                new TurnTask(Units.convert(firstTurnAngle, Units.Angle.REV, Units.Angle.RAD)),
                // Second closest
                new LinearMovementTask(Units.convert(rushDist - firstShortStop, Units.Distance.TILE, Units.Distance.M), 0),
                new LinearMovementTask(0, Units.convert(-strafeDist, Units.Distance.TILE, Units.Distance.M)),
                new LinearMovementTask(Units.convert(-rushDist, Units.Distance.TILE, Units.Distance.M), 0),
                // Third closest
                new LinearMovementTask(Units.convert(rushDist, Units.Distance.TILE, Units.Distance.M), 0),
                new LinearMovementTask(0, Units.convert(-strafeDist, Units.Distance.TILE, Units.Distance.M)),
                new LinearMovementTask(Units.convert(-rushDist, Units.Distance.TILE, Units.Distance.M), 0)
            ));
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}

package org.firstinspires.ftc.teamcode.layer.strategy;

import java.util.ArrayList;
import java.util.Arrays;

import org.firstinspires.ftc.teamcode.Units;
import org.firstinspires.ftc.teamcode.layer.AbstractQueuedLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.IntakeTask;
import org.firstinspires.ftc.teamcode.task.LinearMovementTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TowerForearmTask;
import org.firstinspires.ftc.teamcode.task.TowerTask;
import org.firstinspires.ftc.teamcode.task.TurnTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import org.firstinspires.ftc.teamcode.task.WinTask;

/**
 * Scores alliance-neutral samples into the low basket.
 * Emits LinearMovementTasks and TurnTasks directly instead of using any pathfinding. This means it
 * is only compatible with holonomic drive systems, subject to encoder drift, and cannot respond to
 * obstacles.
 */
public final class TowerStrategy extends AbstractQueuedLayer {
    /**
     * A list of actions built in {@link #acceptTask} that is eventually passed to
     * {@link #setSubtasks}.
     */
    private ArrayList<Task> queue;

    /**
     * Constructs a TowerStrategy.
     */
    public TowerStrategy() {
        queue = new ArrayList<>();
    }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public void acceptTask(Task task) {
        if (task instanceof WinTask) {
            // CSOFF:MagicNumber
            // Forearm unfolded
            // Drive to basket
            queue.add(new LinearMovementTask(Units.convert(12, Units.Distance.IN, Units.Distance.M), 0));
            queue.add(new TurnTask(0.25));
            queue.add(new LinearMovementTask(Units.convert(20, Units.Distance.IN, Units.Distance.M), 0));
            queue.add(new TurnTask(0.125));
            // Score preloaded sample
            score();
            // Acquire and score sample 1
            queue.add(new TurnTask(Units.convert(-135, Units.Angle.DEG, Units.Angle.RAD)));
            queue.add(new TowerTask(false, true));
            queue.add(new IntakeTask(true, false));
            queue.add(new TurnTask(-0.5));
            score();
            setSubtasks(queue);
            // CSON:MagicNumber
        } else if (task instanceof TowerForearmTask) {
            setSubtasks(Arrays.asList(task)); // Forward task to lower layer
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }

    /**
     * Raises the tower and ejects a sample in the intake.
     */
    private void score() {
        queue.add(new TowerTask(true, false));
        queue.add(new IntakeTask(false, true));
    }
}

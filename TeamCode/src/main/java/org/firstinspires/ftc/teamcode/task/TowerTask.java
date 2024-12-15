package org.firstinspires.ftc.teamcode.task;

/**
 * Raises or lowers the arm in an arc.
 */
public class TowerTask implements Task {
    /**
     * Whether the tower should be fully lifted in an arc.
     */
    public boolean raise;
    /**
     * Whether the tower should be fully lowered in an arc.
     */
    public boolean lower;

    /**
     * Constructor for TowerTask
     * @param raise - Whether the tower should be fully lifted in an arc.
     * @param lower - Whether the tower should be fully lowered in an arc.
     */
    public TowerTask(boolean raise, boolean lower) {
        this.raise = raise;
        this.lower = lower;
        if (raise && lower) {
            throw new IllegalArgumentException("The arm can't be both raised and lowered");
        }
    }
}

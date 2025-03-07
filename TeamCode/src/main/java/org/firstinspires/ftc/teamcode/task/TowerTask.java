package org.firstinspires.ftc.teamcode.task;

/**
 * Raises or lowers the arm in an arc.
 */
public class TowerTask implements Task {
    /**
     * Whether the tower should be fully raised in an arc.
     */
    private final boolean fullRaise;

    /**
     * Whether the tower should be fully lowered in an arc.
     */
    private final boolean fullLower;

    /**
     * Constructs a TowerTask.
     *
     * @param fullRaise - Whether the tower should be fully raised in an arc.
     * @param fullLower - Whether the tower should be fully lowered in an arc.
     */
    public TowerTask(boolean fullRaise, boolean fullLower) {
        this.fullRaise = fullRaise;
        this.fullLower = fullLower;
        if (fullRaise && fullLower) {
            throw new IllegalArgumentException("The arm can't be both fully raised and fully lowered");
        }
    }

    /**
     * Returns whether the tower should be fully raised in an arc.
     *
     * @return whether the tower should be fully raised in an arc.
     */
    public boolean getFullRaise() {
        return fullRaise;
    }

    /**
     * Returns whether the tower should be fully lowered in an arc.
     *
     * @return whether the tower should be fully lowered in an arc.
     */
    public boolean getFullLower() {
        return fullLower;
    }
}

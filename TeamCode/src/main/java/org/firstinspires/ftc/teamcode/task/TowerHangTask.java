package org.firstinspires.ftc.teamcode.task;

/**
 * Hang and hold or release the tower forearm.
 */
public class TowerHangTask implements Task{
    /**
     * When true, hangs and holds the forearm.
     */
    private final boolean hang;

    /**
     * When true, releases the forearm.
     */
    private final boolean unhang;

    /**
     * Constructs a TowerHangTask.
     * @param hang - Whether the forearm should be clamped and held still.
     * @param unhang - Whether the forearm should be released.
     */
    public TowerHangTask(boolean hang, boolean unhang) {

        this.hang = hang;
        this.unhang = unhang;

        if (hang && unhang) {
            throw new IllegalArgumentException("Robot cannot hang and unhang at the same time.");
        }
    }

    /**
     * Returns whether the forearm should be clamped and held still.
     * 
     * @return whether the forearm should be clamped and held still.
     */
    public boolean getHang() {
        return hang;
    }

    /**
     * Returns whether the forearm should be released.
     * 
     * @return whether the forearm should be released.
     */
    public boolean getUnhang() {
        return unhang;
    }

}
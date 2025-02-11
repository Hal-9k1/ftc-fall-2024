package org.firstinspires.ftc.teamcode.layer.pathfinding;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.localization.Mat3;
import org.firstinspires.ftc.teamcode.localization.Vec2;
import org.firstinspires.ftc.teamcode.localization.Vec3;
import org.firstinspires.ftc.teamcode.task.Task;

import java.util.Iterator;

public class PathfindingLayer implements Layer {
    private static final double TRAJECTORY_SEARCH_INCREMENT = 0.01;

    private Trajectory currentTrajectory;

    public PathfindingLayer() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) {

    }

    @Override
    public boolean isTaskDone() {

    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {

    }

    @Override
    public void acceptTask(Task task) {

    }

    private double evaluateTrajectory(Trajectory t) {

    }

    private double evaluateTargetAngle(Trajectory t) {

    }

    private double evaluateClearence(Trajectory t) {

    }

    private double evaluateSpeed(Trajectory t) {

    }

    private void calculatePath() {
        Trajectory bestTrajectory;
        double bestScore = Double.NEGATIVE_INFINITY;

        // Creates two Vec3 to represent the top right upper corner of the rectangular search space.
        // Another to represent the bottom left lower corner of the rectangular search space.
        // I don't understand why we need z for this.
        Trajectory maxBounds = new Trajectory(1, 1, 1);
        Trajectory minBounds = new Trajectory(-1, -1, -1);

        // Implement for loop or something.
        for (double a = minBounds.getAxial(); a < maxBounds.getAxial(); a += TRAJECTORY_SEARCH_INCREMENT) {
            for (double l = minBounds.getLateral(); l < maxBounds.getLateral(); l += TRAJECTORY_SEARCH_INCREMENT) {
                for (double y = minBounds.getYaw(); y < maxBounds.getYaw(); y += TRAJECTORY_SEARCH_INCREMENT) {
                    if (!checkDynamicWindow(t)) {
                        continue;
                    }
                    double score = evaluateTrajectory(t);
                    if (score > bestScore) {
                        bestScore = score;
                        bestTrajectory = t;
                    }
                }
            }
        }
        if (bestScore == Double.NEGATIVE_INFINITY) {
            throw new RuntimeException("AAAAAAAAAAAAAAA (panic.)");
        }
        currentTrajectory = bestTrajectory;
    }

    private boolean checkDynamicWindow(Trajectory t) {
        // hit something?
        // axial and lateral and yaw are possible?
    }

    private void addObstacle(Obstacle obstacle) {

    }

    private class Trajectory {
        private double axial;

        private double lateral;

        private double yaw;

        Trajectory(double axial, double lateral, double yaw) {
            this.axial = axial;
            this.lateral = lateral;
            this.yaw = yaw;
        }

        public double getAxial() {
            return axial;
        }

        public double getLateral() {
            return lateral;
        }

        public double getYaw() {
            return yaw;
        }
    }

    private interface Obstacle {
        double getDistanceTo(Vec2 point);
    }

    private class DynObstacle implements Obstacle {
        private Mat3 transform;

        private double size;

        @Override
        public double getDistanceTo(Vec2 point) {

        }
    }

    private class StaticObstacle implements Obstacle {
        private Mat3 transform;

        private Vec2 size;

        @Override
        public double getDistanceTo(Vec2 point) {

        }
    }
}

package org.firstinspires.ftc.teamcode.layer.pathfinding;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.localization.Mat3;
import org.firstinspires.ftc.teamcode.localization.Vec2;
import org.firstinspires.ftc.teamcode.task.Task;

import java.util.Iterator;

public class PathfindingLayer implements Layer {
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

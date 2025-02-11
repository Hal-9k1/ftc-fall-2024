package org.firstinspires.ftc.teamcode.layer.pathfinding;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.localization.Mat3;
import org.firstinspires.ftc.teamcode.localization.Vec2;
import org.firstinspires.ftc.teamcode.task.Task;

import java.util.Iterator;

public class PathfindingLayer implements Layer {
    private static final double GOAL_COMPLETE_EPSILON = 0.01;

    private Mat3 goal;

    private Trajectory currentTrajectory;

    private long lastCalcTime;

    public PathfindingLayer() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) {

    }

    @Override
    public boolean isTaskDone() {
        Mat3 delta = getTransform().inv().mul(goal);
        return delta.getTranslation().len() < GOAL_COMPLETE_EPSILON
            && delta.getDirection().getAngle() < GOAL_COMPLETE_EPSILON;
    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        long nowNano = System.nanoTime();
        if (nowNano - lastCalcTime > Units.convert(CALC_TIME_INTERVAL, Units.Time.SEC, Units.Time.NANO)) {
            calculatePath();
            lastCalcTime = nowNano;
        }
        return Collections.singleton(new HolonomicDriveTask(
            currentTrajectory.getAxial(),
            -currentTrajectory.getLateral(),
            currentTrajectory.getYaw()
        )).iterator();
    }

    @Override
    public void acceptTask(Task task) {
        if (task instanceof MoveToFieldTask) {
            MoveToFieldTask castedTask = (MoveToFieldTask)task;
            goal = castedTask.goal;
            lastCalcTime = -1;
        }
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

    private static class Trajectory {
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

    private static interface Obstacle {
        double getDistanceTo(Vec2 point);
    }

    private static class DynamicObstacle implements Obstacle {
        private Mat3 transform;

        private double size;

        DynamicObstacle(Mat3 transform, double size) {
            this.transform = transform;
            this.size = size;
        }

        @Override
        public double getDistanceTo(Vec2 point) {
            Vec2 delta = transform.getTranslation().add(point.mul(-1));
            double axialProj = transform.getDirection().proj(delta);
            double lateralProj = transform.getDirection().getPerpendicular().proj(delta);
            if (lateralProj < size / 2) {
                return axialProj;
            }
            Vec2 ep1 = transform.mul(new Vec2(0, size / 2));
            Vec2 ep2 = transform.mul(new Vec2(0, -size / 2));
            return Math.min(ep1.add(point.mul(-1)).len(), ep2.add(point.mul(-1)).len());
        }
    }

    private static class StaticObstacle implements Obstacle {
        private Mat3 transform;

        private Vec2 size;

        DynamicObstacle(Mat3 transform, Vec2 size) {
            this.transform = transform;
            this.size = size;
        }

        @Override
        public double getDistanceTo(Vec2 point) {
            Vec2 p = transform.inv().mul(point);
            double m1 = size.getY() / size.getX();
            double m2 = -m1;
            boolean useHeight = Math.signum(p.getY() - m1) == Math.signum(p.getY() - m2);
            double dim = useHeight ? size.getY() : size.getX();
            return p.len() - dim / (2 * Math.cos(p.angle()));
        }
    }
}

package org.firstinspires.ftc.teamcode.layer.pathfinding;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.localization.Mat3;
import org.firstinspires.ftc.teamcode.localization.Vec2;
import org.firstinspires.ftc.teamcode.task.Task;

import java.util.Iterator;

/**
 * Computes holonomic drive powers to pathfind around obstacles to a goal transform.
 */
public class PathfindingLayer implements Layer {
    /**
     * The distance in meters and offset in radians the robot's transform may be from the goal
     * before the robot is considered arrived.
     */
    private static final double GOAL_COMPLETE_EPSILON = 0.01;

    /**
     * The goal robot transform in field space.
     */
    private Mat3 goal;

    /**
     * The current trajectory the robot should take.
     */
    private Trajectory currentTrajectory;

    /**
     * The timestamp in nanoseconds the current trajectory was calculated.
     */
    private long lastCalcTime;

    /**
     * Constructs a PathfindingLayer.
     */
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

    /**
     * Computes a comparable score for a trajectory considering three factors.
     *
     * This is the objective function the dynamic window approach optimizes.
     *
     * @param t - the trajectory to evaluate.
     * @return A comparable score for the trajectory.
     */
    private double evaluateTrajectory(Trajectory t) {

    }

    /**
     * Computes a comparable score for a trajectory on the grounds of final angle to target.
     *
     * @param t - the trajectory to evaluate.
     * @return A comparable score for the trajectory which is higher the more directly the robot
     * would face the goal at the end of the evaluated trajectory.
     */
    private double evaluateTargetAngle(Trajectory t) {

    }


    /**
     * Computes a comparable score for a trajectory on the grounds of minimum clearence to
     * obstacles.
     *
     * @param t - the trajectory to evaluate.
     * @return A comparable score for the trajectory which is higher the greater the minimum
     * clearence the robot has to any obstacle at any point during the evaluated trajectory.
     */
    private double evaluateClearence(Trajectory t) {

    }


    /**
     * Computes a comparable score for a trajectory on the grounds of final speed of the robot.
     *
     * @param t - the trajectory to evaluate.
     * @return A comparable score for the trajectory which is higher the greater the robot's
     * translational velocity at the end of the evaluated trajectory..
     */
    private double evaluateSpeed(Trajectory t) {

    }

    /**
     * Checks if a trajectory is within the dynamic window.
     *
     * During optimization, trajectories not within the dynamic window may be culled from the
     * search.
     *
     * @param t - the trajectory to check.
     * @return Whether the checked trajectory is achievable by the robot and the trajectory would
     * not cause the robot to crash into an obstacle.
     */
    private boolean checkDynamicWindow(Trajectory t) {
        // hit something?
        // axial and lateral and yaw are possible?
    }

    /**
     * Adds an obstacle to the internal list of obstacles.
     *
     * @param obstacle - the obstacle to add.
     */
    private void addObstacle(Obstacle obstacle) {

    }

    /**
     * A set of accelerations that the robot can take.
     *
     * Alternately, a 3D point in the search space of trajectories.
     */
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

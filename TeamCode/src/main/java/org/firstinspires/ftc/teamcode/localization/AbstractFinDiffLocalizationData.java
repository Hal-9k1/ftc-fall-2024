package org.firstinspires.ftc.teamcode.localization;

import java.util.List;

public abstract class AbstractFinDiffLocalizationData implements LocalizationData {
    private final double epsilon;

    public AbstractFinDiffLocalizationData(double epsilon) {
        this.epsilon = epsilon;
    }

    public double getPositionProbabilityDx(Vec2 pos, List<Vec2> ignoreRoots) {
        return (getPositionProbabilityIgnoringRoots(pos.add(new Vec2(epsilon, 0)), ignoreRoots)
            - getPositionProbabilityIgnoringRoots(pos, ignoreRoots)) / epsilon;
    }

    public double getPositionProbabilityDy(Vec2 pos, List<Vec2> ignoreRoots) {
        return (getPositionProbabilityIgnoringRoots(pos.add(new Vec2(0, epsilon)), ignoreRoots)
            - getPositionProbabilityIgnoringRoots(pos, ignoreRoots)) / epsilon;
    }

    public Vec2 getPositionProbabilityDxGradient(Vec2 pos, List<Vec2> ignoreRoots) {
        double z = getPositionProbabilityDx(pos, ignoreRoots);
        double wrtX = (getPositionProbabilityDx(pos.add(new Vec2(epsilon, 0)), ignoreRoots) - z)
            / epsilon;
        double wrtY = (getPositionProbabilityDx(pos.add(new Vec2(0, epsilon)), ignoreRoots) - z)
            / epsilon;
        return new Vec2(wrtX, wrtY);
    }

    public Vec2 getPositionProbabilityDyGradient(Vec2 pos, List<Vec2> ignoreRoots) {
        double z = getPositionProbabilityDy(pos, ignoreRoots);
        double wrtX = (getPositionProbabilityDy(pos.add(new Vec2(epsilon, 0)), ignoreRoots) - z)
            / epsilon;
        double wrtY = (getPositionProbabilityDy(pos.add(new Vec2(0, epsilon)), ignoreRoots) - z)
            / epsilon;
        return new Vec2(wrtX, wrtY);
    }

    public double getRotationProbabilityDx(double rot, List<Double> ignoreRoots) {
        return (getRotationProbabilityIgnoringRoots(rot + epsilon, ignoreRoots)
            - getRotationProbabilityIgnoringRoots(rot, ignoreRoots)) / epsilon;
    }

    public double getRotationProbabilityDx2(double rot, List<Double> ignoreRoots) {
        return (getRotationProbabilityDx(rot + epsilon, ignoreRoots)
            - getRotationProbabilityDx(rot, ignoreRoots)) / epsilon;
    }

    private double getPositionProbabilityIgnoringRoots(Vec2 pos, List<Vec2> ignoreRoots) {
        double ignoreRootFactor = ignoreRoots.stream().map((b) -> {
            double product;
            Vec2 negativeCenter = pos.mul(-1);
            Vec2 epsilonVec = new Vec2(epsilon, epsilon);
            do {
                Vec2 diff = negativeCenter.add(b);
                double factor = 1.0 / (diff.dot(diff) + 1.0) - 1.0;
                product = 1.0 / factor;
                negativeCenter = negativeCenter.add(epsilonVec);
            } while (Double.isNaN(product) || !Double.isFinite(product));
            return product;
        }).reduce(1.0, (a, b) -> a * b);
        return getPositionProbability(pos) * ignoreRootFactor;
    }

    private double getRotationProbabilityIgnoringRoots(double rot, List<Double> ignoreRoots) {
        double ignoreRootFactor = ignoreRoots.stream().reduce(1.0, (a, b) -> {
            double product;
            double x = rot;
            do {
                product = a / (x - b);
                x += epsilon;
            } while (Double.isNaN(product) || !Double.isFinite(product));
            return product;
        });
        return getRotationProbability(rot) * ignoreRootFactor;
    }
}

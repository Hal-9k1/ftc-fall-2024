package org.firstinspires.ftc.teamcode.localization;

import java.util.ArrayList;
import java.util.List;
import java.util.Stream;
import java.util.stream.Collectors;

public final class NewtonRobotLocalizer implements RobotLocalizer {
    private static final int MAX_NEWTON_STEPS = 40;

    private static final int MAX_NEWTON_ROOTS = 10;

    private static final double NEWTON_DISTURBANCE_SIZE = 1;

    private ArrayList<LocalizationSource> sources;

    private Map<LocalizationSource, LocalizationData> cachedData;

    private Vec2 cachedPos;

    private Double cachedRotation;

    public SimpleRobotLocalizer() {
        sources = new ArrayList<>();
        cachedData = new HashMap<>();
    }

    @Override
    public void invalidateCache() {
        cachedData.clear();
        cachedPos = null;
        cachedRotation = null;
    }

    @Override
    public void registerSource(LocalizationSource source) {
        sources.add(source);
    }

    @Override
    public Mat3 resolveTransform() {
        resolve(cachedPos == null, cachedRotation == null);
        return Mat3.fromTransform(Mat2.fromAngle(cachedRotation), cachedPos);
    }

    @Override
    public Vec2 resolvePosition() {
        resolve(cachedPos == null, false);
        return cachedPos;
    }

    @Override
    public double resolveRotation() {
        resolve(false, cachedRotation == null);
        return cachedRotation;
    }

    private void resolve(boolean pos, boolean rot) {
        // TODO: extract Newton's method because it's used here three times
        if (pos && cachedPosition == null) {
            List<LocalizationSource> posSources = sources
                .filter(LocalizationSource::canLocalizePosition);
            List<Vec2> xRoots = new ArrayList<>();
            for (int i = 0; i < MAX_NEWTON_ROOTS; ++i) {
                Vec2 xy = new Vec2(0, 0);
                Vec2 xyMinErr = xy;
                double minErr = Double.POSITIVE_INFINITY;
                for (int j = 0; j < MAX_NEWTON_STEPS + 1; ++j) {
                    double err = posSources
                        .stream()
                        .mapToDouble(src -> getData(src).getPositionProbabilityDx(xy, roots))
                        .sum();
                    if (err < minErr) {
                        xyMinErr = xy;
                        minErr = err;
                    }
                    if (j < MAX_NEWTON_STEPS) {
                        Vec2 grad = posSources
                            .stream()
                            .map(src -> getData(src).getPositionProbabilityDxGradient(xy, roots))
                            .reduce(new Vec2(0, 0), Vec2::add);
                        Vec2 delta = grad.mul(-err / grad.len());
                        if (!delta.isFinite()) {
                            double dir = Math.random() * 2 * Math.PI;
                            delta = new Vec2(
                                Math.cos(dir) * NEWTON_DISTURBANCE_SIZE,
                                Math.sin(dir) * NEWTON_DISTURBANCE_SIZE
                            );
                        }
                        xy = xy.add(delta);
                    }
                }
                xRoots.add(xyMinErr);
            }

            List<Vec2> yRoots = new ArrayList<>();
            for (int i = 0; i < MAX_NEWTON_ROOTS; ++i) {
                Vec2 xy = new Vec2(0, 0);
                Vec2 xyMinErr = xy;
                double minErr = Double.POSITIVE_INFINITY;
                for (int j = 0; j < MAX_NEWTON_STEPS + 1; ++j) {
                    double err = posSources
                        .stream()
                        .mapToDouble(src -> getData(src).getPositionProbabilityDy(xy, roots))
                        .sum();
                    if (err < minErr) {
                        xyMinErr = xy;
                        minErr = err;
                    }
                    if (j < MAX_NEWTON_STEPS) {
                        Vec2 grad = posSources
                            .stream()
                            .map(src -> getData(src).getPositionProbabilityDyGradient(xy, roots))
                            .reduce(new Vec2(0, 0), Vec2::add);
                        Vec2 delta = grad.mul(-err / grad.len());
                        if (!delta.isFinite()) {
                            double dir = Math.random() * 2 * Math.PI;
                            delta = new Vec2(
                                Math.cos(dir) * NEWTON_DISTURBANCE_SIZE,
                                Math.sin(dir) * NEWTON_DISTURBANCE_SIZE
                            );
                        }
                        xy = xy.add(delta);
                    }
                }
                yRoots.add(xyMinErr);
            }

            // Also contains saddle points and extrema in only one variable. We're going to take the
            // maximum of the function at every combination, though, so we don't care.
            Map<Vec2, Double> extrema = new HashMap<>();
            xRoots.forEach(x -> {
                yRoots.forEach(y -> {
                    Vec2 pos = new Vec2(x, y);
                    extrema.put(pos, posSources
                        .stream()
                        .mapToDouble(src -> getData(src).getPositionProbability(pos))
                        .sum()
                    );
                });
            });
            cachedPosition = Collections.max(extrema.entrySet(), Map.Entry.comparingByValue())
                .getValue();
        }
        if (rot && cachedRotation == null) {
            List<LocalizationSource> rotSources = sources
                .filter(LocalizationSource::canLocalizeRotation);
            List<Double> roots = new ArrayList<>();
            for (int i = 0; i < MAX_NEWTON_ROOTS; ++i) {
                double x = 0;
                double xMinErr = x;
                double minErr = Double.POSITIVE_INFINITY;
                for (int j = 0; j < MAX_NEWTON_STEPS + 1; ++j) {
                    double err = rotSources
                        .stream()
                        .mapToDouble(src -> getData(src).getRotationProbabilityDx(x, roots))
                        .sum();
                    if (err < minErr) {
                        xMinErr = x;
                        minErr = err;
                    }
                    if (j < MAX_NEWTON_STEPS) {
                        double slope = rotSources
                            .stream()
                            .mapToDouble(src -> getData(src).getRotationProbabilityDx2(x, roots))
                            .sum();
                        double delta = -err / slope;
                        if (!Double.isFinite(delta)) {
                            // Randomly disturb
                            delta = Math.signum(Math.random() - 0.5) * NEWTON_DISTURBANCE_SIZE;
                        }
                        x += delta;
                    }
                }
                roots.add(xMinErr);
            }
            Map<Double, Double> extrema = new HashMap<>();
            roots.forEach(x -> extrema.put(rotSources
                .stream()
                .mapToDouble(src -> getData(src).getRotationProbability(x))
                .sum()
            ));
            cachedRotation = Collections.max(extrema.entrySet(), Map.Entry.comparingByValue())
                .getValue();
        }
    }

    private LocalizationData getData(LocalizationSource source) {
        if (!cachedData.containsKey(source)) {
            cachedData.put(source, source.collectData());
        }
        return cachedData.get(source);
    }
}

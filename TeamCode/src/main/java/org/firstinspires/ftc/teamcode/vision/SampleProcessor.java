package org.firstinspires.ftc.teamcode.vision;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Mat4;
import org.firstinspires.ftc.teamcode.vision.AbstractCameraModule;

public class SampleProcessor implements VisionProcessor {
    private static double HUE_VAR = 0.1;

    private static double SAT_MIN = 0.5;

    private static double SAT_MAX = 1.0;

    private static double VAL_MIN = 0.8;

    private static double VAL_MAX = 1.0;

    private List<SampleDetection> detectionList;

    private Mat cameraMatrix;

    public List<SampleDetection> getDetections() {
        return detectionList;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        detectionList = new ArrayList<>();
        // Populate cameraMatrix, see AprilTagProcessor's implementation
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Mat alphaStrippedFrame = new Mat();
        Mat hsvFrame = new Mat();
        Imgproc.cvtColor(frame, alphaStrippedFrame, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(alphaStrippedFrame, hsvFrame, Imgproc.COLOR_RGB2HSV);
        List<SampleDetection> newDetections = new ArrayList<>();
        for (SampleDetection.Color color : SampleDetection.Color.values()) {
            newDetections.addAll(detectSamples(hsvFrame, color));
        }
        detectionList = newDetections;
        return null;
    }

    @Override
    public void onDrawFrame(
        Canvas canvas,
        int onscreenWidth,
        int onscreenHeight,
        float scaleBmpPxToCanvasPx,
        float scaleCanvasDensity,
        Object userContext
    ) { }

    private List<SampleDetection> detectSamples(Mat frame, SampleDetection.Color color) {
        // Placeholder-heavy code inbound...
        Mat mask = new Mat();
        Core.inRange(
            frame,
            new Scalar(color.getHue() - HUE_VAR, SAT_MIN, VAL_MIN),
            new Scalar(color.getHue() + HUE_VAR, SAT_MAX, VAL_MAX),
            mask
        );

        // Might be unnecessary. Test first.
        //Imgproc.morphologyEx(
        //  mask,
        //  mask,
        //  MORPH_CLOSE,
        //  Imgproc.getStructuringElement(MORPH_RECT, {3, 3})
        //);

        Mat cornerMask = new Mat();
        Imgproc.cornerHarris(mask, cornerMask, 2, 3, 0.04); // Figure out what these values mean
        // Use blob detection maybe? Get corners as points somehow
        MatOfPoint2f corners = new MatOfPoint2f();

        // Group corners by blob and order them somehow so they line up with the 3d points mat

        // Do the following for each grouping
        MatOfPoint3f objectPoints = new MatOfPoint3f(); // Fill this
        Mat rotationVec = new Mat();
        Mat translationVec = new Mat();
        Calib3d.solvePnP(
            objectPoints,
            corners,
            cameraMatrix,
            new MatOfDouble(), // Find out what distort coeffs are and if it's ok to leave it empty
            rotationVec,
            translationVec
        );

        // Convert rotationVec and translationVec into camera space transformation matrix somehow
        Mat4 sampleTfmCamSpace = Mat4.identity();

        // Add all detections
        ArrayList<SampleDetection> detections = new ArrayList<>();
        detections.add(new SampleDetection(sampleTfmCamSpace, color));

        return detections;
    }
}

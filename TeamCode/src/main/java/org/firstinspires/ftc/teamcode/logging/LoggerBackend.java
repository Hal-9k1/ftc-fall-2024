package org.firstinspires.ftc.teamcode.logging;

import java.net.Socket;
import java.util.List;
import java.nio.charset.Charset;
import java.io.Closeable;

import org.firstinspires.ftc.teamcode.matrix.Vec2;
import org.firstinspires.ftc.teamcode.matrix.Mat3;

public interface LoggerBackend extends Closeable {
    void sendPosition(String label, Vec2 position);

    void sendVector(String label, String attachLabel, Vec2 position);

    void sendTransform(String label, String attachLabel, Mat3 transform);

    void sendUpdatableObject(String label, Object object);

    void sendLogs(List<Log> logs);
}

package org.firstinspires.ftc.teamcode.dusk;

import java.net.Socket;
import java.util.List;
import java.nio.charset.Charset;
import java.io.Closeable;

import org.firstinspires.ftc.teamcode.localization.Vec2;
import org.firstinspires.ftc.teamcode.localization.Mat3;

public interface DuskClient extends Closeable {
    void connect();
    void sendPosition(String label, Vec2 position);
    void sendVector(String label, String attachLabel, Vec2 position);
    void sendTransform(String label, String attachLabel, Mat3 transform);
    void sendUpdatableObject(String label, Object object);
    void sendLogs(List<Log> logs);
}

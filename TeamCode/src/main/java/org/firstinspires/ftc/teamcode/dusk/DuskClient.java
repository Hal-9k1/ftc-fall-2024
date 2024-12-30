package org.firstinspires.ftc.teamcode.dusk;

public interface DuskClient extends Closeable {
    void connect();
    void sendPosition(String label, Vec2 position);
    void sendVector(String label, String attachLabel, Vec2 position);
    void sendTransform(String label, String attachLabel, Mat3 transform);
    void sendUpdatableObject(String label, Object object);
    void sendLogs(List<Log> logs);
}

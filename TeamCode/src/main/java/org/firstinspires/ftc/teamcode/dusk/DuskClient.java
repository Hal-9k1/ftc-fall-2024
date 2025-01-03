package org.firstinspires.ftc.teamcode.dusk;

import java.net.Socket;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;

import org.firstinspires.ftc.teamcode.matrix.Vec2;
import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.logging.LoggerBackend;
import org.firstinspires.ftc.teamcode.logging.Log;

public class DuskClient implements LoggerBackend {
    private static final String HOSTNAME = "Callisto.local";

    private static final int PORT = 22047;

    private static final byte TYPE_POS = 1;

    private static final byte TYPE_VEC = 2;

    private static final byte TYPE_TFM = 3;

    private static final byte TYPE_UPD = 4;

    private static final byte TYPE_LOG = 5;

    private Socket socket;

    private OutputStream stream;

    private boolean failed;

    public DuskClient() { }

    @Override
    public void close() {
        if (socket != null) {
            socket.close();
        }
    }

    @Override
    public void sendPosition(String label, Vec2 position) {
        stream.write(TYPE_POS);
        writeString(label);
        writeDouble(position.getX());
        writeDouble(position.getY());
    }

    @Override
    public void sendVector(String label, String attachLabel, Vec2 vector) {
        stream.write(TYPE_VEC);
        writeString(label);
        writeString(attachLabel);
        writeDouble(vector.getX());
        writeDouble(vector.getY());
    }

    @Override
    public void sendTransform(String label, String attachLabel, Mat3 transform) {
        stream.write(TYPE_VEC);
        writeString(label);
        writeString(attachLabel);
        writeDouble(transform.elem(0, 0));
        writeDouble(transform.elem(1, 0));
        writeDouble(transform.elem(2, 0));
        writeDouble(transform.elem(0, 1));
        writeDouble(transform.elem(1, 1));
        writeDouble(transform.elem(2, 1));
        // Don't care about the third row
    }

    @Override
    public void sendUpdatableObject(String label, Object object) {
        stream.write(TYPE_UPD);
        writeString(label);
        writeString(object == null ? "<null>" : object.toString());
    }

    @Override
    public void sendLogs(List<Log> logs) {
        stream.write(TYPE_LOG);
        stream.write(logs.size() > 255 ? 0 : logs.size());
        logs.forEach(log -> {
            stream.write(log.serialize());
        });
        if (logs.size() > 255) {
            stream.write(0);
        }
    }

    public void connect() {
        socket = new Socket(HOSTNAME, PORT);
        stream = socket.getOutputStream();
    }

    private void writeString(String str) {
        if (str == null) {
            stream.write(0);
        } else {
            stream.write(str.length());
            stream.write(str.getBytes(StandardCharsets.US_ASCII));
        }
    }

    private void writeDouble(double num) {
        for (long bits = Double.doubleToLongBits(num); bits != 0; bits >>= 8) {
            stream.write((int)bits); // Only sends LSB
        }
    }
}

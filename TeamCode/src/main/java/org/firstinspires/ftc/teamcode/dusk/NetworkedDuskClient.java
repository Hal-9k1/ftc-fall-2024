package org.firstinspires.ftc.teamcode.dusk;

import java.net.Socket;

public class NetworkedDuskClient implements DuskClient {
    private static final String HOSTNAME = "Callisto.local";

    private static final int PORT = 22047;

    private static final byte TYPE_POS = 1;

    private static final byte TYPE_VEC = 1;

    private static final byte TYPE_TFM = 1;

    private static final byte TYPE_UPD = 1;

    private static final byte TYPE_LOG = 1;

    private Socket socket;

    private OutputStream stream;

    private Charset encoding;

    public NetworkedDuskClient() {
        encoding = Charset.forName("US-ASCII");
    }

    @Override
    public void close() {
        if (socket != null) {
            socket.close();
        }
    }

    @Override
    public void connect() {
        socket = new Socket(HOSTNAME, PORT);
        stream = socket.getOutputStream();
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
        writeDouble(position.getX());
        writeDouble(position.getY());
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
        stream.write(logs.length > 255 ? 0 : logs.length);
        logs.forEach(log -> {
            stream.write(log.serialize());
        });
        if (logs.length > 255) {
            stream.write(0);
        }
    }

    private void sendString(String str) {
        if (str == null) {
            stream.write(0);
        } else {
            stream.write(str.length);
            stream.write(str.getBytes(encoding));
        }
    }

    private void sendDouble(double num) {
        for (long bits = Double.doubleToLongBits(num); bits != 0; bits >>= 8) {
            stream.write(bits & 0xff);
        }
    }
}

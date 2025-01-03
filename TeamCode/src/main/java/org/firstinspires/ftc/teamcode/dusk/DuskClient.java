package org.firstinspires.ftc.teamcode.dusk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.firstinspires.ftc.teamcode.logging.Log;
import org.firstinspires.ftc.teamcode.logging.LoggerBackend;
import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;

public final class DuskClient implements LoggerBackend {
    private static final String HOSTNAME = "Callisto.local";

    private static final int PORT = 22047;

    private static final int RECONNECT_TIMEOUT = 1000;

    private static final byte TYPE_POS = 1;

    private static final byte TYPE_VEC = 2;

    private static final byte TYPE_TFM = 3;

    private static final byte TYPE_UPD = 4;

    private static final byte TYPE_LOG = 5;

    private Socket socket;

    private ByteArrayOutputStream stream;

    private OutputStream socketStream;

    private BlockingQueue<byte[]> packetsInFlight;

    private Thread thread;

    private boolean closing;

    public DuskClient() {
        closing = false;
        thread = null;
        packetsInFlight = new LinkedBlockingQueue<>();
    }

    public void start() {
        if (thread != null) {
            throw new IllegalStateException("DuskClient already started.");
        }
        thread = new Thread(this::connectLoop);
        thread.start();
    }

    @Override
    public void close() {
        if (thread == null) {
            throw new IllegalStateException("DuskClient not started.");
        }
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            // This shouldn't happen since we're the main thread.
        }
    }

    @Override
    public void sendPosition(String label, Vec2 position) {
        stream.write(TYPE_POS);
        writeString(label);
        writeDouble(position.getX());
        writeDouble(position.getY());
        finishPacket();
    }

    @Override
    public void sendVector(String label, String attachLabel, Vec2 vector) {
        stream.write(TYPE_VEC);
        writeString(label);
        writeString(attachLabel);
        writeDouble(vector.getX());
        writeDouble(vector.getY());
        finishPacket();
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
        finishPacket();
    }

    @Override
    public void sendUpdatableObject(String label, Object object) {
        stream.write(TYPE_UPD);
        writeString(label);
        writeString(object == null ? "<null>" : object.toString());
        finishPacket();
    }

    @Override
    public void sendLogs(List<Log> logs) {
        stream.write(TYPE_LOG);
        stream.write(logs.size() > Byte.MAX_VALUE ? 0 : logs.size());
        logs.forEach(log -> {
            byte[] bytes = log.serialize();
            // Avoid inherited write signature that throws IOException
            stream.write(bytes, 0, bytes.length);
        });
        if (logs.size() > Byte.MAX_VALUE) {
            stream.write(0);
        }
        finishPacket();
    }

    private void connect() throws IOException {
        socket = new Socket(HOSTNAME, PORT);
        socketStream = socket.getOutputStream();
    }

    private void writeString(String str) {
        if (str == null) {
            stream.write(0);
        } else {
            stream.write(str.length());
            byte[] bytes = str.getBytes(StandardCharsets.US_ASCII);
            stream.write(bytes, 0, bytes.length);
        }
    }

    private void writeDouble(double num) {
        for (long bits = Double.doubleToLongBits(num); bits != 0; bits >>= Byte.SIZE) {
            stream.write((int)bits); // Only sends LSB
        }
    }

    private void finishPacket() {
        packetsInFlight.offer(stream.toByteArray());
        stream.reset();
    }

    private void connectLoop() {
        while (!closing) {
            try {
                connect();
                packetPumpLoop();
            } catch (IOException | InterruptedException e) {
                // Do nothing, just reconnect (or stop if closing)
            } finally {
                try {
                    socket.close();
                } catch (IOException | NullPointerException e) {
                    // We at least tried to close gracefully, so do nothing
                }
            }
            if (!closing) {
                try {
                    Thread.sleep(RECONNECT_TIMEOUT);
                } catch (InterruptedException e) {
                    // Stopped while reconnecting, so do nothing
                }
            }
        }
    }

    private void packetPumpLoop() throws IOException, InterruptedException {
        while (!closing) {
            socketStream.write(packetsInFlight.take());
        }
    }
}

package org.firstinspires.ftc.teamcode.dusk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.firstinspires.ftc.teamcode.IOUtil;
import org.firstinspires.ftc.teamcode.logging.Log;
import org.firstinspires.ftc.teamcode.logging.LoggerBackend;
import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;

/**
 * Sends logging data to a Dusk server running on the same network as the robot.
 * Not for use in competitions.
 */
public final class DuskClient implements LoggerBackend {
    /**
     * The hostname of the machine running the Dusk server on this network.
     */
    private static final String HOSTNAME = "Callisto.local";

    /**
     * The port of the Dusk server on the listening machine.
     */
    private static final int PORT = 22047;

    /**
     * The delay in milliseconds between disconnection and reconnect attempts.
     */
    private static final int RECONNECT_TIMEOUT = 1000;

    /**
     * Packet type for reporting optionally labeled absolute positions.
     */
    private static final byte TYPE_POS = 1;

    /**
     * Packet type for reporting optionally labeled and anchored vectors.
     */
    private static final byte TYPE_VEC = 2;

    /**
     * Packet type for reporting optionally labeled and anchored transforms.
     */
    private static final byte TYPE_TFM = 3;

    /**
     * Packet type for reporting updatable values.
     */
    private static final byte TYPE_UPD = 4;

    /**
     * Packet type for formatted log messages.
     */
    private static final byte TYPE_LOG = 5;

    /**
     * The socket used to communicate with the server.
     */
    private Socket socket;

    /**
     * The stream used to write data to a staging buffer on the calling thread before passing it to
     * the networking thread.
     */
    private ByteArrayOutputStream stream;

    /**
     * The stream used to write data to the socket by the networking thread.
     */
    private OutputStream socketStream;

    /**
     * A queue of packets to send to the server.
     */
    private BlockingQueue<byte[]> packetsInFlight;

    /**
     * The networking thread that sends packets to the server as logging data is offered by callers.
     * When set to null, the thread should terminate if it is still running.
     */
    private volatile Thread networkThread;

    /**
     * Constructs a DuskClient.
     */
    public DuskClient() {
        networkThread = null;
        packetsInFlight = new LinkedBlockingQueue<>();
    }

    /**
     * Starts the networking thread.
     */
    public void start() {
        if (networkThread != null) {
            throw new IllegalStateException("DuskClient already started.");
        }
        networkThread = new Thread(this::connectLoop);
        networkThread.start();
    }

    @Override
    public void close() {
        if (networkThread == null) {
            throw new IllegalStateException("DuskClient not started.");
        }
        networkThread.interrupt();
        try {
            networkThread.join();
        } catch (InterruptedException e) {
            // This shouldn't happen since we're the main thread.
        }
    }

    @Override
    public void processPosition(String loggerLabel, String itemLabel, Vec2 position) {
        stream.write(TYPE_POS);
        writeString(loggerLabel);
        writeString(itemLabel);
        writeDouble(position.getX());
        writeDouble(position.getY());
        finishPacket();
    }

    @Override
    public void processVector(String loggerLabel, String itemLabel, String attachLabel, Vec2 vector) {
        stream.write(TYPE_VEC);
        writeString(loggerLabel);
        writeString(itemLabel);
        writeString(attachLabel);
        writeDouble(vector.getX());
        writeDouble(vector.getY());
        finishPacket();
    }

    @Override
    public void processTransform(String loggerLabel, String itemLabel, String attachLabel, Mat3 transform) {
        stream.write(TYPE_VEC);
        writeString(loggerLabel);
        writeString(itemLabel);
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
    public void processUpdatableObject(String loggerLabel, String itemLabel, Object object) {
        stream.write(TYPE_UPD);
        writeString(loggerLabel);
        writeString(itemLabel);
        writeString(object == null ? "<null>" : object.toString());
        finishPacket();
    }

    @Override
    public void processLog(Log log) {
        stream.write(TYPE_LOG);
        try {
            log.writeTo(stream);
        } catch (IOException e) {
            // Should never happen because stream is a ByteArrayOutputStream
        }
        finishPacket();
    }

    /**
     * Attempts to connect to the server via TCP and set up the OutputStream.
     * @throws IOException - an I/O error occurred when trying to connect to the server.
     */
    private void connect() throws IOException {
        socket = new Socket(HOSTNAME, PORT);
        socketStream = socket.getOutputStream();
    }

    /**
     * Writes a length-prefixed or null-terminated string to the staging buffer.
     * @param str - If not null, the ASCII-encoded string to write.
     * @see IOUtil#writeFlexibleString
     */
    private void writeString(String str) {
        try {
            IOUtil.writeFlexibleString(stream, str);
        } catch (IOException e) {
            // Should never happen because stream is a ByteArrayOutputStream
        }
    }

    /**
     * Writes a double to the staging buffer as 4 bytes, little-endian.
     * @param num - the double to write.
     */
    private void writeDouble(double num) {
        for (long bits = Double.doubleToLongBits(num); bits != 0; bits >>= Byte.SIZE) {
            stream.write((int)bits); // Only sends LSB
        }
    }

    /**
     * Moves the contents of the staging buffer to a queue of packets to be sent by the networking
     * thread.
     */
    private void finishPacket() {
        packetsInFlight.offer(stream.toByteArray());
        stream.reset();
    }

    /**
     * Maintains a connection to the server and attempts to recover from disconnections and I/O
     * errors, stopping only when {@link #close} is called.
     */
    private void connectLoop() {
        Thread thisThread = Thread.currentThread();
        while (networkThread == thisThread) {
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
            if (!networkThread) {
                try {
                    Thread.sleep(RECONNECT_TIMEOUT);
                } catch (InterruptedException e) {
                    // Stopped while reconnecting, so do nothing
                }
            }
        }
    }

    /**
     * Sends packets from the queue to the server until {@link #close} is called or an I/O error
     * occurs.
     * @throws IOException - an I/O error occurred when writing to the socket.
     * @throws InterruptedException - the networking thread was interrupted while waiting for a
     * packet to send.
     */
    private void packetPumpLoop() throws IOException, InterruptedException {
        Thread thisThread = Thread.currentThread();
        while (networkThread == thisThread) {
            socketStream.write(packetsInFlight.take());
        }
    }
}

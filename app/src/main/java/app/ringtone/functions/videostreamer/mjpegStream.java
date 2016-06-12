package app.ringtone.functions.videostreamer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class mjpegStream extends DataInputStream {
    private static final int FRAME_MAX_LENGTH = 40100;
    private static final int HEADER_MAX_LENGTH = 100;
    private static final String TAG = "MjpegStream";
    private final String CONTENT_LENGTH;
    private final byte[] EOF_MARKER;
    private final byte[] SOI_MARKER;
    private int mContentLength;

    public mjpegStream(InputStream in) {
        super(new BufferedInputStream(in, FRAME_MAX_LENGTH));
        this.SOI_MARKER = new byte[]{(byte) -1, (byte) -40};
        this.EOF_MARKER = new byte[]{(byte) -1, (byte) -39};
        this.CONTENT_LENGTH = "Content-Length";
        this.mContentLength = -1;
    }

    private int getEndOfSeqeunce(DataInputStream in, byte[] sequence) throws IOException {
        int seqIndex = 0;
        for (int i = 0; i < FRAME_MAX_LENGTH; i++) {
            if (((byte) in.readUnsignedByte()) == sequence[seqIndex]) {
                seqIndex++;
                if (seqIndex == sequence.length) {
                    return i + 1;
                }
            } else {
                seqIndex = 0;
            }
        }
        return -1;
    }

    private int getStartOfSequence(DataInputStream in, byte[] sequence) throws IOException {
        int end = getEndOfSeqeunce(in, sequence);
        return end < 0 ? -1 : end - sequence.length;
    }

    private int parseContentLength(byte[] headerBytes) throws IOException, NumberFormatException {
        ByteArrayInputStream headerIn = new ByteArrayInputStream(headerBytes);
        Properties props = new Properties();
        props.load(headerIn);
        return Integer.parseInt(props.getProperty("Content-Length"));
    }

    public Bitmap readMjpegFrame() throws IOException {
        mark(FRAME_MAX_LENGTH);
        int headerLen = getStartOfSequence(this, this.SOI_MARKER);
        reset();
        byte[] header = new byte[headerLen];
        readFully(header);
        try {
            this.mContentLength = parseContentLength(header);
        } catch (NumberFormatException nfe) {
            nfe.getStackTrace();
            Log.d(TAG, "catch NumberFormatException hit", nfe);
            this.mContentLength = getEndOfSeqeunce(this, this.EOF_MARKER);
        }
        reset();
        byte[] frameData = new byte[this.mContentLength];
        skipBytes(headerLen);
        readFully(frameData);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(frameData));
    }
}

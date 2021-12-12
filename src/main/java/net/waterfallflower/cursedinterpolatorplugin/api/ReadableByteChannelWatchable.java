package net.waterfallflower.cursedinterpolatorplugin.api;

import com.intellij.openapi.progress.ProgressIndicator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class ReadableByteChannelWatchable implements ReadableByteChannel {

    final ReadableByteChannel target;
    final ProgressIndicator indicator;
    final int fileSize;
    int size;

    public ReadableByteChannelWatchable(ReadableByteChannel t, ProgressIndicator i, int s) {
        this.target = t;
        this.indicator = i;
        this.fileSize = s;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        int i = target.read(dst);

        if(i > 0) {
            size += i;
            indicator.setFraction(size / fileSize);
        }

        return i;
    }

    @Override
    public boolean isOpen() {
        return target.isOpen();
    }

    @Override
    public void close() throws IOException {
        target.close();
    }
}

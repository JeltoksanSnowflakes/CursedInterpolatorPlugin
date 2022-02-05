package net.waterfallflower.cursedinterpolatorplugin.api.network;

import com.intellij.openapi.progress.ProgressIndicator;
import net.glasslauncher.common.FileUtils;
import net.waterfallflower.cursedinterpolatorplugin.api.io.ReadableByteChannelWatchable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DownloadFileRunnable implements Runnable {

    final ProgressIndicator indicator;
    final String urlString;
    final String pathString;
    final String md5;
    final String filename;

    public DownloadFileRunnable(ProgressIndicator indicator, String urlString, String pathString, String md5, String filename) {
        this.indicator = indicator;
        this.urlString = urlString;
        this.pathString = pathString;
        this.md5 = md5;
        this.filename = filename;
    }

    @Override
    public void run() {
        URLConnection urlConnection;
        File point;
        //Validate URL.
        try {
            urlConnection = new URL(urlString).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //Validate File.
        try {
            (new File(pathString)).mkdirs();
            point = new File(pathString + "/" + filename);
            if (md5 != null && point.exists() && FileUtils.getFileChecksum(MessageDigest.getInstance("MD5"), point).equalsIgnoreCase(md5))
                return;

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }

        try {
            ReadableByteChannel readableByteChannel = new ReadableByteChannelWatchable(Channels.newChannel(urlConnection.getInputStream()), indicator, urlConnection.getContentLength());
            FileOutputStream fileOutputStream = new FileOutputStream(point);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            readableByteChannel.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

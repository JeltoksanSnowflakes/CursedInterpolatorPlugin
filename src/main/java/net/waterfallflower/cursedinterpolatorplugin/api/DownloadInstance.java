package net.waterfallflower.cursedinterpolatorplugin.api;

import com.intellij.openapi.progress.ProgressIndicator;
import net.glasslauncher.common.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DownloadInstance {

    final ProgressIndicator indicator;

    public DownloadInstance(ProgressIndicator indicator) {
        this.indicator = indicator;
    }

    public boolean downloadFile(String urlString, String pathString, String md5, String filename) {
        URLConnection urlConnection = null;
        File point;
        //Validate URL.
        try {
            urlConnection = new URL(urlString).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //Validate File.
        try {
            (new File(pathString)).mkdirs();
            point = new File(pathString + "/" + filename);
            if (md5 != null && point.exists() && FileUtils.getFileChecksum(MessageDigest.getInstance("MD5"), point).equalsIgnoreCase(md5))
                return true;

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }

        try {
            ReadableByteChannel readableByteChannel = new ReadableByteChannelWatchable(Channels.newChannel(urlConnection.getInputStream()), indicator, urlConnection.getContentLength());
            FileOutputStream fileOutputStream = new FileOutputStream(point);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            readableByteChannel.close();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            //Cringe url.
            e.printStackTrace();
            return false;
        }

    }
}

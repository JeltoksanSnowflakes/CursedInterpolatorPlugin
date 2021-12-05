package net.waterfallflower.cursedinterpolatorplugin.api;

import com.intellij.openapi.progress.ProgressIndicator;
import net.glasslauncher.common.CommonConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

import static net.glasslauncher.common.FileUtils.getFileChecksum;

public class DownloadInstance {

    final ProgressIndicator indicator;

    public DownloadInstance(ProgressIndicator indicator) {
        this.indicator = indicator;
    }

    public boolean downloadFile(String urlStr, String pathStr, String md5, String filename) {
        URL url;
        try {
            url = new URL(urlStr);
        } catch (Exception e) {
            CommonConfig.getLogger().info("Failed to download file \"" + urlStr + "\": Invalid URL.");
            e.printStackTrace();
            return false;
        }
        File file;
        try {
            (new File(pathStr)).mkdirs();
            file = new File(pathStr + "/" + filename);
            if (md5 != null && file.exists() && getFileChecksum(MessageDigest.getInstance("MD5"), file).toLowerCase().equals(md5.toLowerCase())) {
                return true;
            }
        } catch (Exception e) {
            CommonConfig.getLogger().info("Failed to download file \"" + urlStr + "\": Invalid path.");
            e.printStackTrace();
            return false;
        }

        try {
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
            FileOutputStream fileOS = new FileOutputStream(file);
            byte[] data = new byte[1024];
            int byteContent;

            int bytesRead = -1;
            long totalBytesRead = 0;
            long fileSize = connection.getContentLength();

            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
                totalBytesRead += bytesRead;
                indicator.setFraction(totalBytesRead / fileSize);
            }

            fileOS.close();
        } catch (Exception e) {
            CommonConfig.getLogger().info("Failed to download file \"" + urlStr + "\":");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

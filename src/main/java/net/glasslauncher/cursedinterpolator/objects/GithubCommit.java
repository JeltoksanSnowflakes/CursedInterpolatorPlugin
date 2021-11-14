package net.glasslauncher.cursedinterpolator.objects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GithubCommit {


    public String sha;

    public static List<GithubCommit> getCommits(String repo) {
        try(InputStream inputStream = new URL("https://api.github.com/repos/" + repo + "/commits").openStream()) {
            return new Gson().fromJson(new InputStreamReader(inputStream), new TypeToken<List<GithubCommit>>() {}.getType());
        } catch (IOException e) {
            System.out.println("bald url exception");
            e.printStackTrace();
            return null;
        }
    }
}

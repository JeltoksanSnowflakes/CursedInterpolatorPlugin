package net.glasslauncher.cursedinterpolator.objects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class GithubCommit {

    public String sha;

    @SerializedName("html_url")
    public String htmlUrl;

    public static List<GithubCommit> getCommits(String repo) throws IOException {
        List<GithubCommit> versions;
        Gson gson = new Gson();

        URL githubURL = new URL("https://api.github.com/repos/" + repo + "/commits");
        InputStream githubStream = githubURL.openStream();

        versions = gson.fromJson(new InputStreamReader(githubStream), new TypeToken<List<GithubCommit>>() {}.getType());

        return versions;
    }
}

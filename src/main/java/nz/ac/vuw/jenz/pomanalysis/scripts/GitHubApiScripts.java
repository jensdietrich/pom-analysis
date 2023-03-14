package nz.ac.vuw.jenz.pomanalysis.scripts;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class GitHubApiScripts {

    static String password = System.getenv("GITHUB_TOKEN");
    static String username = System.getenv("GITHUB_USER");

    public static void main(String[] args) {
        // search for all users in data/github-selected-users.txt
        // download files
    }

    public static List<String> searchForPoms(String owner) {
        List<String> results = new ArrayList<>();
        String search_query = URLEncoder.encode("\\<relocation\\>  filename:pom.xml user:" + owner);
        String url = "https://api.github.com/search/code?q=" + search_query + "&per_page=1000";

        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            while (url != null) {
                System.out.println(url);
                HttpGet request = new HttpGet(url);
                request.setHeader("Accept", "application/vnd.github.v3+json");
                request.setHeader("User-Agent", "pom-analysis/1.0 (Java)");
                request.setHeader("Authorization", "Bearer " + password);

                HttpResponse response = httpClient.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    JSONObject json = new JSONObject(responseBody);

                    JSONArray res = json.getJSONArray("items");
                    for(int i = 0; i < res.length(); i++) {
                        JSONObject item = (JSONObject) res.get(i);
                        String path = item.getString("path");
                        String fullName = item.getJSONObject("repository").getString("full_name");
                        results.add(owner + "\t" + fullName + "\t" + path);
                    }

                    String linkHeader = null;
                    url = null;
                    Header[] linkHeaders = response.getHeaders("Link");
                    if (linkHeaders.length != 0) {
                        linkHeader = linkHeaders[0].getValue();
                        String[] links = linkHeader.split(", ");
                        for (String link : links) {
                            if (link.contains("rel=\"next\"")) {
                                url = link.split(";")[0].substring(1, link.split(";")[0].length() - 1);
                                break;
                            }
                        }
                    }

                    Header[] headers = response.getHeaders("X-RateLimit-Reset");
                    long rateLimitReset = Long.parseLong((headers[0].getValue()));
                    headers = response.getHeaders("X-RateLimit-Remaining");
                    int rateLimitRemaining = Integer.parseInt(headers[0].getValue());
                    long now = System.currentTimeMillis();
                    while (now <= rateLimitReset && rateLimitRemaining < 2) {
                        Thread.sleep(3000);
                        now = System.currentTimeMillis();
                    }

                } else {
                    System.out.println("Request failed: " + response.getStatusLine().getReasonPhrase());
                    String responseBody = EntityUtils.toString(response.getEntity());
                    Thread.sleep(3000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;

    }


    public static void downloadPom(String owner, String repo, String path, String output) {

        try {
            HttpClient httpClient = HttpClientBuilder.create().build();

            String urlStr = "https://api.github.com/repos/%s/%s/contents/%s";
            String requestUrl = String.format(urlStr, owner, repo, path);

            HttpGet request = new HttpGet(requestUrl);
            request.setHeader("Accept", "application/vnd.github.v3+json");
            request.setHeader("User-Agent", "pom-analysis/1.0 (Java)");
            String userCredentials = username + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
            request.setHeader("Authorization", basicAuth);

            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                String content = (new JSONObject(responseBody)).getString("content");
                content = content.replaceAll("[\n\r]", "");
                byte[] decodedContent = Base64.getDecoder().decode(content);
                String dirpath = output + "/" + owner + "_" + repo + "/" + new File(path).getParent();
                new File(dirpath).mkdirs();
                FileOutputStream fos = new FileOutputStream(output + "/" + owner + "_" + repo + "/" + path);
                fos.write(decodedContent);
                fos.close();
                System.out.println("File downloaded!");
            } else {
                System.out.println("Request failed: " + response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
           e.printStackTrace();
        }

    }
}

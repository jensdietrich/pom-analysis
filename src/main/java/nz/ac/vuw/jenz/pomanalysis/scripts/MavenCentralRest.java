package nz.ac.vuw.jenz.pomanalysis.scripts;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class MavenCentralRest {


    public static boolean search(String artifactId, String groupId) throws Exception {

            String uri = "https://search.maven.org/solrsearch/select?q=g:"+groupId+"+AND+a:"+artifactId+"&rows=20&wt=json";
            HttpGet httpGet = new HttpGet(uri);
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build(); CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject json = new JSONObject(responseBody);
                JSONObject response1 = json.getJSONObject("response");
                return response1.getInt("numFound") > 0;
            }
    }

    public static List<Map<String, String>> searchByPackage(String name) throws Exception {
        List<Map<String, String>> results = new ArrayList<>();
        String uri = "https://search.maven.org/solrsearch/select?q=fc:"+name+"&rows=20&wt=json";
        HttpGet httpGet = new HttpGet(uri);
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build(); CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject json = new JSONObject(responseBody);
            JSONArray response1 = json.getJSONObject("response").getJSONArray("docs");
            for(int i = 0; i < response1.length(); i++) {
                JSONObject obj = (JSONObject) response1.get(i);
                Map m = new HashMap<>();
                m.put("g", obj.getString("g"));
                m.put("a", obj.getString("a"));
                m.put("v", obj.getString("v"));
                results.add(m);
            }
        }
        return results;

    }

}

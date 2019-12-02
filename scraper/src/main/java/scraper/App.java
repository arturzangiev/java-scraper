package scraper;

import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {

    private static JSONArray scoresArray = new JSONArray();

    static Document getHtml(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc;
    }

    static JSONObject createJsonObject(String teamOne, String teamTwo, String score) {
        JSONObject obj = new JSONObject();
        obj.put("TeamOne", teamOne);
        obj.put("TeamTwo", teamTwo);
        obj.put("Score", score);
        return obj;
    }

    static void jsonToFile() throws IOException {
        FileWriter file = new FileWriter("output.json");
        file.write(scoresArray.toJSONString());
        file.close();
    }

    public static void main(String[] args) throws IOException {
        Document doc = getHtml("https://www.hltv.org/results");

        Elements results = doc.select("div .result table tbody tr");
        for (Element result : results) {
            String teamOne = result.select("td.team-cell div.team1 div.team").text();
            String score = result.select("td.result-score").text();
            String teamTwo = result.select("td.team-cell div.team2 div.team").text();
            JSONObject scoreObject = createJsonObject(teamOne, teamTwo, score);
            scoresArray.add(scoreObject);
        }

        System.out.println(scoresArray);
        jsonToFile();

    }
}

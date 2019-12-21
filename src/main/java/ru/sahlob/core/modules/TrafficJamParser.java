package ru.sahlob.core.modules;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import java.io.IOException;


public class TrafficJamParser {

    private WebClient client;

    public TrafficJamParser() {
        client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setJavaScriptEnabled(true);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
    }

    public String getBusTime(String route) {

        var time = "Время пробок почему-то не получилось узнать=(";

        try {
            var v = client.getPage(route);
            var s = v.getWebResponse().getContentAsString();
            s = s.substring(14, 65);
            var route1 = route.substring(0, 88);
            var route2 = route.substring(88);
            var newRoute = route1 + s + route2;
            var w = client.getPage(newRoute);
            var sds = w.getWebResponse().getContentAsString();
            sds = sds.replace("\"", "");
            sds = sds.replace("{", "");
            sds = sds.replace("}", "");
            var i = sds.lastIndexOf("text:");
            sds = sds.substring(i);
            i = sds.indexOf(",");
            sds = sds.substring(0, i);
            i = sds.indexOf(" ");
            sds = sds.substring(i);
            time = sds;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return time;
    }

    public String getAutoTime(String route) {

        var time = "Время пробок почему-то не получилось узнать=(";

        try {
            var v = client.getPage(route);
            var s = v.getWebResponse().getContentAsString();
            s = s.substring(14, 65);
            var route1 = route.substring(0, 100);
            var route2 = route.substring(100);
            var newRoute = route1 + s + route2;
            var w = client.getPage(newRoute);
            var sds = w.getWebResponse().getContentAsString();
            var ssdfaqer = ",\"bounds\"";
            var i = sds.indexOf(ssdfaqer);
            sds = sds.substring(i + ssdfaqer.length());
            i = sds.indexOf(",\"bounds\"");
            sds = sds.substring(0, i);
            var newssd = "\"text\":\"";
            sds = sds.substring(sds.lastIndexOf(newssd) + newssd.length());
            sds = sds.substring(0, sds.lastIndexOf("\""));
            time = sds;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return time;
    }
}

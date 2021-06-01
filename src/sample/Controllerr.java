package sample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Controllerr {

    public static void main(String[] args) {
        final String url =
                "https://www.imdb.com/search/title/?groups=top_100&sort=user_rating,desc";

        try {
            final Document document = Jsoup.connect(url).userAgent("Mozilla/17.0").get();
            Elements temp = document.select("div.lister-item-content");
            int i = 0;
            for(Element movieList : temp){
                i++;
                System.out.println(i + " " + movieList.getElementsByTag("a").first().text() );
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

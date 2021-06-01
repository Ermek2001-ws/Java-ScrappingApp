package sample;

// Controller.java

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class Controller implements Initializable {

    @FXML
    private TextField urlTextField;

    @FXML
    private Button fetchBtn;

    @FXML
    private TextArea docTextArea;

    @FXML
    private TextField queryTextField;

    @FXML
    private Accordion elementsAccordion;

    private Document doc;
    private PauseTransition debouncer;

    @FXML
    void handleFetchPage(ActionEvent event) {
        try {

            doc = Jsoup.connect(urlTextField.getText()).get();
            docTextArea.setText(doc.body().html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fetchBtn.disableProperty().bind(
                urlTextField.textProperty().isEmpty()
        );

        debouncer = new PauseTransition(Duration.seconds(1));


        queryTextField.textProperty().addListener((obs, ov, nv) -> {

            debouncer.setOnFinished(evt -> {

                elementsAccordion.getPanes().clear();

                String querySelector = queryTextField.getText();
                if (querySelector != null && !querySelector.isEmpty()) {
                    final String url =
                            "https://www.imdb.com/search/title/?groups=top_100&sort=user_rating,desc";

                    //Elements elements = doc.select(querySelector);
                    final Document document;
                    try {
                        document = Jsoup.connect(url).userAgent("Mozilla/17.0").get();
                        Elements temp = document.select("div.lister-item-content");
                        temp.forEach(el -> {

                            TextArea textArea = new TextArea(el.getElementsByTag("p").first().text());//el.html()
                            textArea.setWrapText(true);
                            textArea.setEditable(false);
                            textArea.setPrefSize(600, 200);
                            elementsAccordion.getPanes().add(
                                    new TitledPane(el.tagName(), new StackPane(textArea))
                            );
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            debouncer.playFromStart();
        });
    }
}

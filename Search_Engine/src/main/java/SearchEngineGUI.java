import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.List;

public class SearchEngineGUI extends Application {

    private ListView<String> resultList;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Arama Motoru");

        // Arama Paneli
        TextField searchField = new TextField();
        searchField.setPromptText("Arama yapın...");
        Button searchButton = new Button("Ara");
        searchField.setStyle("-fx-font-size: 14px; -fx-background-color: #ffffff; -fx-border-color: #4CAF50; -fx-border-radius: 5;");

        
        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(searchField, searchButton);
        
               
        // Sonuç Listesi
        resultList = new ListView<>();
        resultList.setPrefSize(300, 400);

        // Arama butonuna tıklanıldığında
        searchButton.setOnAction(e -> performSearch(searchField.getText()));
        searchButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 5;");


        // Sonuç listesinde bir öğe seçildiğinde
        resultList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.startsWith("URL: ")) {
                String url = newValue.replace("URL: ", "");
                openInNewWindow(url); // Bağlantıyı yeni bir pencerede aç
            }
        });

        // Ana düzen
        VBox layout = new VBox(10, searchBox, resultList);
        layout.setStyle("-fx-background-color: #f0f0f0;");
        Scene scene = new Scene(layout, 300, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void performSearch(String query) {
        try {
            Searcher searcher = new Searcher("index_directory");
            List<String> searchResults = searcher.searchAndReturn(query);

            resultList.getItems().clear();
            if (searchResults.isEmpty()) {
                resultList.getItems().add("Sonuç bulunamadı.");
            } else {
                for (String result : searchResults) {
                    resultList.getItems().add(result);
                }
            }

        } catch (Exception ex) {
            resultList.getItems().clear();
            resultList.getItems().add("Arama sırasında bir hata oluştu: " + ex.getMessage());
        }
    }

    private void openInNewWindow(String url) {
        // Yeni pencere oluştur
        Stage newStage = new Stage();
        newStage.setTitle("Bağlantı: " + url);

        // WebView oluştur ve URL yükle
        WebView webView = new WebView();
        webView.getEngine().load(url);

        // WebHistory ve düğmeler oluştur
        Button backButton = new Button("Geri");
        Button forwardButton = new Button("İleri");

        backButton.setOnAction(e -> {
            if (webView.getEngine().getHistory().getCurrentIndex() > 0) {
                webView.getEngine().getHistory().go(-1); // Bir önceki sayfaya git
            }
        });

        forwardButton.setOnAction(e -> {
            if (webView.getEngine().getHistory().getCurrentIndex() < webView.getEngine().getHistory().getEntries().size() - 1) {
                webView.getEngine().getHistory().go(1); // Bir sonraki sayfaya git
            }
        });

        // Navigasyon çubuğu
        HBox navigationBar = new HBox(10, backButton, forwardButton);
        navigationBar.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");
        VBox.setVgrow(webView, Priority.ALWAYS);
        
        // WebView ve düğmeleri düzenleyen düzen
        VBox layout = new VBox(navigationBar, webView);
        layout.setStyle("-fx-background-color: #ffffff;");
        Scene scene = new Scene(layout, 800, 600);

        // Sahne ayarları
        newStage.setScene(scene);
        //newStage.setResizable(false); // Pencere boyutunu kilitle
        newStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

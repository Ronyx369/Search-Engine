import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class TestClass extends Application {
    @Override
    public void start(Stage primaryStage) {
        // WebView ve WebEngine oluştur
        WebView webView = new WebView();
        webView.getEngine().load("Başlangıç urlsi");

        // Layout oluştur
        BorderPane root = new BorderPane();
        root.setCenter(webView);

        // Sahne ve pencere ayarları
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("JavaFX WebView Örneği");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

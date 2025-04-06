import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;   
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class Indexer {

    private final Directory indexDirectory; // İndeksin saklanacağı yer
    private final StandardAnalyzer analyzer; // Veri analizi için

    public Indexer(String indexPath) throws IOException {
        this.indexDirectory = FSDirectory.open(Paths.get(indexPath)); // Disk tabanlı dizin
        this.analyzer = new StandardAnalyzer(); // Standart analizör kullanılıyor
    }

    public void indexFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String url;
            while ((url = reader.readLine()) != null) {
                indexUrl(url);
            }
        } catch (IOException e) {
            System.err.println("Dosya okuma hatasi: " + e.getMessage());
        }
    }

    private void indexUrl(String url) {
        try {
            // URL'yi ziyaret et ve içeriğini al
            org.jsoup.nodes.Document doc = org.jsoup.Jsoup.connect(url).get();
            String content = doc.text(); // Sayfa içeriği

            // İndeksleme işlemi
            addDocument(url, content);

            System.out.println("Indekslenen URL: " + url);
        } catch (IOException e) {
            System.err.println("URL islenemedi: " + url + " Hata: " + e.getMessage());
        }
    }

    public void addDocument(String url, String content) throws IOException {
        // IndexWriter ayarlarını yapılandır
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(indexDirectory, config)) {
            // Yeni bir doküman oluştur
            Document doc = new Document();
            doc.add(new StringField("url", url, Field.Store.YES)); // URL alanı
            doc.add(new TextField("content", content, Field.Store.YES)); // İçerik alanı
            writer.addDocument(doc); // Dokümanı ekle
        }
    }
}

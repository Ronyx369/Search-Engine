import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class WebCrawler {
    
    private static final Set<String> visitedUrls = new HashSet<>();

    public static void main(String[] args) {
        String startUrl = "https://ornek.com.tr/"; // Başlangıç URL'si
        System.out.println("Web Crawler Baslatiliyor...");
        crawl(startUrl);

        
    }
    
    public static void crawl(String url) {
        
        if (visitedUrls.contains(url)) {
            return; // URL zaten ziyaret edilmişse atla
        }
        
        visitedUrls.add(url);
        
        // URL'yi bir dosyaya kaydet
        saveUrlToFile(url);

        try {
            // URL'yi ziyaret et ve HTML içeriğini al
            Document doc = Jsoup.connect(url).get();

            // Sayfa başlığını yazdır
            System.out.println("Ziyaret edilen: " + url);
            System.out.println("Baslik: " + doc.title());
            
            

            // Sayfadaki tüm bağlantıları bul
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                
                String nextUrl = link.absUrl("href"); // Tam URL'yi al
                
                if ((nextUrl.endsWith(".aspx") || nextUrl.endsWith(".com.tr")) && !visitedUrls.contains(nextUrl)) {
                    
                    crawl(nextUrl); // Bağlantıyı taramaya devam et
                }
            }
        } catch (IOException e) {
            System.err.println("Hata: " + e.getMessage());
        }
    }

    
    public static void saveUrlToFile(String url) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("crawled_urls.txt", true))) {
            writer.write(url);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Dosyaya yazma hatası: " + e.getMessage());
        }
    }
    
}

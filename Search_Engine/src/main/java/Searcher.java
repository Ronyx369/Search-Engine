import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.nio.file.Paths;

public class Searcher {

    private final FSDirectory indexDirectory;

    public Searcher(String indexPath) throws IOException {
        this.indexDirectory = FSDirectory.open(Paths.get(indexPath));
    }

    public List<String> searchAndReturn(String searchTerm) throws Exception {
        List<String> results = new ArrayList<>();

        try (DirectoryReader reader = DirectoryReader.open(indexDirectory)) {
            IndexSearcher searcher = new IndexSearcher(reader);

            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            Query query = parser.parse(searchTerm);

            TopDocs topDocs = searcher.search(query, 10); // Maksimum 10 sonuç
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                results.add("URL: " + doc.get("url"));
            }
        }

        return results;
    }
}

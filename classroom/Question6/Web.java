
package Question6;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;

class WebCrawler {
    private final ExecutorService executor;
    private final Queue<String> urlQueue;
    private final Set<String> visitedUrls;
    private final ConcurrentHashMap<String, String> crawledData;

    public WebCrawler(int numThreads, Queue<String> initialUrls) {
        this.executor = Executors.newFixedThreadPool(numThreads);
        this.urlQueue = initialUrls;
        this.visitedUrls = new HashSet<>();
        this.crawledData = new ConcurrentHashMap<>();
    }

    public void startCrawling() {
        while (!urlQueue.isEmpty()) {
            String url = urlQueue.poll();
            if (url != null && !visitedUrls.contains(url)) {
                visitedUrls.add(url);
                executor.submit(() -> crawl(url));
            }
        }
        executor.shutdown();
    }

    private void crawl(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

            crawledData.put(url, content.toString());
            System.out.println("Crawled: " + url);
        } catch (Exception e) {
            System.err.println("Failed to crawl: " + url);
        }
    }

    public ConcurrentHashMap<String, String> getCrawledData() {
        return crawledData;
    }
}

public class Web {
    public static void main(String[] args) {
        Queue<String> initialUrls = new ConcurrentLinkedQueue<>();
        initialUrls.add("https://www.example.com");
        initialUrls.add("https://www.google.com");
        initialUrls.add("https://www.wikipedia.org");

        WebCrawler crawler = new WebCrawler(3, initialUrls);
        crawler.startCrawling();
    }
}

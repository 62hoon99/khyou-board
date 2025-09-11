package khyou.board.hotarticle.client;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleClient {

    private RestClient restClient;
    @Value("endpoints.khyou-board-article-service.url")
    private String articleServiceUrl;

    @PostConstruct
    void initRestClient() {
        this.restClient = RestClient.create(articleServiceUrl);
    }

    public ArticleResponse read(Long articleId) {
        try {
            return restClient
                    .get()
                    .uri("/v1/articles/{articleId}", articleId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(ArticleResponse.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Getter
    public static class ArticleResponse {
        private Long articleId;
        private String title;
        private LocalDateTime createdAt;
    }
}

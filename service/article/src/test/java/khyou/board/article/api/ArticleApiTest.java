package khyou.board.article.api;

import khyou.board.article.service.response.ArticlePageResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Map;

public class ArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createArticle() {
        //given
        ArticleCreateRequest request = ArticleCreateRequest.builder().boardId(1231124L).content("adsf").writerId(2132L).title("adfadf").build();
        //when
        ArticleResponse response = createArticle(request);
        //then
        System.out.println(response);
    }

    @Test
    void readAllArticles() {
        //when
        ArticlePageResponse articlePageResponse = readAllArticles(1L, 50000L, 30L);
        //then
        System.out.println(articlePageResponse.getArticles().size());
    }

    ArticlePageResponse readAllArticles(Long boardId, Long page, Long pageSize) {
         return restClient.get()
                 .uri("/v1/articles?boardId={boardId}&page={page}&pageSize={pageSize}",
                         Map.of("boardId", boardId, "page", page, "pageSize", pageSize))
                 .retrieve()
                 .body(ArticlePageResponse.class);
    }

    ArticleResponse createArticle(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Getter
    @Builder
    @ToString
    static class ArticleResponse {
        private Long articleId;
        private String title;
        private String content;
        private Long boardId;
        private Long writerId;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @ToString
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }
}

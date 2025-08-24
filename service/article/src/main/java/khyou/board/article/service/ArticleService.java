package khyou.board.article.service;

import khyou.board.article.entity.Article;
import khyou.board.article.repository.ArticleRepository;
import khyou.board.article.service.request.ArticleCreateRequest;
import khyou.board.article.service.request.ArticleUpdateRequest;
import khyou.board.article.service.response.ArticlePageResponse;
import khyou.board.article.service.response.ArticleResponse;
import khyou.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ArticleService {
    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;

    @Transactional
    public ArticleResponse create(ArticleCreateRequest request) {
        Article article = articleRepository.save(
                Article.create(snowflake.nextId(), request.getTitle(), request.getContent(), request.getBoardId(), request.getWriterId())
        );

        return ArticleResponse.from(article);
    }

    @Transactional
    public ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        article.update(request.getTitle(), request.getContent());
        return ArticleResponse.from(article);
    }

    public ArticleResponse read(Long articleId) {
        return ArticleResponse.from(articleRepository.findById(articleId).orElseThrow());
    }

    @Transactional
    public void delete(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        articleRepository.delete(article);
    }

    public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize) {
        return ArticlePageResponse.of(
                articleRepository.findAll(boardId, page, pageSize).stream().map(ArticleResponse::from).toList(),
                articleRepository.count(boardId, PageLimitCalculator.calculatePageLimit(page, pageSize, 10L))
        );
    }

    public List<ArticleResponse> readAllInfiniteScroll(Long board, Long limit, Long lastArticleId) {
        List<Article> articles = lastArticleId == null ?
        articleRepository.findArticleInfiniteScroll(board, limit) :
        articleRepository.findArticleInfiniteScroll(board, limit, lastArticleId);

        return articles.stream().map(ArticleResponse::from).toList();
    }
}

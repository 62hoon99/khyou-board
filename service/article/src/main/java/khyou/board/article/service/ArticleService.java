package khyou.board.article.service;

import khyou.board.article.entity.Article;
import khyou.board.article.entity.BoardArticleCount;
import khyou.board.article.repository.ArticleRepository;
import khyou.board.article.repository.BoardArticleCountRepository;
import khyou.board.article.service.request.ArticleCreateRequest;
import khyou.board.article.service.request.ArticleUpdateRequest;
import khyou.board.article.service.response.ArticlePageResponse;
import khyou.board.article.service.response.ArticleResponse;
import khyou.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ArticleService {
    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;

    @Transactional
    public ArticleResponse create(ArticleCreateRequest request) {
        Article article = articleRepository.save(
                Article.create(snowflake.nextId(), request.getTitle(), request.getContent(), request.getBoardId(), request.getWriterId())
        );

        int count = boardArticleCountRepository.increase(article.getBoardId());
        if (count == 0) {
            boardArticleCountRepository.save(new BoardArticleCount(article.getBoardId(), 1L));
        }

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
        boardArticleCountRepository.decrease(article.getBoardId());
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

    public Long count(Long boardId) {
        return boardArticleCountRepository.findById(boardId)
                .map(BoardArticleCount::getArticleCount)
                .orElse(0L);
    }
}

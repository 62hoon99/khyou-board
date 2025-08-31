package khyou.board.like.service.request;

import khyou.board.like.entity.ArticleLike;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleLikeResponse {
    private final Long articleLikeId;
    private final Long articleId; // shard key
    private final Long userId;
    private final LocalDateTime createdAt;

    public ArticleLikeResponse(Long articleLikeId, Long articleId, Long userId, LocalDateTime createdAt) {
        this.articleLikeId = articleLikeId;
        this.articleId = articleId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public ArticleLikeResponse(ArticleLike articleLike) {
        this.articleLikeId = articleLike.getArticleLikeId();
        this.articleId = articleLike.getArticleId();
        this.userId = articleLike.getUserId();
        this.createdAt = articleLike.getCreatedAt();
    }
}
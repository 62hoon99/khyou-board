package khyou.board.like.service;

import khyou.board.common.snowflake.Snowflake;
import khyou.board.like.entity.ArticleLike;
import khyou.board.like.repository.ArticleLikeRepository;
import khyou.board.like.service.request.ArticleLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {
    private final khyou.board.common.snowflake.Snowflake snowflake = new Snowflake();
    private final ArticleLikeRepository articleLikeRepository;

    public ArticleLikeResponse read(Long articleId, Long userId) {
        return  new ArticleLikeResponse(getByArticleIdAndUserId(articleId, userId));
    }

    public ArticleLikeResponse like(Long articleId, Long userId) {
        return new ArticleLikeResponse(
                articleLikeRepository.save(
                        ArticleLike.create(snowflake.nextId(), articleId, userId)
                )
        );
    }

    public void unlike(Long articleId, Long userId) {
        articleLikeRepository.delete(getByArticleIdAndUserId(articleId, userId));
    }

    private ArticleLike getByArticleIdAndUserId(Long articleId, Long userId) {
        return articleLikeRepository.findByArticleIdAndUserId(articleId, userId).orElseThrow();
    }
}

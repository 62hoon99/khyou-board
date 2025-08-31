package khyou.board.like.service;

import khyou.board.common.snowflake.Snowflake;
import khyou.board.like.entity.ArticleLike;
import khyou.board.like.entity.ArticleLikeCount;
import khyou.board.like.repository.ArticleLikeCountRepository;
import khyou.board.like.repository.ArticleLikeRepository;
import khyou.board.like.service.request.ArticleLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {
    private final khyou.board.common.snowflake.Snowflake snowflake = new Snowflake();
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    public ArticleLikeResponse read(Long articleId, Long userId) {
        return  new ArticleLikeResponse(getByArticleIdAndUserId(articleId, userId));
    }

    @Transactional
    public ArticleLikeResponse likePessimisticLock1(Long articleId, Long userId) {
        ArticleLike articleLike = articleLikeRepository.save(
                ArticleLike.create(snowflake.nextId(), articleId, userId)
        );

        int result = articleLikeCountRepository.increase(articleId);
        if (result == 0) {
            // 최초 요청 시에는 update 되는 레코드가 없으므로, 1로 초기화한다.
            // 트래픽이 순식간에 몰릴 수 있는 상황에는 유실될 수 있으므로, 게시글 생성 시점에 미리 0으로 초기화 해둘 수도 있다.
            articleLikeCountRepository.save(ArticleLikeCount.init(articleId, 1L));
        }

        return new ArticleLikeResponse(articleLike);
    }

    @Transactional
    public void unlikePessimisticLock1(Long articleId, Long userId) {
        articleLikeRepository.delete(getByArticleIdAndUserId(articleId, userId));

        articleLikeCountRepository.decrease(articleId);
    }

    @Transactional
    public ArticleLikeResponse likePessimisticLock2(Long articleId, Long userId) {
        ArticleLike articleLike = articleLikeRepository.save(
                ArticleLike.create(snowflake.nextId(), articleId, userId)
        );

        articleLikeCountRepository.findLockedByArticleId(articleId)
                .ifPresentOrElse(
                        ArticleLikeCount::increase,
                        () -> articleLikeCountRepository.save(ArticleLikeCount.init(articleId, 1L)));

        return new ArticleLikeResponse(articleLike);
    }

    @Transactional
    public void unlikePessimisticLock2(Long articleId, Long userId) {
        articleLikeRepository.delete(getByArticleIdAndUserId(articleId, userId));

        articleLikeCountRepository.findLockedByArticleId(articleId).orElseThrow().decrease();
    }

    @Transactional
    public ArticleLikeResponse likeOptimisticLock(Long articleId, Long userId) {
        ArticleLike articleLike = articleLikeRepository.save(
                ArticleLike.create(snowflake.nextId(), articleId, userId)
        );

        ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleId)
                .orElseGet(() -> ArticleLikeCount.init(articleId, 0L));
        articleLikeCount.increase();
        articleLikeCountRepository.save(articleLikeCount);

        return new ArticleLikeResponse(articleLike);
    }

    @Transactional
    public void unlikeOptimisticLock(Long articleId, Long userId) {
        articleLikeRepository.delete(getByArticleIdAndUserId(articleId, userId));

        articleLikeCountRepository.findById(articleId).orElseThrow().decrease();
    }

    private ArticleLike getByArticleIdAndUserId(Long articleId, Long userId) {
        return articleLikeRepository.findByArticleIdAndUserId(articleId, userId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Long count(Long articleId) {
        return articleLikeCountRepository.findById(articleId)
                .map(ArticleLikeCount::getLikeCount)
                .orElse(0L);
    }
}

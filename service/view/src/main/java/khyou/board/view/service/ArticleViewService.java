package khyou.board.view.service;

import khyou.board.view.repository.ArticleViewCountRepository;
import khyou.board.view.repository.ArticleViewDistributedLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

    private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewCountBackUpProcessor articleViewCountBackUpProcessor;
    private static final int BACK_UP_BATCH_SIZE = 100;
    private static final Duration ttl = Duration.ofMinutes(10L);

    public Long increase(Long articleId, Long userId) {
        if (!articleViewDistributedLockRepository.lock(articleId, userId, ttl)) {
            return articleViewCountRepository.count(articleId);
        }

        Long count = articleViewCountRepository.increase(articleId);
        if (count %  BACK_UP_BATCH_SIZE == 0) {
            articleViewCountBackUpProcessor.backup(articleId, count);
        }
        return count;
    }

    public Long count(Long articleId) {
        return articleViewCountRepository.count(articleId);
    }
}

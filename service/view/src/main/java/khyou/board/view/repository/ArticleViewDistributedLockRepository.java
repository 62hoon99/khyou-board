package khyou.board.view.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class ArticleViewDistributedLockRepository {

    private final StringRedisTemplate stringRedisTemplate;
    public static final String KEY_FORMAT = "view::article::%s::user::%s::lock";

    public boolean lock(Long articleId, Long userId, Duration ttl) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(generateKey(articleId, userId), "", ttl));
    }

    private String generateKey(Long articleId, Long userId) {
        return String.format(KEY_FORMAT, articleId, userId);
    }
}

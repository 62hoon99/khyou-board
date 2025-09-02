package khyou.board.view.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleViewCountRepository {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String KEY_FORMAT = "view::article::%s::view_count";

    public Long count(Long articleId) {
        String result = stringRedisTemplate.opsForValue().get(generated(articleId));
        return result == null ? 0L : Long.parseLong(result);
    }

    public Long increase(Long articleId) {
        return stringRedisTemplate.opsForValue().increment(generated(articleId));
    }

    private String generated(Long articleId) {
        return KEY_FORMAT.formatted(articleId);
    }
}

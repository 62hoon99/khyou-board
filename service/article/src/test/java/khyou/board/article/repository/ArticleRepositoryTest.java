package khyou.board.article.repository;

import khyou.board.article.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class ArticleRepositoryTest {

    @Autowired
    ArticleRepository articleRepository;

    @Test
    public void findAll() {
        List<Article> articles = articleRepository.findAll(1L, 1499970L, 30L);
        log.info("article.size: {}", articles.size());
    }

    @Test
    public void count() {
        long count = articleRepository.count(1L, 50000L);
        log.info("count: {}", count);
    }
}

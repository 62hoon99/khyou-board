package khyou.board.view.repsitory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import khyou.board.view.entity.ArticleViewCount;
import khyou.board.view.repository.ArticleViewCountBackupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ArticleViewCountBackUpRepositoryTest {
    @Autowired
    ArticleViewCountBackupRepository articleViewCountBackUpRepository;
    @PersistenceContext
    EntityManager entityManager;

    @Test
    @Transactional
    void updateViewCountTest() {
        // given
        articleViewCountBackUpRepository.save(
                ArticleViewCount.init(2L, 0L)
        );
        entityManager.flush();
        entityManager.clear();

        // when
        int result1 = articleViewCountBackUpRepository.updateViewCount(100L, 2L);
        int result2 = articleViewCountBackUpRepository.updateViewCount(300L, 2L);
        int result3 = articleViewCountBackUpRepository.updateViewCount(200L, 2L);

        // then
        assertThat(result1).isEqualTo(1);
        assertThat(result2).isEqualTo(1);
        assertThat(result3).isEqualTo(0);

        ArticleViewCount articleViewCount = articleViewCountBackUpRepository.findById(2L).get();
        assertThat(articleViewCount.getViewCount()).isEqualTo(300L);
    }
}

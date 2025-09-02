package khyou.board.view.service;

import khyou.board.view.entity.ArticleViewCount;
import khyou.board.view.repository.ArticleViewCountBackupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ArticleViewCountBackUpProcessor {
    private final ArticleViewCountBackupRepository articleViewCountBackupRepository;

    @Transactional
    public void backup(Long articleId, Long viewCount) {
        int result = articleViewCountBackupRepository.updateViewCount(viewCount, articleId);

        if (result > 0) {
            articleViewCountBackupRepository.findById(articleId)
                    .ifPresentOrElse(ignored -> {},
                            () -> articleViewCountBackupRepository.save(ArticleViewCount.init(articleId, viewCount)));
        }
    }
}

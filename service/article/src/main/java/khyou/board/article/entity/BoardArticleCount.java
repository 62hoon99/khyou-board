package khyou.board.article.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "board_article_count")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardArticleCount {
    @Id
    private Long boardId;
    private Long articleCount;

    public BoardArticleCount(Long boardId, Long articleCount) {
        this.boardId = boardId;
        this.articleCount = articleCount;
    }
}

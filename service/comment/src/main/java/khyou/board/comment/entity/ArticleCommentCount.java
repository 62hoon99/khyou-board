package khyou.board.comment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "article_comment_count")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleCommentCount {
    @Id
    private Long articleId;

    private Long commentCount;

    public ArticleCommentCount(Long articleId, Long commentCount) {
        this.articleId = articleId;
        this.commentCount = commentCount;
    }
}

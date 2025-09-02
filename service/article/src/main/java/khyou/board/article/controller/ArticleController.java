package khyou.board.article.controller;

import khyou.board.article.service.ArticleService;
import khyou.board.article.service.request.ArticleCreateRequest;
import khyou.board.article.service.request.ArticleUpdateRequest;
import khyou.board.article.service.response.ArticlePageResponse;
import khyou.board.article.service.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/v1/articles/{articleId}")
    public ArticleResponse read(@PathVariable Long articleId) {
        return articleService.read(articleId);
    }

    @GetMapping("/v1/articles")
    public ArticlePageResponse readAll(@RequestParam("boardId") Long boardId,
                                       @RequestParam("page") Long page,
                                       @RequestParam("pageSize") Long pageSize) {
        return articleService.readAll(boardId, page, pageSize);
    }

    @GetMapping("/v1/articles/infinite-scroll")
    public List<ArticleResponse> readAllInfiniteScroll(@RequestParam("boardId") Long boardId,
                                                       @RequestParam("limit") Long limit,
                                                       @RequestParam(value = "lastArticleId", required = false) Long lastArticleId) {
        return articleService.readAllInfiniteScroll(boardId, limit, lastArticleId);
    }

    @PostMapping("/v1/articles")
    public ArticleResponse create(@RequestBody ArticleCreateRequest request) {
        return articleService.create(request);
    }

    @PutMapping("/v1/articles/{articleId}")
    public ArticleResponse update(@PathVariable Long articleId, @RequestBody ArticleUpdateRequest request) {
        return articleService.update(articleId, request);
    }

    @DeleteMapping("/v1/articles/{articleId}")
    public void delete(@PathVariable Long articleId) {
        articleService.delete(articleId);
    }

    @GetMapping("/v1/articles/boards/{boardId}/count")
    public Long count(@PathVariable Long boardId) {
        return articleService.count(boardId);
    }
}

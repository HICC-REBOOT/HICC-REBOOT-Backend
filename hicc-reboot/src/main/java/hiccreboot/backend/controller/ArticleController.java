package hiccreboot.backend.controller;

import hiccreboot.backend.common.dto.Article.ArticleListResponse;
import hiccreboot.backend.common.dto.Article.ArticleRequestDTO;
import hiccreboot.backend.common.dto.Article.ArticleResponse;
import hiccreboot.backend.common.exception.ArticleNotFoundException;
import hiccreboot.backend.domain.Article;
import hiccreboot.backend.domain.Grade;
import hiccreboot.backend.domain.Member;
import hiccreboot.backend.service.ArticleService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bulletin-board")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final EntityManager em;

    @GetMapping("/list")
    public Object searchArticleList(@RequestParam(value = "page") int pageNumber,
                                    @RequestParam(value = "size") int pageSize,
                                    @RequestParam(value = "sort", required = false, defaultValue = "null") String sort,
                                    @RequestParam(value = "search", required = false, defaultValue = "null") String search) {
        //유저 이름,등급
        Grade userGrade = Grade.APPLICANT;
        String userName = "나야나";

        if (sort.equals("null")) {
            List<Article> articles = articleService.findArticles(pageNumber, pageSize).getContent();

            ArticleListResponse articleListResponse = new ArticleListResponse();
            articles.stream().forEach(article -> articleListResponse.addResult(ArticleResponse.createArticleResponse(article.getId(), userGrade, userName, article.getDate(), article.getBoardType(), article.getSubject())));

            return articleListResponse;
        }

        if (sort.equals("member")) {
            List<Article> articles = articleService.findArticlesByMemberName(pageNumber, pageSize, search).getContent();

            ArticleListResponse articleListResponse = new ArticleListResponse();
            articles.stream().forEach(article -> articleListResponse.addResult(ArticleResponse.createArticleResponse(article.getId(), userGrade, userName, article.getDate(), article.getBoardType(), article.getSubject())));

            return articleListResponse;
        }

        if (sort.equals("subject")) {
            List<Article> articles = articleService.findArticlesBySubject(pageNumber, pageSize, search).getContent();

            ArticleListResponse articleListResponse = new ArticleListResponse();
            articles.stream().forEach(article -> articleListResponse.addResult(ArticleResponse.createArticleResponse(article.getId(), userGrade, userName, article.getDate(), article.getBoardType(), article.getSubject())));

            return articleListResponse;
        }

        return new ArticleNotFoundException();
    }

    @GetMapping("/article")
    public Object searchArticle(@RequestParam("articleId") Long id) {
        Optional<Article> article = articleService.findArticle(id);

        // 이 부분 jwt로 유저이름이랑, 유저 등급 가져온다.
        Grade userGrade = Grade.APPLICANT;
        String userName = "나야나";


        if (article.isPresent()) {
            return makeArticleDTO(article.get(), userGrade, userName);
        }
        throw new ArticleNotFoundException();
    }

    private Object makeArticleDTO(Article article, Grade grade, String name) {
        if (article.getAppendices().size() != 0) {
            return ArticleResponse.createArticleResponse(article.getId(), grade, name, article.getDate(), true, article.getAppendices(), article.getBoardType(), article.getSubject(), article.getContent());
        }
        return ArticleResponse.createArticleResponse(article.getId(), grade, name, article.getDate(), false, article.getBoardType(), article.getSubject(), article.getContent());
    }

    @PostMapping("/article")
    public Object addArticle(Member member, ArticleRequestDTO articleRequestDTO) {
        //여기에 jwt로 Member 가져오는 로직 작성, 매개변수의 member 삭제!

        articleService.saveArticle(member, articleRequestDTO.getSubject(), articleRequestDTO.getContent(), articleRequestDTO.getBoard(), articleRequestDTO.getDate(), articleRequestDTO.getAppendices());

        //여기에 204 status에 맞게 return 작성
        HashMap<String, Object> returnValues = new HashMap<>();
        returnValues.put("a", "b");
        return returnValues;
    }

    @PatchMapping("/article")
    public Object updateArticle(Member member, @RequestParam("articleId") Long id, ArticleRequestDTO articleRequestDTO) {
        // Member 부분은 jwt에서 id 찾아서 가져오는 방식으로 수정

        articleService.deleteArticle(id);
        articleService.saveArticle(member, articleRequestDTO.getSubject(), articleRequestDTO.getContent(), articleRequestDTO.getBoard(), articleRequestDTO.getDate(), articleRequestDTO.getAppendices());

        //여기에 204 status에 맞게 return 작성
        HashMap<String, Object> returnValues = new HashMap<>();
        returnValues.put("a", "b");
        return returnValues;
    }

    @DeleteMapping("/article")
    public Object deleteArticle(@RequestParam("articleId") Long id) {
        articleService.deleteArticle(id);

        //여기에 204 status에 맞게 return 작성
        HashMap<String, Object> returnValues = new HashMap<>();
        returnValues.put("a", "b");
        return returnValues;
    }
}

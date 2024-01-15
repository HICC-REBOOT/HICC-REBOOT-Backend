package hiccreboot.backend.common.dto.Article;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ArticleListResponse {
    private List<ArticleResponse> contents = new ArrayList<>();

    public void addResult(ArticleResponse articleResponse) {
        contents.add(articleResponse);
    }
}

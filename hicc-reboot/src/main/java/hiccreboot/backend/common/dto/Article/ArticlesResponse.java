package hiccreboot.backend.common.dto.Article;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ArticlesResponse {
	private List<ArticleResponse> contents = new ArrayList<>();

	public void add(ArticleResponse articleResponse) {
		contents.add(articleResponse);
	}
}

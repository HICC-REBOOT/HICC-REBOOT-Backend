package hiccreboot.backend.domain;

public enum ArticleGrade {
	EXECUTIVE("EXECUTIVE"),
	NORMAL("NORMAL"),
	;
	private final String name;

	ArticleGrade(String name) {
		this.name = name;
	}
}

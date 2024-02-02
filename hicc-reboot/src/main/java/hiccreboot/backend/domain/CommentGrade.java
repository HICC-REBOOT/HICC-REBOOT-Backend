package hiccreboot.backend.domain;

public enum CommentGrade {
	EXECUTIVE("EXECUTIVE"),
	NORMAL("NORMAL"),
	;
	private final String name;

	CommentGrade(String name) {
		this.name = name;
	}
}

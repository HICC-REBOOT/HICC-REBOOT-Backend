package hiccreboot.backend.domain;

public enum CommentType {
    COMMENT("댓글"),
    REPLY_TO_COMMENT("대댓글"),
    ;

    private final String name;

    CommentType(String name) {
        this.name = name;
    }
}

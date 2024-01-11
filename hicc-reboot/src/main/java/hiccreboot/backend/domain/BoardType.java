package hiccreboot.backend.domain;

import lombok.Getter;

@Getter
public enum BoardType {
    FREE("자유"),
    ACTIVITY_PICTURE("활동사진"),
    EMPLOYMENT_INFORMATION("취업정보"),
    HICCS_PICK("힉스픽");

    private final String name;

    BoardType(String name) {
        this.name = name;
    }
}

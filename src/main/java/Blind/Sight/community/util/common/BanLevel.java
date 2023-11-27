package Blind.Sight.community.util.common;

import lombok.Getter;

@Getter
public enum BanLevel {
    REPORT(2),
    TROUBLE(3),
    DANGER(4);

    private final int code;

    BanLevel(int code) { this.code = code; }
}

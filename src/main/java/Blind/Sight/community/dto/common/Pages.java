package Blind.Sight.community.dto.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class Pages {
    private int size;
    private int number;
    private Sort sort;
}

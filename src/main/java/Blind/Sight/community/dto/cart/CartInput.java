package Blind.Sight.community.dto.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartInput {
    private String cartId;
    private String token;
    private Double versionNo;
    private Integer quantity;
    private Long cartDetailId;
}

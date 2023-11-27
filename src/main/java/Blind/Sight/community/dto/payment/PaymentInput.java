package Blind.Sight.community.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInput {
    private Integer option;
    private Double totalMoney;
    private String currency;
    private String stripeToken;
}

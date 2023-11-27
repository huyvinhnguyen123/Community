package Blind.Sight.community.domain.service.custom;

import Blind.Sight.community.domain.entity.Currency;
import Blind.Sight.community.domain.repository.postgresql.CurrencyRepositoryPg;
import Blind.Sight.community.dto.payment.PaymentInput;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentStripeService {
    private final CurrencyRepositoryPg currencyRepositoryPg;
    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = "sk_test_51OBq0PCTCBVVQmlYPZh0W1kpLtdOThl1mE5ZyenobZm1ZVNVk2JoZHaQxqVCF41Fi9POvcKhz1bE00YOZt1ivgBc0032pwlg2z";
    }

    public String createPaymentIntent(PaymentInput paymentInput) throws StripeException {
        long amountInCents = (long) (paymentInput.getTotalMoney() * 100);

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amountInCents)
                        .setCurrency(paymentInput.getCurrency())
                        .setPaymentMethod("pm_card_visa")
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Send the client secret back to the client
        return paymentIntent.getClientSecret();
    }

//    public Charge chargeCard(PaymentInput paymentInput) throws StripeException {
//        String testCardNumber = "4242424242424242";
//        int testExpMonth = 12;
//        int testExpYear = 2024;
//        String testCvc = "314";
//
//        long amountInCents = (long) (paymentInput.getTotalMoney() * 100);
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("amount", amountInCents);
//
//        for (Currency currency: currencyRepositoryPg.findAll()) {
//            if (currency.getCurrencyCode().equals(paymentInput.getCurrency())) {
//                params.put("currency", paymentInput.getCurrency());
//            }
//        }
//
//        Map<String, Object> cardParams = new HashMap<>();
//        cardParams.put("number", testCardNumber);
//        cardParams.put("exp_month", testExpMonth);
//        cardParams.put("exp_year", testExpYear);
//        cardParams.put("cvc", testCvc);
//
//        params.put("source", cardParams);
//        return Charge.create(params);
//    }
}

package Blind.Sight.community.domain.service.write;

import Blind.Sight.community.domain.entity.*;
import Blind.Sight.community.domain.entity.many.CartDetail;
import Blind.Sight.community.domain.repository.postgresql.CurrencyRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.OrderRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.PaymentRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.ProfileRepositoryPg;
import Blind.Sight.community.domain.service.custom.PaymentStripeService;
import Blind.Sight.community.domain.service.many.CartDetailService;
import Blind.Sight.community.domain.service.many.OrderDetailService;
import Blind.Sight.community.dto.payment.PaymentInput;
import Blind.Sight.community.util.common.StatusData;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServicePg {
    private final OrderRepositoryPg orderRepositoryPg;
    private final PaymentRepositoryPg paymentRepositoryPg;
    private final ProfileRepositoryPg profileRepositoryPg;
    private final CurrencyRepositoryPg currencyRepositoryPg;
    private final OrderDetailService orderDetailService;
    private final CartServicePg cartServicePg;
    private final CartDetailService cartDetailService;
    private final ProfileServicePg profileServicePg;
    private final PaymentStripeService paymentStripeService;

    public Iterable<Order> findByUserId(String userId) {
        Iterable<Order> orders = orderRepositoryPg.findByUserId(userId);
        log.info("find orders success");
        return orders;
    }

    public Order findByOrderId(String userId) {
        Order existOrder = orderRepositoryPg.findById(userId).orElseThrow(
                () -> new NullPointerException("Not found this order by user: " + userId)
        );
        log.info("Found order");
        return existOrder;
    }

//    public List<Product> findProductsByOrderId(String orderId) {
//        List<ProductDataForPayment> productDataForPayments = orderRepositoryPg.findProductsByOrderId(orderId);
//        return
//    }

    public void createOrder(Authentication authentication, String cartId) {
        if (authentication != null && authentication.isAuthenticated()) {
            Cart existCart = cartServicePg.findByCartId(cartId);
            List<CartDetail> existCartDetails = cartDetailService.findByCartId(existCart.getCartId());
            User userLogin = (User) authentication.getPrincipal();

            Order order = new Order(1, existCart.getTotalPrice(), existCart.getTotalPoint(), StatusData.CONFIRMED.getCode());
            order.setUser(userLogin);
            orderRepositoryPg.save(order);
            log.info("Save order success");

            for (CartDetail cartDetail: existCartDetails) {
                orderDetailService.createOrderDetail(cartDetail.getQuantity(), order, cartDetail.getProduct());
            }
        } else {
            log.error("Authentication failed");
        }
    }

    public void createPayment(Authentication authentication, String orderId, PaymentInput paymentInput) throws StripeException {
        if (authentication != null && authentication.isAuthenticated()) {
            User userLogin = (User) authentication.getPrincipal();

            Order existOrder = findByOrderId(orderId);

            Payment payment = new Payment();
            payment.setUser(userLogin);
            payment.setOrder(existOrder);

            if (paymentInput.getOption().equals(1)) {
                payment.setUsingCard(true);
                payment.setPaymentMethod("Card Payment");

                paymentRepositoryPg.save(payment);
                log.info("Save payment success success");

                paymentByCard(existOrder, payment, paymentInput);
            }

            if (paymentInput.getOption().equals(2)) {
                payment.setUsingPoint(true);
                payment.setPaymentMethod("Point Payment");

                paymentRepositoryPg.save(payment);
                log.info("Save payment success success");

                paymentByPoint(userLogin, existOrder, payment);
            }

            if (paymentInput.getOption().equals(3)) {
                payment.setUsingCash(true);
                payment.setAllowPayment(true);
                payment.setPaymentMethod("Cash Payment");

                paymentRepositoryPg.save(payment);
                log.info("Save payment success success");
            }
        }
    }

    private void paymentByCard(Order order, Payment payment, PaymentInput paymentInput) throws StripeException {
        String clientSecret = paymentStripeService.createPaymentIntent(paymentInput);
        String paymentIntentId = PaymentIntent.retrieve(clientSecret).getId();
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        Long amountPaid = paymentIntent.getAmountReceived();

        Currency convertCurrency = convertPriceCurrency(paymentInput.getCurrency());
        Double convertPrice;
        double remainingAmount;

        if (Boolean.FALSE.equals(convertCurrency.getIsDefaultCurrency())) {
            convertPrice = order.getTotalPrice() / convertCurrency.getExchangeRate();
            remainingAmount = amountPaid - convertPrice;

            log.info("Convert to {}", convertCurrency.getCurrencyName() );
        } else {
            remainingAmount = amountPaid - order.getTotalPrice();
        }

        payment.setAllowPayment(true);
        paymentRepositoryPg.save(payment);
        log.info("Payment success");
        log.info("Payment success. Remaining Amount: {}", remainingAmount);
    }

    private Currency convertPriceCurrency(String currencyCode) {
        List<Currency> currencies = currencyRepositoryPg.findAll();

        Currency convertCurrency = new Currency();
        for (Currency currency: currencies) {
            if (currency.getCurrencyCode().equals(currencyCode)) {
                convertCurrency = currency;
            }
        }

        return convertCurrency;
    }

    private void paymentByPoint(User user, Order order, Payment payment) {
        Profile existProfile = profileServicePg.findProfileByUserId(user.getUserId());

        if (existProfile.getTotalPoint() >= order.getTotalPoint()) {
            Double leftPoint = existProfile.getTotalPoint() - order.getTotalPoint();

            existProfile.setTotalPoint(leftPoint);
            payment.setAllowPayment(true);
            paymentRepositoryPg.save(payment);
            profileRepositoryPg.save(existProfile);
            log.info("Payment success");

            order.setIsPayment(true);
            orderRepositoryPg.save(order);
            log.info("Update order success");

        } else {
            log.error("You don't have enough point to pay");
        }
    }
}

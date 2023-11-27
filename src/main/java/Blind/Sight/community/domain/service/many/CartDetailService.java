package Blind.Sight.community.domain.service.many;

import Blind.Sight.community.domain.entity.Cart;
import Blind.Sight.community.domain.entity.Product;
import Blind.Sight.community.domain.entity.many.CartDetail;
import Blind.Sight.community.domain.repository.postgresql.CartDetailRepositoryPg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartDetailService {
    private final CartDetailRepositoryPg cartDetailRepositoryPg;

    public Iterable<CartDetail> findAll() {
        Iterable<CartDetail> cartDetails = cartDetailRepositoryPg.findAll();
        log.info("Find all cart detail success");
        return cartDetails;
    }

    public List<CartDetail> findByCartId(String cartId) {
        List<CartDetail> cartDetails = cartDetailRepositoryPg.findByCartId(cartId);
        log.info("Find list cart detail success");
        return cartDetails;
    }

    public CartDetail findByCartDetailId(Long cartDetailId) {
        CartDetail existCartDetail = cartDetailRepositoryPg.findById(cartDetailId).orElseThrow(
                () -> new NullPointerException("Not found this cart detail: " + cartDetailId)
        );

        log.info("Found cart detail");
        return existCartDetail;
    }

    public CartDetail createCartDetail(Integer quantity, Double price, Double point, Double totalPrice, Double totalPoint, Integer statusCode,
                                  Cart cart, Product product) {
        CartDetail cartDetail = new CartDetail();
        cartDetail.setQuantity(quantity);
        cartDetail.setPrice(price);
        cartDetail.setPoint(point);
        cartDetail.setTotalPrice(totalPrice);
        cartDetail.setTotalPoint(totalPoint);
        cartDetail.setStatusCode(statusCode);
        cartDetail.setCart(cart);
        cartDetail.setProduct(product);
        cartDetailRepositoryPg.save(cartDetail);

        log.info("Save cart detail success");
        return cartDetail;
    }

    public void updateCartDetail(Long cartDetailId, Integer quantity, Double price, Double point, Double totalPrice, Double totalPoint,
                                 Integer statusCode, Cart cart, Product product) {
        CartDetail cartDetail = findByCartDetailId(cartDetailId);
        cartDetail.setQuantity(quantity);
        cartDetail.setPrice(price);
        cartDetail.setPoint(point);
        cartDetail.setTotalPrice(totalPrice);
        cartDetail.setTotalPoint(totalPoint);
        cartDetail.setStatusCode(statusCode);
        cartDetail.setCart(cart);
        cartDetail.setProduct(product);
        cartDetailRepositoryPg.save(cartDetail);

        log.info("Update cart detail success");
    }

    public void deleteCartDetail(CartDetail cartDetail) {
        cartDetailRepositoryPg.delete(cartDetail);
        log.info("Delete cart detail success");
    }
}

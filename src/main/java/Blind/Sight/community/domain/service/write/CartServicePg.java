package Blind.Sight.community.domain.service.write;

import Blind.Sight.community.domain.entity.Cart;
import Blind.Sight.community.domain.entity.Product;
import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.domain.entity.many.CartDetail;
import Blind.Sight.community.domain.repository.postgresql.CartDetailRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.CartRepositoryPg;
import Blind.Sight.community.domain.service.many.CartDetailService;
import Blind.Sight.community.dto.cart.CartInput;
import Blind.Sight.community.util.common.StatusData;
import Blind.Sight.community.util.random.RandomId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServicePg {
    private final CartRepositoryPg cartRepositoryPg;
    private final CartDetailRepositoryPg cartDetailRepositoryPg;
    private final ProductServicePg productServicePg;
     private final CartDetailService cartDetailService;
     private static final String NOT_FOUND_CART = "Not found cart: ";
     private static final String FOUND_CART = "Found cart";

    public Boolean checkByCartId(String cartId) {
        Optional<Cart> existCart = cartRepositoryPg.findById(cartId);
        return existCart.isPresent();
    }

    public Boolean checkByToken(String token) {
        Optional<Cart> existCart = cartRepositoryPg.findByToken(token);
        return existCart.isPresent();
    }

    public Boolean checkByUserId(String userId) {
        Optional<Cart> existCart = cartRepositoryPg.findByUserId(userId);
        return existCart.isPresent();
    }

    public Cart findByCartId(String cartId) {
        Cart existCart = cartRepositoryPg.findById(cartId).orElseThrow(
                () -> new NullPointerException(NOT_FOUND_CART + cartId)
        );

        log.info(FOUND_CART);
        return existCart;
    }

    public Cart findByToken(String token) {
        Cart existCart = cartRepositoryPg.findByToken(token).orElseThrow(
                () -> new NullPointerException(NOT_FOUND_CART + token)
        );

        log.info(FOUND_CART);
        return existCart;
    }

    public Cart findByUserId(String userId) {
        Cart existCart = cartRepositoryPg.findByUserId(userId).orElseThrow(
                () -> new NullPointerException(NOT_FOUND_CART + userId)
        );

        log.info(FOUND_CART);
        return existCart;
    }

    public void addToCart(Authentication authentication, String productId, CartInput cartInput) {
        Product existProduct = productServicePg.findProductById(productId);
        double totalPrice = 0.0;
        double totalPoint = 0.0;

        if (authentication != null && authentication.isAuthenticated()) {
            User userLogin = (User) authentication.getPrincipal();

            // create & update cart with authentication
            if (Boolean.FALSE.equals(checkByCartId(cartInput.getCartId()))) {
                createCartLogin(cartInput, userLogin, existProduct, totalPrice, totalPoint);
            } else {
                updateCartLogin(cartInput, existProduct);
            }

        } else {
            // create & update cart without authentication
            if (Boolean.FALSE.equals(checkByToken(cartInput.getToken()))) {
                createCartNotLogin(cartInput, existProduct, totalPrice, totalPoint);
            } else {
                updateCartNotLogin(cartInput, existProduct);
            }
        }
    }

    private void createCartLogin(CartInput cartInput, User user, Product product, double totalPrice, double totalPoint) {
        Cart cart = new Cart(totalPrice, totalPoint, cartInput.getVersionNo());
        cart.setUser(user);
        cartRepositoryPg.save(cart);
        log.info("Save cart login success");

        totalPrice = product.getPrice() * cartInput.getQuantity();
        totalPoint = product.getPoint() * cartInput.getQuantity();

        CartDetail cartDetail = cartDetailService.createCartDetail(cartInput.getQuantity(),
                product.getPrice(), product.getPoint(), totalPrice, totalPoint,
                StatusData.SAVED.getCode(), cart, product);

        cart.setTotalPrice(cartDetail.getTotalPrice());
        cart.setTotalPoint(cartDetail.getTotalPoint());
        cartRepositoryPg.save(cart);
        log.info("Save update cart login success");

        log.info("Create cart login success");
    }

    private void createCartNotLogin(CartInput cartInput, Product product, double totalPrice, double totalPoint) {
        Cart cart = new Cart(RandomId.generateUUIDString(20));
        cart.setTotalPrice(totalPrice);
        cart.setTotalPoint(totalPoint);
        cart.setVersionNo(cartInput.getVersionNo());
        cartRepositoryPg.save(cart);
        log.info("Save cart not login success");

        totalPrice = product.getPrice() * cartInput.getQuantity();
        totalPoint = product.getPoint() * cartInput.getQuantity();

        CartDetail cartDetail = cartDetailService.createCartDetail(cartInput.getQuantity(),
                product.getPrice(), product.getPoint(), totalPrice, totalPoint,
                StatusData.SAVED.getCode(), cart, product);

        cart.setTotalPrice(cartDetail.getTotalPrice());
        cart.setTotalPoint(cartDetail.getTotalPoint());
        cartRepositoryPg.save(cart);
        log.info("Save update cart not login success");

        log.info("Create cart not login success");
    }

    private void updateCartLogin(CartInput cartInput, Product product) {
        Cart existCart = findByCartId(cartInput.getCartId());
        existCart.setVersionNo(cartInput.getVersionNo());

        double totalPrice = product.getPrice() * cartInput.getQuantity();
        double totalPoint = product.getPoint() * cartInput.getQuantity();

        CartDetail existCartDetail = cartDetailService.findByCartDetailId(cartInput.getCartDetailId());
        if (existCartDetail.getProduct().getProductId().equals(product.getProductId())) {
            existCartDetail.setQuantity(existCartDetail.getQuantity() + cartInput.getQuantity());
            existCartDetail.setTotalPrice(existCartDetail.getTotalPrice() + totalPrice);
            existCartDetail.setTotalPoint(existCartDetail.getTotalPoint() + totalPoint);

            cartDetailRepositoryPg.save(existCartDetail);
            log.info("update cart detail success");

            existCart.setTotalPrice(existCartDetail.getTotalPrice());
            cartRepositoryPg.save(existCart);
            log.info("update cart login success");
        } else {
            cartDetailService.updateCartDetail(existCartDetail.getCartDetailId(), cartInput.getQuantity(),
                    product.getPrice(), product.getPoint(), totalPrice, totalPoint,
                    StatusData.SAVED.getCode(), existCart, product);
        }
    }

    private void updateCartNotLogin(CartInput cartInput, Product product) {
        Cart existCart = findByToken(cartInput.getToken());
        existCart.setVersionNo(cartInput.getVersionNo());

        double totalPrice = product.getPrice() * cartInput.getQuantity();
        double totalPoint = product.getPoint() * cartInput.getQuantity();

        CartDetail existCartDetail = cartDetailService.findByCartDetailId(cartInput.getCartDetailId());
        if (existCartDetail.getProduct().getProductId().equals(product.getProductId())) {
            existCartDetail.setQuantity(existCartDetail.getQuantity() + cartInput.getQuantity());
            existCartDetail.setTotalPrice(existCartDetail.getTotalPrice() + totalPrice);
            existCartDetail.setTotalPoint(existCartDetail.getTotalPoint() + totalPoint);

            cartDetailRepositoryPg.save(existCartDetail);
            log.info("update cart detail success");

            existCart.setTotalPrice(existCartDetail.getTotalPrice());
            cartRepositoryPg.save(existCart);
            log.info("update cart not login success");
        } else {
            cartDetailService.updateCartDetail(existCartDetail.getCartDetailId(), cartInput.getQuantity(),
                    product.getPrice(), product.getPoint(), totalPrice, totalPoint,
                    StatusData.SAVED.getCode(), existCart, product);
        }
    }

    public void synchronizeCart(Authentication authentication, CartInput cartInput) {
        if (authentication != null && authentication.isAuthenticated()) {
            User userLogin = (User) authentication.getPrincipal();

            if (Boolean.TRUE.equals(checkByUserId(userLogin.getUserId()))) {
                Cart existCartLogin = findByUserId(userLogin.getUserId());

                if (Boolean.TRUE.equals(checkByToken(cartInput.getToken()))) {
                    Cart existCartNotLogin = findByToken(cartInput.getToken());

                    List<CartDetail> cartDetailLogins = cartDetailService.findByCartId(existCartLogin.getCartId());
                    List<CartDetail> cartDetailNotLogins = cartDetailService.findByCartId(existCartNotLogin.getCartId());

                    for (CartDetail cartDetailLogin : cartDetailLogins) {
                        for (CartDetail cartDetailNotLogin : cartDetailNotLogins) {
                            if (cartDetailNotLogin.getProduct().getProductId().equals(cartDetailLogin.getProduct().getProductId())) {
                                cartDetailLogin.setQuantity(cartDetailLogin.getQuantity() + cartDetailNotLogin.getQuantity());
                                cartDetailLogin.setTotalPrice(cartDetailLogin.getQuantity() * cartDetailNotLogin.getPrice());
                                cartDetailRepositoryPg.save(cartDetailLogin);
                                log.info("save cart detail login success");

                                existCartLogin.setTotalPrice(cartDetailLogin.getTotalPrice());
                                existCartLogin.setVersionNo(cartInput.getVersionNo());
                                cartRepositoryPg.save(existCartLogin);
                                log.info("Save update cart login success");

                                cartDetailService.deleteCartDetail(cartDetailNotLogin);
                                log.info("Delete cart detail not login success");

                                cartRepositoryPg.delete(existCartNotLogin);
                                log.info("Delete cart not login success");

                            } else {
                                CartDetail newCartDetailLogin = cartDetailService.createCartDetail(cartDetailNotLogin.getQuantity(),
                                        cartDetailNotLogin.getPrice(), cartDetailNotLogin.getPoint(), cartDetailNotLogin.getTotalPrice(), cartDetailNotLogin.getTotalPoint(),
                                        StatusData.SAVED.getCode(), existCartLogin, cartDetailNotLogin.getProduct());

                                Double totalPrice = cartDetailLogin.getTotalPrice() + newCartDetailLogin.getTotalPrice();
                                Double totalPoint = cartDetailLogin.getTotalPoint() + newCartDetailLogin.getTotalPoint();

                                existCartLogin.setTotalPrice(totalPrice);
                                existCartLogin.setTotalPoint(totalPoint);
                                existCartLogin.setVersionNo(cartInput.getVersionNo());
                                cartRepositoryPg.save(existCartLogin);
                                log.info("Save update cart login success");

                                cartDetailService.deleteCartDetail(cartDetailNotLogin);
                                log.info("Delete cart detail not login success");

                                cartRepositoryPg.delete(existCartNotLogin);
                                log.info("Delete cart not login success");
                            }
                        }
                    }
                }
            } else {
                log.info("You don't have cart, create cart with add one product first");
            }
        }
        log.info("synchronize cart success");
    }

    public void deleteCart(User user) {
        Cart existCart = findByUserId(user.getUserId());
        List<CartDetail> existCartDetails = cartDetailService.findByCartId(existCart.getCartId());

        for (CartDetail cartDetail: existCartDetails) {
            cartDetailService.deleteCartDetail(cartDetail);
        }

        cartRepositoryPg.delete(existCart);
        log.info("Delete cart success");
    }
}

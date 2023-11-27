package Blind.Sight.community.domain.service.many;

import Blind.Sight.community.domain.entity.Order;
import Blind.Sight.community.domain.entity.Product;
import Blind.Sight.community.domain.entity.many.OrderDetail;
import Blind.Sight.community.domain.repository.postgresql.OrderDetailRepositoryPg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepositoryPg orderDetailRepositoryPg;

    public Iterable<OrderDetail> findAll() {
        Iterable<OrderDetail> orderDetails = orderDetailRepositoryPg.findAll();
        log.info("Find all order detail success");
        return  orderDetails;
    }

    public OrderDetail findByOrderDetailId(Long orderDetailId) {
        OrderDetail existOrderDetail = orderDetailRepositoryPg.findById(orderDetailId).orElseThrow(
                () -> new NullPointerException("Not found this order detail: " + orderDetailId)
        );

        log.info("Found order detail");
        return existOrderDetail;
    }

    public void createOrderDetail(Integer quantity, Order order, Product product) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(product.getPrice());
        orderDetail.setTotalPrice(orderDetail.getPrice() * orderDetail.getQuantity());
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetailRepositoryPg.save(orderDetail);

        log.info("Save order detail success");
    }

    public void updateOrderDetail(Long orderDetailId, Integer quantity, Order order, Product product) {
        OrderDetail existOrderDetail = findByOrderDetailId(orderDetailId);
        existOrderDetail.setQuantity(quantity);
        existOrderDetail.setPrice(product.getPrice());
        existOrderDetail.setTotalPrice(existOrderDetail.getPrice() * existOrderDetail.getQuantity());
        existOrderDetail.setOrder(order);
        existOrderDetail.setProduct(product);
        orderDetailRepositoryPg.save(existOrderDetail);

        log.info("Update order detail success");
    }

    public void deleteOrderDetail(Long orderDetailId) {
        OrderDetail existOrderDetail = findByOrderDetailId(orderDetailId);
        orderDetailRepositoryPg.delete(existOrderDetail);

        log.info("Delete order detail success");
    }
}

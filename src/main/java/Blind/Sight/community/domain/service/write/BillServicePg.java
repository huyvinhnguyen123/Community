package Blind.Sight.community.domain.service.write;

import Blind.Sight.community.domain.entity.*;
import Blind.Sight.community.domain.repository.postgresql.BillRepositoryPg;
import Blind.Sight.community.domain.service.many.BillDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillServicePg {
    private final BillRepositoryPg billRepositoryPg;
    private final BillDetailService billDetailService;

    public void createBill(Order order, Profile profile, Product product, Currency currency, Double moneyInCard) {
        Double leftMoney = moneyInCard - order.getTotalPrice();
        Double leftPoint = profile.getTotalPoint() - order.getTotalPoint();

        Bill bill = new Bill(
            order.getTotalPrice(), moneyInCard, leftMoney,
            order.getTotalPoint(), profile.getTotalPoint(), leftPoint
        );
        bill.setOrder(order);
        bill.setCurrency(currency);

        billRepositoryPg.save(bill);
        log.info("Save bill success");

        billDetailService.createBillDetail(bill, product);

        log.info("Create bill success");
    }
}

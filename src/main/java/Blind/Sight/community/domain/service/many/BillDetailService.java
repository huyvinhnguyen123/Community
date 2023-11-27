package Blind.Sight.community.domain.service.many;

import Blind.Sight.community.domain.entity.Bill;
import Blind.Sight.community.domain.entity.Product;
import Blind.Sight.community.domain.entity.many.BillDetail;
import Blind.Sight.community.domain.repository.postgresql.BillDetailRepositoryPg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillDetailService {
    private final BillDetailRepositoryPg billDetailRepositoryPg;

    public BillDetail findBillDetailById(Long billDetailId) {
        BillDetail existBillDetail = billDetailRepositoryPg.findById(billDetailId).orElseThrow(
                () -> new NullPointerException("Not found this bill detail: " + billDetailId)
        );

        log.info("Found bill detail");
        return existBillDetail;
    }

    public void createBillDetail(Bill bill, Product product) {
        BillDetail billDetail = new BillDetail();
        billDetail.setDatePaymentProduct(LocalDate.now());
        billDetail.setBill(bill);
        billDetail.setProduct(product);
        billDetailRepositoryPg.save(billDetail);

        log.info("Save bill detail success");
    }

    public void deleteBillDetail(Long billDetailId) {
        BillDetail existBillDetail = findBillDetailById(billDetailId);
        billDetailRepositoryPg.delete(existBillDetail);

        log.info("Delete bill detail success");
    }

}

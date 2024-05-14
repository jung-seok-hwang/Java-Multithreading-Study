package multithreadingcore.multithreading.coupon.applicaiton;


import lombok.RequiredArgsConstructor;
import multithreadingcore.multithreading.coupon.entity.Coupon;
import multithreadingcore.multithreading.coupon.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository ticketRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {
        Coupon coupon = ticketRepository.findByIdWithOptimisticLock(id);
        coupon.decrease(quantity);
        ticketRepository.save(coupon);
    }


}

package multithreadingcore.multithreading.coupon.repository;

import jakarta.persistence.LockModeType;
import multithreadingcore.multithreading.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select c from Coupon c where c.id = :id")
    Coupon findByIdWithOptimisticLock(Long id);

}

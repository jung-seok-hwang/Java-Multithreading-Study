package multithreadingcore.multithreading.util;


import lombok.RequiredArgsConstructor;
import multithreadingcore.multithreading.coupon.applicaiton.CouponService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimisticLockConfig {

    private final CouponService coupons;

    public void decrease(Long id, Long quantity) throws InterruptedException {

        while (true) {
            try {
                coupons.decrease(id, quantity);
                break;
            } catch (Exception e) {
                Thread.sleep(5);  // 지연 시간을 50ms로 늘림
            }
        }
        throw new RuntimeException("최적화 잠금 충돌로 인해 요청 처리에 실패했습니다.");
    }

    public void decrease2(Long id, Long quantity) throws InterruptedException {
        int maxAttempts = 10;  // 최대 재시도 횟수를 10으로 설정
        int attempt = 0;
        while (attempt < maxAttempts) {
            try {
                coupons.decrease(id, quantity);
                return;  // 성공 시 함수 종료
            } catch (Exception e) {
                Thread.sleep(50);  // 지연 시간을 50ms로 설정
                attempt++;  // 재시도 횟수 증가
            }
        }
        throw new RuntimeException("최적화 잠금 충돌로 인해 요청 처리에 실패했습니다.");
    }
}

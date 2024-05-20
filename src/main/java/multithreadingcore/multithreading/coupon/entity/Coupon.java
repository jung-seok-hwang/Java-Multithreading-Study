package multithreadingcore.multithreading.coupon.entity;



import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("쿠폰 시리얼 넘버")
    private Long serial;

    @Comment("쿠폰 수량")
    private Long quantity;

    @Version
    private Long version;

    public Coupon(Long serial, Long quantity) {
        this.serial = serial;
        this.quantity = quantity;
    }

    public void decrease(Long quantity) {
        if (this.quantity - quantity < 0) {
            throw new RuntimeException("0 이하가 될수없습니다.");
        }
        this.quantity -= quantity;
    }
}

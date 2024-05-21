package multithreadingcore.multithreading.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import multithreadingcore.multithreading.ticket.entity.TicketBuyLimit;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @ColumnDefault("0")
    @Builder.Default
    private Long ticket = 0L;

    @OneToMany(mappedBy = "user")
    private List<UserTicket> tickets = new ArrayList<>();

    public void maxTicketCount(Long buy) {
        if (ticket >= TicketBuyLimit.MAXIMUM_TICKET_COUNT.getValue()){
            throw new IllegalArgumentException("2개 이상 구매 불가능합니다.");
        }

        ticket += buy;
        System.out.println(ticket);
    }

}

package multithreadingcore.multithreading.user.entity;


import jakarta.persistence.*;
import lombok.*;
import multithreadingcore.multithreading.ticket.entity.AssignMemberTickets;
import multithreadingcore.multithreading.ticket.entity.Ticket;
import multithreadingcore.multithreading.ticket.entity.TicketBuyLimit;

import java.util.HashSet;
import java.util.Set;



@Entity
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToMany(mappedBy = "user")
    private Set<AssignMemberTickets> tickets = new HashSet<>();


    public boolean maxTicketCount() {
        return tickets.size() >= TicketBuyLimit.MAXIMUM_TICKET_COUNT.getValue();
    }



}

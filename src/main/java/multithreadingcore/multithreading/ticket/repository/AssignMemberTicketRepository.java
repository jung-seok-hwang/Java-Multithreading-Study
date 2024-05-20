package multithreadingcore.multithreading.ticket.repository;

import multithreadingcore.multithreading.ticket.entity.AssignMemberTickets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignMemberTicketRepository extends JpaRepository<AssignMemberTickets, Long> {
}

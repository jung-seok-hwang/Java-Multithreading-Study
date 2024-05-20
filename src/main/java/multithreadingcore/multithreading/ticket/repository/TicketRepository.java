package multithreadingcore.multithreading.ticket.repository;

import multithreadingcore.multithreading.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}

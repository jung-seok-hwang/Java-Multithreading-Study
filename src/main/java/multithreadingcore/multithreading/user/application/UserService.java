package multithreadingcore.multithreading.user.application;


import lombok.RequiredArgsConstructor;
import multithreadingcore.multithreading.ticket.entity.AssignMemberTickets;
import multithreadingcore.multithreading.ticket.entity.Ticket;
import multithreadingcore.multithreading.ticket.repository.AssignMemberTicketRepository;
import multithreadingcore.multithreading.ticket.repository.TicketRepository;
import multithreadingcore.multithreading.user.entity.User;
import multithreadingcore.multithreading.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final AssignMemberTicketRepository assignMemberTicketRepository;

    public void buy(Long userId, Long ticketId, Long quantity) {

        User user = userRepository.findById(userId).orElseThrow();

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();

        AssignMemberTickets assignMemberTickets = new AssignMemberTickets(user, ticket);
        assignMemberTickets.decrease(quantity);

        assignMemberTicketRepository.save(assignMemberTickets);
    }
}

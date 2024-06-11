package multithreadingcore.multithreading.explicit;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import multithreadingcore.multithreading.ticket.entity.Ticket;
import multithreadingcore.multithreading.ticket.repository.TicketRepository;
import multithreadingcore.multithreading.user.entity.User;
import multithreadingcore.multithreading.user.entity.UserTicket;
import multithreadingcore.multithreading.user.repository.UserRepository;
import multithreadingcore.multithreading.user.repository.UserTicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExplicitSynchronizationService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final UserTicketRepository userTicketRepository;

    private Lock lock = new ReentrantLock();

    @Transactional
    public void perform(Long userId, Long ticketId, Long quantity) {

        lock.lock();
        try {
            User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("사용자에 대한 정보가 없습니다."));

            Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(()-> new IllegalArgumentException("티켓에 대한 정보가 없습니다."));

            ticket.decrease(quantity);

            ticketRepository.saveAndFlush(ticket);

            UserTicket userTicket = UserTicket.builder()
                    .user(user)
                    .ticket(ticket)
                    .build();

            userTicket.addUser(user);
            userTicketRepository.save(userTicket);
        }finally {
            // 락 봔환
            lock.unlock();
        }
    }



}

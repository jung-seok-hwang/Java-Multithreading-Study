package multithreadingcore.multithreading.stock.application;


import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

import lombok.RequiredArgsConstructor;
import multithreadingcore.multithreading.stock.entity.Stock;
import multithreadingcore.multithreading.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final EntityManager entityManager;

    @Transactional
    public void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }

    @Transactional
    public void pessimisticLocking(Long id, Long quantity) {
        Stock stock = entityManager.find(Stock.class, id, LockModeType.PESSIMISTIC_WRITE);
        stock.decrease(quantity);
        entityManager.merge(stock);
    }



}

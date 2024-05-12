package multithreadingcore.multithreading.stock.repository;


import multithreadingcore.multithreading.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

}

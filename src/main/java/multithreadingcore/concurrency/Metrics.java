package multithreadingcore.concurrency;

import lombok.Getter;

@Getter
public class Metrics {

    private long count = 0;
    private double average = 0.0;

    public void addSample(long sample) {
        double currentSum = average * count;
        count++;
        average = (currentSum + sample) / count;
    }


}

package multithreadingcore.concurrency;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Ball {

    private static final List<String> ballBox = new ArrayList<>();

    private int state = 0;

    public void save(String ballName) {
        ballBox.add(ballName);
    }

    public void check() {
        for (String ball : ballBox) {
            if (ball.equals("레드")) {
                int currentState = state; // 현재 상태를 로컬 변수에 저장
                // 상태 읽기, 변경, 쓰기 연산 사이에 인위적인 딜레이를 주어 동기화 이슈를 유발
                try {
                    Thread.sleep(100); // 딜레이 추가
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                state = currentState + 1; // 변경된 상태를 다시 state에 할당
            }
        }
    }

}

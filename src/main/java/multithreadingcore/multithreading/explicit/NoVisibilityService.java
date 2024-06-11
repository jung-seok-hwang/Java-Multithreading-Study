package multithreadingcore.multithreading.explicit;

public class NoVisibilityService {

    private static boolean ready = false;
    private static int number;

    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            while (!ready) {
                Thread.yield();
            }
            System.out.println(number);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        final ReaderThread thread = new ReaderThread();
        thread.start();
        Thread.sleep(5000);
        readyCheck();
        thread.join();
    }

    private static void readyCheck() {
        ready = true;
        number = 20;
    }
}

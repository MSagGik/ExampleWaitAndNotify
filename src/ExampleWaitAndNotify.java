import java.util.Scanner;

public class ExampleWaitAndNotify {
    public static void main(String[] args) throws InterruptedException {
        WaitAndNotify wn = new WaitAndNotify();
        // создание потоков
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wn.produce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wn.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // запуск потоков
        thread1.start();
        thread2.start();

        // ожидание выпонения потоков
        thread1.join();
        thread2.join();
    }
}

class WaitAndNotify {
    public void produce() throws InterruptedException {
        // синхронизованный блок
        synchronized (this) { // синхронизация на объекте this (WaitAndNotify())
            System.out.println("Producer thread started ...");
            wait(); // метод wait() может вызываться только в пределах синхронизованного блока
            // Описание wait(): 1) отдаёт intrinsic lock, 2) ждёт, пока будет вызван notify().
            System.out.println("Producer thread resumed ...");
        }

    }
    public void consume() throws InterruptedException {
        // ожидание produce() 2 секунды, чтобы он был первым
        Thread.sleep(2000);
        Scanner scanner = new Scanner(System.in);
        // синхронизованный блок
        synchronized (this) {
            System.out.println("Waiting for return key pressed");
            // при нажатии клавиши enter программа продолжится
            scanner.nextLine();
            notify(); // пробуждает один поток
            // notifyAll() - будит все ждущие потоки
        }
    }
}
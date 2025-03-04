
package Question6;

class NumberPrinter {
    public void printZero() {
        System.out.print(0);
    }

    public void printEven(int n) {
        System.out.print(n);
    }

    public void printOdd(int n) {
        System.out.print(n);
    }
}

class ThreadController {
    private int n;
    private NumberPrinter printer;
    private volatile int turn = 0; // Ensures correct alternation
    private final Object lock = new Object();

    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;
        this.printer = printer;
    }

    public void printZero() {
        synchronized (lock) {
            for (int i = 1; i <= n; i++) {
                while (turn % 2 != 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                printer.printZero();
                turn++;
                lock.notifyAll();
            }
        }
    }

    public void printEven() {
        synchronized (lock) {
            for (int i = 2; i <= n; i += 2) {
                while (turn % 4 != 3) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                printer.printEven(i);
                turn++;
                lock.notifyAll();
            }
        }
    }

    public void printOdd() {
        synchronized (lock) {
            for (int i = 1; i <= n; i += 2) {
                while (turn % 4 != 1) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                printer.printOdd(i);
                turn++;
                lock.notifyAll();
            }
        }
    }
}

public class Controller {
    public static void main(String[] args) {
        int n = 5; // Change this value to test different cases
        NumberPrinter printer = new NumberPrinter();
        ThreadController controller = new ThreadController(n, printer);

        Thread zeroThread = new Thread(controller::printZero);
        Thread oddThread = new Thread(controller::printOdd);
        Thread evenThread = new Thread(controller::printEven);

        zeroThread.start();
        oddThread.start();
        evenThread.start();
    }
}

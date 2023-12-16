package monitor;
public class Bridge {
    private int northCars = 0;
    private int southCars = 0;

    public synchronized void enterFromNorth() throws InterruptedException {
        while (southCars > 0) {
            wait();
        }
        northCars++;
    }

    public synchronized void exitToNorth() {
        northCars--;
        if (northCars == 0) {
            notifyAll();
        }
    }

    public synchronized void enterFromSouth() throws InterruptedException {
        while (northCars > 0) {
            wait();
        }
        southCars++;
    }

    public synchronized void exitToSouth() {
        southCars--;
        if (southCars == 0) {
            notifyAll();
        }
    }
}

public class Car extends Thread {
    private Bridge bridge;
    private String direction;

    public Car(Bridge bridge, String direction) {
        this.bridge = bridge;
        this.direction = direction;
    }

    public void run() {
        try {
            if (direction.equals("north")) {
                bridge.enterFromNorth();
                System.out.println("Car entering from North");
              
                sleep(1000);
                bridge.exitToNorth();
                System.out.println("Car exiting to North");
            } else if (direction.equals("south")) {
                bridge.enterFromSouth();
                System.out.println("Car entering from South");
                
                sleep(1000);
                bridge.exitToSouth();
                System.out.println("Car exiting to South");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Bridge bridge = new Bridge();

        new Car(bridge, "north").start();
        new Car(bridge, "south").start();
   
    }
}

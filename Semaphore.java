package semaforo;
public class Semaphore {
    private boolean signal = false;

    public synchronized void take() {
        while (this.signal) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        this.signal = true;
    }

    public synchronized void release() {
        this.signal = false;
        notify();
    }
}

public class GestorDeRecursos {
    private int recursosDisponibles;
    private final Semaphore semaforo;

    public GestorDeRecursos(int totalRecursos) {
        this.recursosDisponibles = totalRecursos;
        this.semaforo = new Semaphore();
    }

    public boolean reserva(int cantidad) {
        semaforo.take();
        try {
            if (cantidad <= recursosDisponibles) {
                recursosDisponibles -= cantidad;
                return true;
            }
            return false;
        } finally {
            semaforo.release();
        }
    }

    public void libera(int cantidad) {
        semaforo.take();
        try {
            recursosDisponibles += cantidad;
        } finally {
            semaforo.release();
        }
    }
}

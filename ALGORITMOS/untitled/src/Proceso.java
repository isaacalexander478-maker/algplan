import java.util.*;

public class Proceso {

    String id;
    int rafaga;
    int llegada;
    int prioridad;

    int tiempoRestante; 
    int tiempoEspera;
    int tiempoRetorno; 

    public Proceso(String id, int rafaga, int llegada, int prioridad) {
        this.id = id;
        this.rafaga = rafaga;
        this.llegada = llegada;
        this.prioridad = prioridad;
        this.tiempoRestante = rafaga;
    }

    
    public Proceso clonar() {
        return new Proceso(id, rafaga, llegada, prioridad);
    }
}

// 1. ALGORITMO FIFO (First In, First Out)
class AlgoritmoFIFO {
    public void ejecutar(List<Proceso> procesos) {
        System.out.println("\n--- Ejecutando FIFO ---");
       
        procesos.sort(Comparator.comparingInt(p -> p.llegada));

        int tiempoActual = 0;
        double sumaEspera = 0;
        double sumaRetorno = 0;

        for (Proceso p : procesos) {
            if (tiempoActual < p.llegada) {
                tiempoActual = p.llegada; 
            }

            p.tiempoEspera = tiempoActual - p.llegada;
            tiempoActual += p.rafaga;
            p.tiempoRetorno = tiempoActual; 

            sumaEspera += p.tiempoEspera;
            sumaRetorno += p.tiempoRetorno;
        }

        imprimirResultados(procesos, sumaEspera, sumaRetorno);
    }

    private void imprimirResultados(List<Proceso> procesos, double sumaEspera, double sumaRetorno) {
        System.out.println("Resultados:");
        for (Proceso p : procesos) {
            System.out.println("Trabajo " + p.id + " -> Espera: " + p.tiempoEspera + ", Retorno: " + p.tiempoRetorno);
        }
        System.out.println("Tiempo Medio de Espera (TEM): " + (sumaEspera / procesos.size()) + " ut");
        System.out.println("Tiempo de Retorno Medio (TRM): " + (sumaRetorno / procesos.size()) + " ut");
    }
}

// 2. ALGORITMO SJF (Shortest Job First - No Apropiativo)
class AlgoritmoSJF {
    public void ejecutar(List<Proceso> procesos) {
        System.out.println("\n--- Ejecutando SJF (No Apropiativo) ---");
        List<Proceso> pendientes = new ArrayList<>(procesos);
        List<Proceso> terminados = new ArrayList<>();

        int tiempoActual = 0;
        double sumaEspera = 0;
        double sumaRetorno = 0;

        while (!pendientes.isEmpty()) {
            List<Proceso> disponibles = new ArrayList<>();
            for (Proceso p : pendientes) {
                if (p.llegada <= tiempoActual) {
                    disponibles.add(p);
                }
            }

            if (disponibles.isEmpty()) {
                tiempoActual++; 
                continue;
            }

            
            disponibles.sort((p1, p2) -> {
                if (p1.rafaga != p2.rafaga) return Integer.compare(p1.rafaga, p2.rafaga);
                return Integer.compare(p1.llegada, p2.llegada);
            });

            Proceso p = disponibles.get(0);
            pendientes.remove(p);

            p.tiempoEspera = tiempoActual - p.llegada;
            tiempoActual += p.rafaga;
            p.tiempoRetorno = tiempoActual;

            sumaEspera += p.tiempoEspera;
            sumaRetorno += p.tiempoRetorno;
            terminados.add(p);
        }

        terminados.sort(Comparator.comparing(p -> p.id)); 
        imprimirResultados(terminados, sumaEspera, sumaRetorno);
    }

    private void imprimirResultados(List<Proceso> procesos, double sumaEspera, double sumaRetorno) {
        System.out.println("Resultados:");
        for (Proceso p : procesos) {
            System.out.println("Trabajo " + p.id + " -> Espera: " + p.tiempoEspera + ", Retorno: " + p.tiempoRetorno);
        }
        System.out.println("Tiempo Medio de Espera (TEM): " + (sumaEspera / procesos.size()) + " ut");
        System.out.println("Tiempo de Retorno Medio (TRM): " + (sumaRetorno / procesos.size()) + " ut");
    }
}

// 3. ALGORITMO PRIORIDAD 
class AlgoritmoPrioridad {
    public void ejecutar(List<Proceso> procesos) {
        System.out.println("\n--- Ejecutando Prioridad (No Apropiativo) ---");
        List<Proceso> pendientes = new ArrayList<>(procesos);
        List<Proceso> terminados = new ArrayList<>();

        int tiempoActual = 0;
        double sumaEspera = 0;
        double sumaRetorno = 0;

        while (!pendientes.isEmpty()) {
            List<Proceso> disponibles = new ArrayList<>();
            for (Proceso p : pendientes) {
                if (p.llegada <= tiempoActual) {
                    disponibles.add(p);
                }
            }

            if (disponibles.isEmpty()) {
                tiempoActual++;
                continue;
            }

           
            disponibles.sort((p1, p2) -> {
                if (p1.prioridad != p2.prioridad) return Integer.compare(p1.prioridad, p2.prioridad);
                return Integer.compare(p1.llegada, p2.llegada); // Desempate por llegada
            });

            Proceso p = disponibles.get(0);
            pendientes.remove(p);

            p.tiempoEspera = tiempoActual - p.llegada;
            tiempoActual += p.rafaga;
            p.tiempoRetorno = tiempoActual;

            sumaEspera += p.tiempoEspera;
            sumaRetorno += p.tiempoRetorno;
            terminados.add(p);
        }

        terminados.sort(Comparator.comparing(p -> p.id));
        imprimirResultados(terminados, sumaEspera, sumaRetorno);
    }

    private void imprimirResultados(List<Proceso> procesos, double sumaEspera, double sumaRetorno) {
        System.out.println("Resultados:");
        for (Proceso p : procesos) {
            System.out.println("Trabajo " + p.id + " -> Espera: " + p.tiempoEspera + ", Retorno: " + p.tiempoRetorno);
        }
        System.out.println("Tiempo Medio de Espera (TEM): " + (sumaEspera / procesos.size()) + " ut");
        System.out.println("Tiempo de Retorno Medio (TRM): " + (sumaRetorno / procesos.size()) + " ut");
    }
}

// 4. ALGORITMO ROUND ROBIN (Apropiativo)
class AlgoritmoRoundRobin {
    public void ejecutar(List<Proceso> procesos, int quantum) {
        System.out.println("\n--- Ejecutando Round Robin (q=" + quantum + ") ---");

       
        procesos.sort(Comparator.comparingInt(p -> p.llegada));

        Queue<Proceso> colaReady = new LinkedList<>();
        int tiempoActual = 0;
        int completados = 0;
        int indexLlegadas = 0;
        int totalProcesos = procesos.size();

        double sumaEspera = 0;
        double sumaRetorno = 0;

        while (completados < totalProcesos) {
            
            while (indexLlegadas < totalProcesos && procesos.get(indexLlegadas).llegada <= tiempoActual) {
                colaReady.add(procesos.get(indexLlegadas));
                indexLlegadas++;
            }

            if (colaReady.isEmpty()) {
                tiempoActual++;
                continue;
            }

            Proceso p = colaReady.poll();

            
            int tiempoEjecucion = Math.min(quantum, p.tiempoRestante);
            tiempoActual += tiempoEjecucion;
            p.tiempoRestante -= tiempoEjecucion;

            
            while (indexLlegadas < totalProcesos && procesos.get(indexLlegadas).llegada <= tiempoActual) {
                colaReady.add(procesos.get(indexLlegadas));
                indexLlegadas++;
            }

            
            if (p.tiempoRestante == 0) {
                p.tiempoRetorno = tiempoActual;
                
                p.tiempoEspera = p.tiempoRetorno - p.llegada - p.rafaga;

                sumaRetorno += p.tiempoRetorno;
                sumaEspera += p.tiempoEspera;
                completados++;
            } else {
                
                colaReady.add(p);
            }
        }

        procesos.sort(Comparator.comparing(p -> p.id)); // Restaurar orden alfabético
        imprimirResultados(procesos, sumaEspera, sumaRetorno);
    }

    private void imprimirResultados(List<Proceso> procesos, double sumaEspera, double sumaRetorno) {
        System.out.println("Resultados:");
        for (Proceso p : procesos) {
            System.out.println("Trabajo " + p.id + " -> Espera: " + p.tiempoEspera + ", Retorno: " + p.tiempoRetorno);
        }
        System.out.println("Tiempo Medio de Espera (TEM): " + (sumaEspera / procesos.size()) + " ut");
        System.out.println("Tiempo de Retorno Medio (TRM): " + (sumaRetorno / procesos.size()) + " ut");
    }

}

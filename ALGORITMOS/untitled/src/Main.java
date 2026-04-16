import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Proceso> listaOriginal = new ArrayList<>();

        System.out.println("=== SISTEMA DE PLANIFICACIÓN DE PROCESOS ===");
        System.out.print("Ingrese la cantidad de trabajos (procesos): ");
        int n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.println("\nDatos del Trabajo " + (i + 1) + ":");
            System.out.print("Nombre/Letra (Ej: A, B): ");
            String id = scanner.next();
            System.out.print("Ráfaga de CPU: ");
            int rafaga = scanner.nextInt();
            System.out.print("Tiempo de Llegada: ");
            int llegada = scanner.nextInt();
            System.out.print("Prioridad (Número menor = más prioridad): ");
            int prioridad = scanner.nextInt();

            listaOriginal.add(new Proceso(id, rafaga, llegada, prioridad));
        }

        int opcion;
        do {
            System.out.println("\n=== MENÚ DE ALGORITMOS ===");
            System.out.println("1. FIFO");
            System.out.println("2. SJF (No Apropiativo)");
            System.out.println("3. Prioridad (No Apropiativo)");
            System.out.println("4. Round Robin");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            // Clonamos la lista para que las variables temporales se reinicien en cada algoritmo
            List<Proceso> procesosAUsar = clonarLista(listaOriginal);

            switch (opcion) {
                case 1:
                    new AlgoritmoFIFO().ejecutar(procesosAUsar);
                    break;
                case 2:
                    new AlgoritmoSJF().ejecutar(procesosAUsar);
                    break;
                case 3:
                    new AlgoritmoPrioridad().ejecutar(procesosAUsar);
                    break;
                case 4:
                    System.out.print("Ingrese el valor del quantum (q): ");
                    int quantum = scanner.nextInt();
                    new AlgoritmoRoundRobin().ejecutar(procesosAUsar, quantum);
                    break;
                case 5:

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 5);

        scanner.close();
    }

    // Helper para generar una copia limpia de los datos iniciales
    private static List<Proceso> clonarLista(List<Proceso> original) {
        List<Proceso> copia = new ArrayList<>();
        for (Proceso p : original) {
            copia.add(p.clonar());
        }
        return copia;

    }
}
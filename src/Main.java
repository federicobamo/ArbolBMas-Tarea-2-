import java.util.Scanner;

public class Main {

    private static final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println(" ----- Árbol B+ -----");
        int orden = leerEntero("Orden del árbol (mínimo 3): ", 3);
        ArbolBMas arbol = new ArbolBMas(orden);
        menu(arbol);
        System.out.println("Fin");
    }

    public static void menu(ArbolBMas arbol) {
        int opcion;

        do {

            mostrarOpciones();
            opcion = leerEntero("Elija una opción: ", 0);

            switch (opcion) {
                case 1 -> insertarElemento(arbol);
                case 2 -> buscarElemento(arbol);
                case 3 -> eliminarElemento(arbol);
                case 4 -> recorrerPorRango(arbol);
                case 5 -> arbol.imprimirArbol();
                case 0 -> { }
                default -> mostrarOpcionInvalida();
            }

        } while (opcion != 0);
    }

    private static void mostrarOpciones() {
        System.out.println("----- Menú -----");
        System.out.println("1. Insertar un elemento");
        System.out.println("2. Buscar un elemento");
        System.out.println("3. Eliminar un elemento");
        System.out.println("4. Recorrer por rango");
        System.out.println("5. Ver el árbol");
        System.out.println("0. Salir");

    }

    private static void mostrarOpcionInvalida() {
        System.out.println("Opción inválida. Elija un número del 0 al 5");
    }

    private static void insertarElemento(ArbolBMas arbol) {

        int clave = leerEntero("Clave (número entero): ", Integer.MIN_VALUE);
        String dato = leerTexto("Dato asociado: ");
        arbol.insertar(clave, dato);
        System.out.println("Elemento " + clave + " guardado");
    }

    private static void buscarElemento(ArbolBMas arbol) {

        int clave = leerEntero("Clave a buscar: ", Integer.MIN_VALUE);
        String dato = arbol.buscar(clave);
        if (dato == null) {
            System.out.println("La clave " + clave + " no está");
        } else {
            System.out.println("Encontrado: " + clave + " --> " + dato);
        }
    }

    private static void eliminarElemento(ArbolBMas arbol) {

        int clave = leerEntero("Clave a eliminar: ", Integer.MIN_VALUE);
        if (arbol.eliminar(clave)) {
            System.out.println("Elemento " + clave + " eliminado");

        } else {
            System.out.println("La clave " + clave + " no está");
        }

    }

    private static void recorrerPorRango(ArbolBMas arbol) {
        int inicio = leerEntero("Clave inicial: ", Integer.MIN_VALUE);
        int n = leerEntero("¿Cuántos elementos mostrar?: ", 1);
        arbol.recorrerRango(inicio, n);
    }

    private static int leerEntero(String mensaje, int minimo) {

        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= minimo) {
                    return valor;
                }
                System.out.println("Debe ser al menos " + minimo);
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número entero");
            }
        }
    }

    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("El texto no puede estar vacío.");
        }
    }
}





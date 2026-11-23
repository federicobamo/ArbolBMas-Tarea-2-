import java.util.ArrayList;

public class ArbolBMas {

    // Atributos
    private final int orden;
    private NodoArbolBMas raiz;

    // Métodos
    // Constructor
    public ArbolBMas(int orden) {
        this.orden = orden;
        raiz = new NodoArbolBMas(true);
    }

    private int minimoClaves() {
        return (orden + 1) / 2 - 1;

    }

    private int indeceHijo(NodoArbolBMas nodo, int clave) {
        int i = 0;
        while (i < nodo.getClaves().size() && clave >= nodo.getClaves().get(i)) {
            i++;
        }
        return i;
    }

    private NodoArbolBMas buscarHoja(int clave) {
        NodoArbolBMas nodo = raiz;
        while (!nodo.esHoja()) {
            nodo = nodo.getHijos().get(indeceHijo(nodo, clave));
        }
        return nodo;
    }

    // Inserción de una llave en el árbol B+
    public void insertar(int clave, String dato) {
        // Se inserta primero de forma recursiva
        insertarRecursivo(this.raiz, clave, dato);

        // Se verifica si la raíz se desbordó
        if (this.raiz.getClaves().size() == orden) {
            NodoArbolBMas nuevaRaiz = new NodoArbolBMas(false);
            nuevaRaiz.getHijos().add(this.raiz);
            dividirHijo(nuevaRaiz, 0);
            this.raiz = nuevaRaiz;
        }
    }

    // Inserción recursiva de una llave
    // Permite que los nodos se desborden temporalmente y los divide al regresar de la recursión
    private void insertarRecursivo(NodoArbolBMas nodo, int llave, String dato) {
        int i = nodo.getClaves().size() - 1;

        if (nodo.esHoja()) {
            while (i >= 0 && llave < nodo.getClaves().get(i)) {
                i--;
            }

            if (i >= 0 && nodo.getClaves().get(i) == llave) {
                nodo.getDatos().set(i, dato);
                return;
            }
            nodo.getClaves().add(i + 1, llave);
            nodo.getDatos().add(i + 1, dato);

        } else {
            while (i >= 0 && llave < nodo.getClaves().get(i)) {
                i--;
            }
            i++;
            NodoArbolBMas hijo = nodo.getHijos().get(i);

            // Llamada recursiva para insertar la llave primero en el hijo correspondiente
            insertarRecursivo(hijo, llave, dato);

            // Evaluación de desbordamiento al retornar de la recursividad
            if (hijo.getClaves().size() == orden) {
                dividirHijo(nodo, i);
            }
        }
    }

    // Dividir un nodo que está lleno
    private void dividirHijo(NodoArbolBMas padre, int indice) {
        NodoArbolBMas nodoLleno = padre.getHijos().get(indice);
        NodoArbolBMas nuevoNodo = new NodoArbolBMas(nodoLleno.esHoja());
        int mitad = orden / 2;

        if (nodoLleno.esHoja()) {
            // En nodos hoja, se mantiene la llave media en ambos lados
            int total = nodoLleno.getClaves().size();
            nuevoNodo.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(mitad, nodoLleno.getClaves().size())));
            nuevoNodo.setDatos(new ArrayList<>(nodoLleno.getDatos().subList(mitad, total)));
            nodoLleno.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(0, mitad)));
            nodoLleno.setDatos(new ArrayList<>(nodoLleno.getDatos().subList(0, mitad)));

            nuevoNodo.setSiguiente(nodoLleno.getSiguiente());
            nodoLleno.setSiguiente(nuevoNodo);

            // El padre recibe como separador la primera llave del nuevo nodo
            int llaveMedia = nuevoNodo.getClaves().getFirst();
            padre.getClaves().add(indice, llaveMedia);
            padre.getHijos().add(indice + 1, nuevoNodo);
        } else {
            // En nodos internos, la llave media sube y se elimina del hijo
            int claveMedia = nodoLleno.getClaves().get(mitad);
            nuevoNodo.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(mitad + 1, nodoLleno.getClaves().size())));
            nodoLleno.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(0, mitad)));

            // Mover los hijos del nodo dividido
            nuevoNodo.setHijos(new ArrayList<>(nodoLleno.getHijos().subList(mitad + 1, nodoLleno.getHijos().size())));
            nodoLleno.setHijos(new ArrayList<>(nodoLleno.getHijos().subList(0, mitad + 1)));

            padre.getClaves().add(indice, claveMedia);
            padre.getHijos().add(indice + 1, nuevoNodo);
        }
    }

    // Impresión del árbol como parte de la interfaz pública del árbol
    public void imprimirArbol() {
        imprimirNodo(raiz, "", true);
    }

    // Impresión recursiva privada de los nodos a partir de uno inicial
    private void imprimirNodo(NodoArbolBMas nodo, String indentacion, boolean esUltimo) {
        System.out.println(indentacion + "+- " + (nodo.esHoja() ? "Hoja > " : "Interno > ") + nodo.getClaves());
        indentacion += esUltimo ? "   " : "|  ";
        for (int i = 0; i < nodo.getHijos().size(); i++) {
            imprimirNodo(nodo.getHijos().get(i), indentacion, i == (nodo.getHijos().size() - 1));
        }
    }

    // Búsqueda de un nodo como parte de la interfaz pública del árbol
    public String buscar(int clave) {
        NodoArbolBMas hoja = buscarHoja(clave);
        int pos = hoja.getClaves().indexOf(clave);

        if (pos == -1) {
            return null;
        }
        return hoja.getDatos().get(pos);
    }

    // Búsqueda recursiva privada de un nodo a partir de uno inicial
    private boolean buscarNodo(NodoArbolBMas nodo, int llave) {
        int i = 0;
        while (i < nodo.getClaves().size() && llave > nodo.getClaves().get(i)) {
            i++;
        }
        if (i < nodo.getClaves().size() && llave == nodo.getClaves().get(i)) {
            return true;
        }
        if (nodo.esHoja()) {
            return false;
        } else {
            return buscarNodo(nodo.getHijos().get(i), llave);
        }
    }


    // Eliminación

    public boolean eliminar(int clave) {

        boolean eliminado = eliminarRecursivo(raiz, clave);

        if (!raiz.esHoja() && raiz.getClaves().isEmpty()) {
            raiz = raiz.getHijos().getFirst();
        }
        return eliminado;
    }

    public boolean eliminarRecursivo(NodoArbolBMas nodo, int clave) {
        if (nodo.esHoja()) {
            int pos = nodo.getClaves().indexOf(clave);
            if (pos == -1) {
                return false;
            }
            nodo.getClaves().remove(pos);
            nodo.getDatos().remove(pos);
            return true;
        }

        int i = indeceHijo(nodo, clave);
        NodoArbolBMas hijo = nodo.getHijos().get(i);
        boolean eliminado = eliminarRecursivo(hijo, clave);

        if (eliminado && hijo.getClaves().size() < minimoClaves()) {
            rebalancear(nodo, i);

        }
        return eliminado;

    }

    private void rebalancear(NodoArbolBMas padre, int i) {
        NodoArbolBMas izquierdo = null;
        NodoArbolBMas derecho = null;

        if (i > 0) {
            izquierdo = padre.getHijos().get(i - 1);
        }
        if (i < padre.getHijos().size() - 1) {
            derecho = padre.getHijos().get(i + 1);
        }

        int min = minimoClaves();
        if (izquierdo != null && izquierdo.getClaves().size() > min) {
            prestarDeIzquierda(padre, i);
        } else if (derecho != null && derecho.getClaves().size() > min) {
            prestarDeDerecha(padre, i);
        } else if (izquierdo != null) {
            fusionar(padre, i - 1);
        } else {
            fusionar(padre, i);
        }
    }

    private void prestarDeIzquierda(NodoArbolBMas padre, int i) {
        NodoArbolBMas hijo = padre.getHijos().get(i);
        NodoArbolBMas izquierdo = padre.getHijos().get(i - 1);
        int ultima = izquierdo.getClaves().size() - 1;

        if (hijo.esHoja()) {

            hijo.getClaves().addFirst(izquierdo.getClaves().remove(ultima));
            hijo.getDatos().addFirst(izquierdo.getDatos().remove(ultima));
            padre.getClaves().set(i - 1, hijo.getClaves().getFirst());
        } else {

            hijo.getClaves().addFirst(padre.getClaves().get(i - 1));
            padre.getClaves().set(i - 1, izquierdo.getClaves().remove(ultima));
            hijo.getHijos().addFirst(izquierdo.getHijos().removeLast());
        }
    }


    private void prestarDeDerecha(NodoArbolBMas padre, int i) {
        NodoArbolBMas hijo = padre.getHijos().get(i);
        NodoArbolBMas derecho = padre.getHijos().get(i + 1);
        if (hijo.esHoja()) {

            hijo.getClaves().add(derecho.getClaves().removeFirst());
            hijo.getDatos().add(derecho.getDatos().removeFirst());
            padre.getClaves().set(i, derecho.getClaves().getFirst());
        } else {

            hijo.getClaves().add(padre.getClaves().get(i));
            padre.getClaves().set(i, derecho.getClaves().removeFirst());
            hijo.getHijos().add(derecho.getHijos().removeFirst());

        }
    }

    private void fusionar(NodoArbolBMas padre, int i) {
        NodoArbolBMas izquierdo = padre.getHijos().get(i);
        NodoArbolBMas derecho = padre.getHijos().get(i + 1);

        if (izquierdo.esHoja()) {

            izquierdo.getClaves().addAll(derecho.getClaves());
            izquierdo.getDatos().addAll(derecho.getDatos());
            izquierdo.setSiguiente(derecho.getSiguiente());

        } else {

            izquierdo.getClaves().add(padre.getClaves().get(i));
            izquierdo.getClaves().addAll(derecho.getClaves());
            izquierdo.getHijos().addAll(derecho.getHijos());

        }
        padre.getClaves().remove(i);
        padre.getHijos().remove(i + 1);
    }

    public void recorrerRango(int claveInicio, int n) {

        NodoArbolBMas hoja = buscarHoja(claveInicio);

        int pos = 0;
        while (pos < hoja.getClaves().size() && hoja.getClaves().get(pos) < claveInicio) {
            pos++;
        }

        int impresos = 0;
        while (hoja != null && impresos < n) {
            if (pos < hoja.getClaves().size()) {
                System.out.println(" " + hoja.getClaves().get(pos) + " --> " + hoja.getDatos().get(pos));
                impresos++;
                pos++;

            } else {
                hoja = hoja.getSiguiente();
                pos = 0;
            }
        }

        if (impresos == 0) {
            System.out.println("No hay elementos desde esa clave");
        }

    }
}

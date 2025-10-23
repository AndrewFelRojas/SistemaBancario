package Excepciones;

/**
 * Excepción lanzada cuando no hay saldo suficiente para realizar una operación.
 * Esta excepción puede ser encadenada con otras excepciones relacionadas.
 * 
 * @author Andres
 * @version 1.0
 */
public class SaldoInsuficienteException extends Exception {
    
    /**
     * Constructor que crea la excepción con un mensaje descriptivo.
     * 
     * @param mensaje Descripción del error
     */
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Constructor que permite encadenar esta excepción con otra excepción causa.
     * Útil para rastrear la cadena de errores.
     * 
     * @param mensaje Descripción del error
     * @param causa Excepción que causó este error
     */
    public SaldoInsuficienteException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
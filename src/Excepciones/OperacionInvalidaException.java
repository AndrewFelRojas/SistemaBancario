package Excepciones;

/**
 * Excepción lanzada cuando se intenta realizar una operación inválida en una cuenta bancaria.
 * Por ejemplo: retiros que exceden límites, montos negativos, o transacciones no permitidas.
 * 
 * @author Andres
 * @version 1.0
 */
public class OperacionInvalidaException extends Exception {
    
    /**
     * Constructor que crea la excepción con un mensaje descriptivo.
     * 
     * @param mensaje Descripción del error
     */
    public OperacionInvalidaException(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Constructor que permite encadenar esta excepción con otra excepción causa.
     * Permite rastrear el origen del error a través de múltiples niveles.
     * 
     * @param mensaje Descripción del error
     * @param causa Excepción que causó este error (puede ser SaldoInsuficienteException u otra)
     */
    public OperacionInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
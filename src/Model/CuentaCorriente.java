package Model;

import Excepciones.OperacionInvalidaException;
import Excepciones.SaldoInsuficienteException;

/**
 * Cuenta Corriente que cobra comisión por cada retiro.
 * Permite sobregiros hasta un límite establecido.
 * 
 * @author Andres
 * @version 1.0
 */
public class CuentaCorriente extends CuentaBancaria {
    private double comisionFija; // Comisión que se cobra por cada retiro
    private double limiteSobregiro; // Cuánto puede quedar en negativo
    private int numeroChequera; // Número de chequera asociada
    
    /**
     * Constructor de CuentaCorriente.
     * 
     * @param titular Nombre del titular
     * @param saldo Saldo inicial
     * @param numeroCuenta Número único de cuenta
     * @param comisionFija Comisión por retiro (ej: 2.5 = $2.50)
     * @param limiteSobregiro Límite de sobregiro (ej: 500 permite -$500)
     * @param numeroChequera Número de chequera
     */
    public CuentaCorriente(String titular, double saldo, int numeroCuenta, 
                           double comisionFija, double limiteSobregiro, 
                           int numeroChequera) {
        super(titular, saldo, numeroCuenta);
        this.comisionFija = comisionFija;
        this.limiteSobregiro = limiteSobregiro;
        this.numeroChequera = numeroChequera;
    }
    
    /**
     * Deposita dinero en la cuenta corriente.
     * 
     * 
     * @param monto Cantidad a depositar
     * @return Monto depositado
     * @throws OperacionInvalidaException Si el monto es <= 0
     */
    @Override
    public double depositar(double monto) throws OperacionInvalidaException {
        if (monto <= 0) {
            throw new OperacionInvalidaException(
                "El monto a depositar debe ser mayor a cero. Monto recibido: " + monto
            );
        }
        setSaldo(getSaldo() + monto);
        return monto;
    }
    
    /**
     * Retira dinero de la cuenta corriente.
     * - Cobra una COMISIÓN FIJA por cada retiro
     * - Permite SOBREGIRO hasta el límite establecido
     * 
     * IMPORTANTE: El retiro real = monto + comisión
     * 
     * @param monto Cantidad a retirar (sin incluir comisión)
     * @return Monto retirado (sin comisión)
     * @throws SaldoInsuficienteException Si excede el límite de sobregiro
     * @throws OperacionInvalidaException Si el monto es <= 0
     */
    @Override
    public double retirar(double monto) throws SaldoInsuficienteException, 
                                                OperacionInvalidaException {
        // Validación: monto debe ser positivo
        if (monto <= 0) {
            throw new OperacionInvalidaException(
                "El monto a retirar debe ser mayor a cero. Monto: " + monto
            );
        }
        
        // Calcular el monto total incluyendo comisión
        double montoTotal = monto + comisionFija;
        
        // Calcular el nuevo saldo después del retiro
        double nuevoSaldo = getSaldo() - montoTotal;
        
        // Validar que no se exceda el límite de sobregiro
        // Si limiteSobregiro = 500, puede llegar hasta -500
        if (nuevoSaldo < -limiteSobregiro) {
            // EXCEPCIÓN ENCADENADA
            throw new OperacionInvalidaException(
                "Operación rechazada. Excede el límite de sobregiro de $" + limiteSobregiro,
                new SaldoInsuficienteException(
                    "Saldo actual: $" + getSaldo() + 
                    ", Monto + comisión: $" + montoTotal + 
                    ", Nuevo saldo sería: $" + nuevoSaldo
                )
            );
        }
        
        // Si pasa las validaciones, realizar el retiro
        setSaldo(nuevoSaldo);
        return monto;
    }
    
    /**
     * Las cuentas corrientes NO generan intereses.
     * @return 0 (no hay intereses)
     */
    @Override
    public double calcularIntereses() {
        return 0; // Las cuentas corrientes no generan intereses
    }
    
    /**
     * Verifica si la cuenta está en sobregiro (saldo negativo).
     * 
     * @return true si el saldo es negativo
     */
    public boolean estaEnSobregiro() {
        return getSaldo() < 0;
    }
    
    @Override
    public String toCSV() {
        return "CORRIENTE," + super.toCSV() + "," + comisionFija + "," + 
               limiteSobregiro + "," + numeroChequera;
    }
    
    // ==================== GETTERS Y SETTERS ====================
    
    public double getComisionFija() {
        return comisionFija;
    }
    
    public void setComisionFija(double comisionFija) {
        this.comisionFija = comisionFija;
    }
    
    public double getLimiteSobregiro() {
        return limiteSobregiro;
    }
    
    public void setLimiteSobregiro(double limiteSobregiro) {
        this.limiteSobregiro = limiteSobregiro;
    }
    
    public int getNumeroChequera() {
        return numeroChequera;
    }
    
    public void setNumeroChequera(int numeroChequera) {
        this.numeroChequera = numeroChequera;
    }
    
    @Override
    public String toString() {
        String sobregiro = estaEnSobregiro() ? " [EN SOBREGIRO]" : "";
        return super.toString() + " | Tipo: CORRIENTE | Comisión: $" + comisionFija + 
               " | Sobregiro disponible: $" + limiteSobregiro + sobregiro;
    }
}
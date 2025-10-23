package Model;

import Excepciones.OperacionInvalidaException;
import Excepciones.SaldoInsuficienteException;

/**
 * Cuenta de Ahorros que genera intereses mensuales.
 * Tiene restricción en el número de retiros permitidos por período.
 * 
 * @author Andres
 * @version 1.0
 */
public class CuentaAhorros extends CuentaBancaria {
    private double tasaInteres; // Ejemplo: 0.02 representa 2%
    private String periodicidadInteres; // "Mensual", "Trimestral", etc.
    private int retirosPermitidos; // Máximo de retiros en el período
    private int retirosRealizados; // Contador de retiros actuales
    
    /**
     * Constructor de CuentaAhorros.
     * 
     * @param titular Nombre del titular
     * @param saldo Saldo inicial
     * @param numeroCuenta Número único de cuenta
     * @param tasaInteres Tasa de interés (0.02 = 2%)
     * @param periodicidadInteres Frecuencia de cálculo ("Mensual", "Trimestral")
     * @param retirosPermitidos Número máximo de retiros por período
     */
    public CuentaAhorros(String titular, double saldo, int numeroCuenta, 
                         double tasaInteres, String periodicidadInteres, 
                         int retirosPermitidos) {
        super(titular, saldo, numeroCuenta);
        this.tasaInteres = tasaInteres;
        this.periodicidadInteres = periodicidadInteres;
        this.retirosPermitidos = retirosPermitidos;
        this.retirosRealizados = 0;
    }
    
    /**
     * Deposita dinero en la cuenta de ahorros.
     * 
     * POLIMORFISMO: Implementación específica para CuentaAhorros
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
     * Retira dinero de la cuenta de ahorros.
     * Valida que no se exceda el límite de retiros permitidos.
     * 
     * EXCEPCIONES ENCADENADAS: 
     * Si se excede el límite, lanza OperacionInvalidaException
     * que tiene como causa una SaldoInsuficienteException.
     * 
     * @param monto Cantidad a retirar
     * @return Monto retirado
     * @throws SaldoInsuficienteException Si no hay saldo suficiente
     * @throws OperacionInvalidaException Si el monto es <= 0 o se excede el límite de retiros
     */
    @Override
    public double retirar(double monto) throws SaldoInsuficienteException, 
                                                OperacionInvalidaException {
        // Validación 1: Monto debe ser positivo
        if (monto <= 0) {
            throw new OperacionInvalidaException(
                "El monto a retirar debe ser mayor a cero. Monto recibido: " + monto
            );
        }
        
        // Validación 2: Verificar límite de retiros
        if (retirosRealizados >= retirosPermitidos) {
            // EXCEPCIÓN ENCADENADA: La causa es el límite de retiros
            throw new OperacionInvalidaException(
                "Límite de retiros excedido. Permitidos: " + retirosPermitidos + 
                ", Realizados: " + retirosRealizados,
                new SaldoInsuficienteException("No hay retiros disponibles en este período")
            );
        }
        
        // Validación 3: Verificar saldo suficiente
        if (getSaldo() < monto) {
            throw new SaldoInsuficienteException(
                "Saldo insuficiente. Disponible: $" + getSaldo() + 
                ", Solicitado: $" + monto
            );
        }
        
        // Si pasa todas las validaciones, realizar el retiro
        setSaldo(getSaldo() - monto);
        retirosRealizados++;
        return monto;
    }
    
    /**
     * Calcula e aplica los intereses al saldo de la cuenta.
     * Los intereses se calculan sobre el saldo actual.
     * 
     * POLIMORFISMO: Implementación específica de CuentaAhorros
     * 
     * @return Monto de intereses generados
     */
    @Override
    public double calcularIntereses() {
        double intereses = getSaldo() * tasaInteres;
        setSaldo(getSaldo() + intereses); // Suma los intereses al saldo
        return intereses;
    }
    
    /**
     * Reinicia el contador de retiros al inicio de un nuevo período.
     */
    public void reiniciarRetiros() {
        this.retirosRealizados = 0;
    }
    
    @Override
    public String toCSV() {
        return "AHORROS," + super.toCSV() + "," + tasaInteres + "," + 
               periodicidadInteres + "," + retirosPermitidos + "," + retirosRealizados;
    }
    
    // ==================== GETTERS Y SETTERS ====================
    
    public double getTasaInteres() {
        return tasaInteres;
    }
    
    public void setTasaInteres(double tasaInteres) {
        this.tasaInteres = tasaInteres;
    }
    
    public String getPeriodicidadInteres() {
        return periodicidadInteres;
    }
    
    public void setPeriodicidadInteres(String periodicidadInteres) {
        this.periodicidadInteres = periodicidadInteres;
    }
    
    public int getRetirosPermitidos() {
        return retirosPermitidos;
    }
    
    public int getRetirosRealizados() {
        return retirosRealizados;
    }
    
    @Override
    public String toString() {
        return super.toString() + " | Tipo: AHORROS | Tasa: " + (tasaInteres * 100) + 
               "% | Retiros: " + retirosRealizados + "/" + retirosPermitidos;
    }
}
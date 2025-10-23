package Model;

import Excepciones.OperacionInvalidaException;
import Excepciones.SaldoInsuficienteException;

/**
 * Clase abstracta que encapsula los tipos de cuentas bancarias.
 * Define el comportamiento común de todas las cuentas y obliga a las subclases
 * a implementar métodos específicos según su tipo.
 * 
 * @author Andres
 * @version 1.0
 */
public abstract class CuentaBancaria {
    private String titular;
    private double saldo;
    private int numeroCuenta;
    
    /**
     * Constructor de la clase CuentaBancaria.
     * 
     * @param titular Nombre completo del titular de la cuenta
     * @param saldo Saldo inicial de la cuenta (debe ser >= 0)
     * @param numeroCuenta Número único que identifica la cuenta
     */
    public CuentaBancaria(String titular, double saldo, int numeroCuenta) {
        this.titular = titular;
        this.saldo = saldo;
        this.numeroCuenta = numeroCuenta;
    }
    
    /**
     * Método abstracto para depositar dinero en la cuenta.
     * Cada tipo de cuenta puede tener validaciones o comportamientos específicos.
     * 
     * @param monto Cantidad a depositar
     * @return Monto depositado
     * @throws OperacionInvalidaException Si el monto es <= 0 o la operación no es válida
     */
    public abstract double depositar(double monto) throws OperacionInvalidaException;
    
    /**
     * Método abstracto para retirar dinero de la cuenta.
     * Cada tipo de cuenta tiene sus propias restricciones:
     * - CuentaAhorros: límite de retiros
     * - CuentaCorriente: cobra comisión y permite sobregiro
     * - CuentaEmpresarial: límite diario
     * 
     * @param monto Cantidad a retirar
     * @return Monto retirado
     * @throws SaldoInsuficienteException Si no hay suficiente saldo
     * @throws OperacionInvalidaException Si el monto es <= 0 o excede límites
     */
    public abstract double retirar(double monto) throws SaldoInsuficienteException, 
                                                         OperacionInvalidaException;
    /**
     * Método abstracto para calcular intereses.
     * Cada tipo de cuenta tiene su propia forma de calcular intereses:
     * - CuentaAhorros: genera intereses mensuales
     * - CuentaCorriente: no genera intereses (retorna 0)
     * - CuentaEmpresarial: genera intereses preferenciales
     * 
     * @return El monto de intereses generados
     */
    public abstract double calcularIntereses();
    
    /**
     * Convierte los datos de la cuenta a formato CSV para persistencia.
     * Las subclases pueden sobrescribir este método para agregar sus propios datos.
     * 
     * @return String en formato CSV: titular,saldo,numeroCuenta
     */
    public String toCSV() {
        return titular + "," + saldo + "," + numeroCuenta;
    }
    
    // ==================== GETTERS Y SETTERS ====================
    
    public String getTitular() {
        return titular;
    }
    
    public void setTitular(String titular) {
        this.titular = titular;
    }
    
    public double getSaldo() {
        return saldo;
    }
    
    /**
     * Método protegido para que solo las subclases puedan modificar el saldo directamente.
     * El saldo normalmente se modifica a través de depositar() y retirar().
     */
    protected void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
    public int getNumeroCuenta() {
        return numeroCuenta;
    }
    
    public void setNumeroCuenta(int numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
    
    @Override
    public String toString() {
        return "Cuenta #" + numeroCuenta + " | Titular: " + titular + " | Saldo: $" + 
               String.format("%.2f", saldo);
    }
}
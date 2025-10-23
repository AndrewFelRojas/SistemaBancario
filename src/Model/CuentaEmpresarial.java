package Model;

import Excepciones.OperacionInvalidaException;
import Excepciones.SaldoInsuficienteException;

/**
 * Cuenta Empresarial con límite de retiro diario.
 * Diseñada para empresas con restricciones de seguridad.
 * 
 * @author Andres
 * @version 1.0
 */
public class CuentaEmpresarial extends CuentaBancaria {
    private String tipoEmpresa; // "S.A.", "S.A.S.", "Ltda.", etc.
    private int registroTributario; // NIT o RUT
    private double limiteDiario; // Máximo que se puede retirar por día
    private double retiradoHoy; // Acumulado de retiros del día actual
    
    /**
     * Constructor de CuentaEmpresarial.
     * 
     * @param titular Razón social de la empresa
     * @param saldo Saldo inicial
     * @param numeroCuenta Número único de cuenta
     * @param tipoEmpresa Tipo de empresa ("S.A.", "S.A.S.", etc.)
     * @param registroTributario NIT o RUT de la empresa
     * @param limiteDiario Límite máximo de retiro por día
     */
    public CuentaEmpresarial(String titular, double saldo, int numeroCuenta, 
                             String tipoEmpresa, int registroTributario, 
                             double limiteDiario) {
        super(titular, saldo, numeroCuenta);
        this.tipoEmpresa = tipoEmpresa;
        this.registroTributario = registroTributario;
        this.limiteDiario = limiteDiario;
        this.retiradoHoy = 0;
    }
    
    /**
     * Deposita dinero en la cuenta empresarial.
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
     * Retira dinero de la cuenta empresarial.
     * Valida que no se exceda el LÍMITE DIARIO de retiros.
     * 
     * EXCEPCIONES ENCADENADAS:
     * Si se excede el límite diario, lanza OperacionInvalidaException
     * que tiene como causa una SaldoInsuficienteException.
     * 
     * @param monto Cantidad a retirar
     * @return Monto retirado
     * @throws SaldoInsuficienteException Si no hay saldo suficiente
     * @throws OperacionInvalidaException Si el monto es <= 0 o excede el límite diario
     */
    @Override
    public double retirar(double monto) throws SaldoInsuficienteException, 
                                                OperacionInvalidaException {
        // Validación 1: Monto debe ser positivo
        if (monto <= 0) {
            throw new OperacionInvalidaException(
                "El monto a retirar debe ser mayor a cero. Monto: " + monto
            );
        }
        
        // Validación 2: Verificar límite diario
        if (retiradoHoy + monto > limiteDiario) {
            // EXCEPCIÓN ENCADENADA
            throw new OperacionInvalidaException(
                "Límite diario excedido. Límite: $" + limiteDiario + 
                ", Ya retirado hoy: $" + retiradoHoy + 
                ", Intenta retirar: $" + monto,
                new SaldoInsuficienteException(
                    "Disponible hoy: $" + (limiteDiario - retiradoHoy)
                )
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
        retiradoHoy += monto;
        
        return monto;
    }
    
    /**
     * Las cuentas empresariales generan un interés preferencial del 0.5%.
     * 
     * 
     * @return Monto de intereses generados
     */
    @Override
    public double calcularIntereses() {
        // Tasa preferencial para empresas: 0.5% mensual
        double tasaPreferencial = 0.005;
        double intereses = getSaldo() * tasaPreferencial;
        setSaldo(getSaldo() + intereses);
        return intereses;
    }
    
    /**
     * Reinicia el contador de retiros diarios.
     * Este método debería llamarse al inicio de cada día.
     */
    public void reiniciarLimiteDiario() {
        this.retiradoHoy = 0;
    }
    
    /**
     * Calcula cuánto puede retirar aún hoy.
     * 
     * @return Monto disponible para retirar en el día
     */
    public double getDisponibleHoy() {
        return limiteDiario - retiradoHoy;
    }
    
    @Override
    public String toCSV() {
        return "EMPRESARIAL," + super.toCSV() + "," + tipoEmpresa + "," + 
               registroTributario + "," + limiteDiario + "," + retiradoHoy;
    }
    
    // ==================== GETTERS Y SETTERS ====================
    
    public String getTipoEmpresa() {
        return tipoEmpresa;
    }
    
    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }
    
    public int getRegistroTributario() {
        return registroTributario;
    }
    
    public void setRegistroTributario(int registroTributario) {
        this.registroTributario = registroTributario;
    }
    
    public double getLimiteDiario() {
        return limiteDiario;
    }
    
    public void setLimiteDiario(double limiteDiario) {
        this.limiteDiario = limiteDiario;
    }
    
    public double getRetiradoHoy() {
        return retiradoHoy;
    }
    
    @Override
    public String toString() {
        return super.toString() + " | Tipo: EMPRESARIAL (" + tipoEmpresa + ")" +
               " | NIT: " + registroTributario + 
               " | Disponible hoy: $" + getDisponibleHoy() + "/" + limiteDiario;
    }
}
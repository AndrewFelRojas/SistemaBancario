package Controller;

import Model.*;
import Excepciones.*;
import java.util.ArrayList;

/**
 * Controlador que maneja la lógica de negocio del sistema bancario.
 * Gestiona las cuentas y coordina las operaciones con la persistencia.
 * 
 * @author Andres Camilo Vargas
 * @version 1.0
 */
public class ControladorBancario {
    
    private ArrayList<CuentaBancaria> cuentas;
    
    /**
     * Constructor que inicializa el controlador y el archivo de transacciones.
     */
    public ControladorBancario() {
        this.cuentas = new ArrayList<>();
        PersistenciaBancaria.inicializarArchivo();
    }
    
    /**
     * Registra una nueva cuenta en el sistema.
     * 
     * @param cuenta Cuenta a registrar
     * @return true si se registró exitosamente
     */
    public boolean registrarCuenta(CuentaBancaria cuenta) {
        // Verificar que no exista una cuenta con el mismo número
        if (buscarCuentaPorNumero(cuenta.getNumeroCuenta()) != null) {
            System.out.println("❌ Error: Ya existe una cuenta con ese número.");
            return false;
        }
        
        cuentas.add(cuenta);
        System.out.println("✓ Cuenta registrada exitosamente.");
        return true;
    }
    
    /**
     * Busca una cuenta por su número.
     * 
     * @param numeroCuenta Número de cuenta a buscar
     * @return La cuenta encontrada o null si no existe
     */
    public CuentaBancaria buscarCuentaPorNumero(int numeroCuenta) {
        for (CuentaBancaria cuenta : cuentas) {
            if (cuenta.getNumeroCuenta() == numeroCuenta) {
                return cuenta;
            }
        }
        return null;
    }
    
    /**
     * Realiza un depósito en una cuenta.
     * 
     * @param numeroCuenta Número de cuenta
     * @param monto Monto a depositar
     * @throws OperacionInvalidaException Si la operación es inválida
     */
    public void realizarDeposito(int numeroCuenta, double monto) 
            throws OperacionInvalidaException {
        
        CuentaBancaria cuenta = buscarCuentaPorNumero(numeroCuenta);
        
        if (cuenta == null) {
            throw new OperacionInvalidaException("Cuenta no encontrada: " + numeroCuenta);
        }
        
        // POLIMORFISMO: Llamar al método depositar() de la cuenta
        // Cada tipo de cuenta tiene su propia implementación
        cuenta.depositar(monto);
        
        // Guardar transacción
        PersistenciaBancaria.guardarTransaccion(
            numeroCuenta, "DEPOSITO", monto, cuenta.getSaldo()
        );
        
        System.out.println("✓ Depósito realizado. Nuevo saldo: $" + String.format("%.2f", cuenta.getSaldo()));
    }
    
    /**
     * Realiza un retiro de una cuenta.
     * 
     * @param numeroCuenta Número de cuenta
     * @param monto Monto a retirar
     * @throws SaldoInsuficienteException Si no hay saldo suficiente
     * @throws OperacionInvalidaException Si la operación es inválida
     */
    public void realizarRetiro(int numeroCuenta, double monto) 
            throws SaldoInsuficienteException, OperacionInvalidaException {
        
        CuentaBancaria cuenta = buscarCuentaPorNumero(numeroCuenta);
        
        if (cuenta == null) {
            throw new OperacionInvalidaException("Cuenta no encontrada: " + numeroCuenta);
        }
        
        // POLIMORFISMO: Llamar al método retirar() de la cuenta
        // Cada tipo de cuenta tiene su propia implementación
        cuenta.retirar(monto);
        
        // Guardar transacción
        PersistenciaBancaria.guardarTransaccion(
            numeroCuenta, "RETIRO", monto, cuenta.getSaldo()
        );
        
        System.out.println("✓ Retiro realizado. Nuevo saldo: $" + cuenta.getSaldo());
    }
    
    /**
     * Calcula y aplica los intereses a una cuenta.
     * 
     * @param numeroCuenta Número de cuenta
     * @throws OperacionInvalidaException Si la cuenta no existe
     */
    public void calcularIntereses(int numeroCuenta) throws OperacionInvalidaException {
        CuentaBancaria cuenta = buscarCuentaPorNumero(numeroCuenta);
        
        if (cuenta == null) {
            throw new OperacionInvalidaException("Cuenta no encontrada: " + numeroCuenta);
        }
        
        // POLIMORFISMO: Cada cuenta calcula sus intereses de forma diferente
        double intereses = cuenta.calcularIntereses();
        
        if (intereses > 0) {
            // Guardar transacción
            PersistenciaBancaria.guardarTransaccion(
                numeroCuenta, "INTERESES", intereses, cuenta.getSaldo()
            );
            
            System.out.println("✓ Intereses calculados: $" + String.format("%.2f", intereses));
            System.out.println("✓ Nuevo saldo: $" + cuenta.getSaldo());
        } else {
            System.out.println("ℹ Esta cuenta no genera intereses.");
        }
    }
    
    /**
     * Muestra información de una cuenta específica.
     * 
     * @param numeroCuenta Número de cuenta
     */
    public void consultarCuenta(int numeroCuenta) {
        CuentaBancaria cuenta = buscarCuentaPorNumero(numeroCuenta);
        
        if (cuenta == null) {
            System.out.println("❌ Cuenta no encontrada.");
            return;
        }
        
        System.out.println("\n========== INFORMACIÓN DE LA CUENTA ==========");
        System.out.println(cuenta.toString());
        System.out.println("=============================================\n");
    }
    
    /**
     * Muestra todas las cuentas registradas en el sistema.
     */
    public void listarTodasCuentas() {
        if (cuentas.isEmpty()) {
            System.out.println("No hay cuentas registradas.");
            return;
        }
        
        System.out.println("\n========== LISTADO DE CUENTAS ==========");
        for (CuentaBancaria cuenta : cuentas) {
            System.out.println(cuenta.toString());
        }
        System.out.println("========================================\n");
    }
    
    /**
     * Consulta el historial de transacciones de una cuenta.
     * 
     * @param numeroCuenta Número de cuenta
     */
    public void consultarHistorial(int numeroCuenta) {
        CuentaBancaria cuenta = buscarCuentaPorNumero(numeroCuenta);
        
        if (cuenta == null) {
            System.out.println("❌ Cuenta no encontrada.");
            return;
        }
        
        PersistenciaBancaria.consultarTransacciones(numeroCuenta);
    }
    
    /**
     * Obtiene el número de cuentas registradas.
     * 
     * @return Cantidad de cuentas
     */
    public int getCantidadCuentas() {
        return cuentas.size();
    }
    
    /**
     * Exporta todas las cuentas a formato CSV.
     * 
     * @return String con todas las cuentas en formato CSV
     */
    public String exportarCuentasCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append("TipoCuenta,Titular,Saldo,NumeroCuenta,DatosAdicionales\n");
        
        for (CuentaBancaria cuenta : cuentas) {
            sb.append(cuenta.toCSV()).append("\n");
        }
        
        return sb.toString();
    }
}
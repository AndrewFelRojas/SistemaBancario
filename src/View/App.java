package View;

import Controller.ControladorBancario;
import Model.*;
import Excepciones.*;
import java.util.Scanner;

/**
 * Clase principal que maneja la interfaz de usuario del sistema bancario.
 * Implementa un menú interactivo para gestionar cuentas y transacciones.
 * 
 * @author Andres
 * @version 1.0
 */
public class App {
    
    private static ControladorBancario controlador = new ControladorBancario();
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Método principal que inicia la aplicación.
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   	 	SISTEMA BANCARIO             ║");
        System.out.println("║  									     ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenu();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1:
                    registrarCuentaAhorros();
                    break;
                case 2:
                    registrarCuentaCorriente();
                    break;
                case 3:
                    registrarCuentaEmpresarial();
                    break;
                case 4:
                    realizarDeposito();
                    break;
                case 5:
                    realizarRetiro();
                    break;
                case 6:
                    calcularIntereses();
                    break;
                case 7:
                    consultarCuenta();
                    break;
                case 8:
                    consultarHistorial();
                    break;
                case 9:
                    controlador.listarTodasCuentas();
                    break;
                case 10:
                    PersistenciaBancaria.mostrarTodasTransacciones();
                    break;
                case 0:
                    continuar = false;
                    System.out.println("\n✓ Gracias por usar el Sistema Bancario. ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("❌ Opción inválida. Intente nuevamente.\n");
            }
        }
        
        scanner.close();
    }
    
    /**
     * Muestra el menú principal de opciones.
     */
    private static void mostrarMenu() {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│           MENÚ PRINCIPAL               │");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. Registrar Cuenta de Ahorros         │");
        System.out.println("│ 2. Registrar Cuenta Corriente          │");
        System.out.println("│ 3. Registrar Cuenta Empresarial        │");
        System.out.println("│ 4. Realizar Depósito                   │");
        System.out.println("│ 5. Realizar Retiro                     │");
        System.out.println("│ 6. Calcular Intereses                  │");
        System.out.println("│ 7. Consultar Cuenta                    │");
        System.out.println("│ 8. Ver Historial de Transacciones      │");
        System.out.println("│ 9. Listar Todas las Cuentas            │");
        System.out.println("│ 10. Ver Todas las Transacciones        │");
        System.out.println("│ 0. Salir                               │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Seleccione una opción: ");
    }
    
    /**
     * Lee una opción numérica del usuario.
     */
    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Registra una nueva Cuenta de Ahorros.
     */
    private static void registrarCuentaAhorros() {
        System.out.println("\n=== REGISTRO DE CUENTA DE AHORROS ===");
        
        try {
            System.out.print("Titular: ");
            String titular = scanner.nextLine();
            
            System.out.print("Saldo inicial: $");
            double saldo = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Número de cuenta: ");
            int numeroCuenta = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Tasa de interés (ej: 0.02 para 2%): ");
            double tasaInteres = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Periodicidad (Mensual/Trimestral): ");
            String periodicidad = scanner.nextLine();
            
            System.out.print("Retiros permitidos por período: ");
            int retirosPermitidos = Integer.parseInt(scanner.nextLine());
            
            CuentaAhorros cuenta = new CuentaAhorros(titular, saldo, numeroCuenta, 
                                                     tasaInteres, periodicidad, retirosPermitidos);
            
            controlador.registrarCuenta(cuenta);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese valores numéricos válidos.");
        }
        System.out.println();
    }
    
    /**
     * Registra una nueva Cuenta Corriente.
     */
    private static void registrarCuentaCorriente() {
        System.out.println("\n=== REGISTRO DE CUENTA CORRIENTE ===");
        
        try {
            System.out.print("Titular: ");
            String titular = scanner.nextLine();
            
            System.out.print("Saldo inicial: $");
            double saldo = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Número de cuenta: ");
            int numeroCuenta = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Comisión fija por retiro: $");
            double comision = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Límite de sobregiro: $");
            double limiteSobregiro = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Número de chequera: ");
            int numeroChequera = Integer.parseInt(scanner.nextLine());
            
            CuentaCorriente cuenta = new CuentaCorriente(titular, saldo, numeroCuenta, 
                                                         comision, limiteSobregiro, numeroChequera);
            
            controlador.registrarCuenta(cuenta);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese valores numéricos válidos.");
        }
        System.out.println();
    }
    
    /**
     * Registra una nueva Cuenta Empresarial.
     */
    private static void registrarCuentaEmpresarial() {
        System.out.println("\n=== REGISTRO DE CUENTA EMPRESARIAL ===");
        
        try {
            System.out.print("Razón social: ");
            String titular = scanner.nextLine();
            
            System.out.print("Saldo inicial: $");
            double saldo = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Número de cuenta: ");
            int numeroCuenta = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Tipo de empresa (S.A., S.A.S., Ltda.): ");
            String tipoEmpresa = scanner.nextLine();
            
            System.out.print("NIT/RUT: ");
            int registroTributario = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Límite de retiro diario: $");
            double limiteDiario = Double.parseDouble(scanner.nextLine());
            
            CuentaEmpresarial cuenta = new CuentaEmpresarial(titular, saldo, numeroCuenta, 
                                                             tipoEmpresa, registroTributario, limiteDiario);
            
            controlador.registrarCuenta(cuenta);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese valores numéricos válidos.");
        }
        System.out.println();
    }
    
    /**
     * Realiza un depósito en una cuenta.
     */
    private static void realizarDeposito() {
        System.out.println("\n=== REALIZAR DEPÓSITO ===");
        
        try {
            System.out.print("Número de cuenta: ");
            int numeroCuenta = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Monto a depositar: $");
            double monto = Double.parseDouble(scanner.nextLine());
            
            controlador.realizarDeposito(numeroCuenta, monto);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese valores numéricos válidos.");
        } catch (OperacionInvalidaException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Realiza un retiro de una cuenta.
     */
    private static void realizarRetiro() {
        System.out.println("\n=== REALIZAR RETIRO ===");
        
        try {
            System.out.print("Número de cuenta: ");
            int numeroCuenta = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Monto a retirar: $");
            double monto = Double.parseDouble(scanner.nextLine());
            
            controlador.realizarRetiro(numeroCuenta, monto);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese valores numéricos válidos.");
        } catch (SaldoInsuficienteException e) {
            System.out.println("❌ Saldo Insuficiente: " + e.getMessage());
            // Mostrar la causa si existe (excepción encadenada)
            if (e.getCause() != null) {
                System.out.println("   Causa: " + e.getCause().getMessage());
            }
        } catch (OperacionInvalidaException e) {
            System.out.println("❌ Operación Inválida: " + e.getMessage());
            // Mostrar la causa si existe (excepción encadenada)
            if (e.getCause() != null) {
                System.out.println("   Causa: " + e.getCause().getMessage());
            }
        }
        System.out.println();
    }
    
    /**
     * Calcula los intereses de una cuenta.
     */
    private static void calcularIntereses() {
        System.out.println("\n=== CALCULAR INTERESES ===");
        
        try {
            System.out.print("Número de cuenta: ");
            int numeroCuenta = Integer.parseInt(scanner.nextLine());
            
            controlador.calcularIntereses(numeroCuenta);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese un número de cuenta válido.");
        } catch (OperacionInvalidaException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Consulta información de una cuenta.
     */
    private static void consultarCuenta() {
        System.out.println("\n=== CONSULTAR CUENTA ===");
        
        try {
            System.out.print("Número de cuenta: ");
            int numeroCuenta = Integer.parseInt(scanner.nextLine());
            
            controlador.consultarCuenta(numeroCuenta);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese un número de cuenta válido.");
        }
    }
    
    /**
     * Consulta el historial de transacciones de una cuenta.
     */
    private static void consultarHistorial() {
        System.out.println("\n=== HISTORIAL DE TRANSACCIONES ===");
        
        try {
            System.out.print("Número de cuenta: ");
            int numeroCuenta = Integer.parseInt(scanner.nextLine());
            
            controlador.consultarHistorial(numeroCuenta);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese un número de cuenta válido.");
        }
    }
}
package Model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que maneja la persistencia de transacciones en archivo CSV.
 * Guarda los movimientos (depósitos y retiros) en el archivo Transacciones.txt
 * 
 * @author Andres
 * @version 1.0
 */
public class PersistenciaBancaria {
    
    // Ruta del archivo (en la raíz del proyecto, al mismo nivel que src)
    private static final String ARCHIVO_TRANSACCIONES = "Transacciones.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Guarda una transacción en el archivo CSV.
     * Formato legible: fecha-hora | cuenta | tipo | monto | saldo
     * 
     * @param numeroCuenta Número de cuenta que realizó la transacción
     * @param tipoTransaccion "DEPOSITO", "RETIRO", "INTERESES"
     * @param monto Monto de la transacción
     * @param saldoResultante Saldo después de la transacción
     * @return true si se guardó exitosamente, false en caso contrario
     */
    public static boolean guardarTransaccion(int numeroCuenta, String tipoTransaccion, 
                                             double monto, double saldoResultante) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_TRANSACCIONES, true))) {
            
            // Obtener fecha y hora actual
            String fechaHora = LocalDateTime.now().format(formatter);
            
            // Crear línea con formato legible
            String linea = String.format("%s | Cuenta: %d | %s | Monto: $%.2f | Saldo Final: $%.2f%n", 
                                        fechaHora, 
                                        numeroCuenta, 
                                        tipoTransaccion, 
                                        monto, 
                                        saldoResultante);
            
            // Escribir en el archivo
            writer.write(linea);
            
            return true;
            
        } catch (IOException e) {
            System.err.println("Error al guardar transacción: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Crea el archivo de transacciones con encabezados si no existe.
     */
    public static void inicializarArchivo() {
        File archivo = new File(ARCHIVO_TRANSACCIONES);
        
        // Si el archivo no existe, crearlo con encabezados
        if (!archivo.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                writer.write("====================================================================\n");
                writer.write("        REGISTRO DE TRANSACCIONES - SISTEMA BANCARIO\n");
                writer.write("====================================================================\n");
                writer.write("Formato: Fecha-Hora | Número Cuenta | Tipo | Monto | Saldo Final\n");
                writer.write("====================================================================\n\n");
                System.out.println("✓ Archivo de transacciones creado exitosamente.");
            } catch (IOException e) {
                System.err.println("Error al crear archivo: " + e.getMessage());
            }
        }
    }
    
    /**
     * Lee y muestra todas las transacciones de una cuenta específica.
     * 
     * @param numeroCuenta Número de cuenta a consultar
     */
    public static void consultarTransacciones(int numeroCuenta) {
        File archivo = new File(ARCHIVO_TRANSACCIONES);
        
        if (!archivo.exists()) {
            System.out.println("No hay transacciones registradas.");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean encontrado = false;
            
            System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
            System.out.println("║        HISTORIAL DE TRANSACCIONES - CUENTA #" + numeroCuenta + "              ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════╝");
            
            // Leer línea por línea
            while ((linea = reader.readLine()) != null) {
                // Saltar líneas de encabezado o decorativas
                if (linea.startsWith("=") || linea.contains("REGISTRO DE") || 
                    linea.contains("Formato:") || linea.trim().isEmpty()) {
                    continue;
                }
                
                // Verificar si la línea contiene el número de cuenta
                if (linea.contains("Cuenta: " + numeroCuenta)) {
                    encontrado = true;
                    System.out.println(linea);
                }
            }
            
            if (!encontrado) {
                System.out.println("No se encontraron transacciones para esta cuenta.");
            }
            
            System.out.println("════════════════════════════════════════════════════════════════════\n");
            
        } catch (IOException e) {
            System.err.println("Error al leer transacciones: " + e.getMessage());
        }
    }
    
    /**
     * Muestra todas las transacciones del sistema.
     */
    public static void mostrarTodasTransacciones() {
        File archivo = new File(ARCHIVO_TRANSACCIONES);
        
        if (!archivo.exists()) {
            System.out.println("No hay transacciones registradas.");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            
            System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
            System.out.println("║           TODAS LAS TRANSACCIONES DEL SISTEMA                     ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════╝\n");
            
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
            
            System.out.println("\n════════════════════════════════════════════════════════════════════\n");
            
        } catch (IOException e) {
            System.err.println("Error al leer transacciones: " + e.getMessage());
        }
    }
    
    /**
     * Elimina todas las transacciones del archivo (reinicia el sistema).
     * PRECAUCIÓN: Esta operación no se puede deshacer.
     * 
     * @return true si se eliminó exitosamente
     */
    public static boolean limpiarTransacciones() {
        File archivo = new File(ARCHIVO_TRANSACCIONES);
        
        if (archivo.exists()) {
            if (archivo.delete()) {
                inicializarArchivo(); // Recrear con encabezados
                return true;
            }
        }
        return false;
    }
}
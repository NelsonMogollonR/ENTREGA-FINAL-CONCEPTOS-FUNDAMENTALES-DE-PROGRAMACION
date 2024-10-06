package EntregaSemana7;



import java.io.BufferedReader;
import java.io.File; // Importa la clase File para manejar archivos y directorios
import java.io.FileReader;
import java.io.FileWriter; // Importa la clase FileWriter para escribir en archivos
import java.io.IOException; // Importa la clase IOException para manejar excepciones de E/S
import java.util.Arrays; // Importa la clase Arrays para trabajar con arrays
import java.util.HashMap; // Importa la clase HashMap para trabajar con mapas
import java.util.List; // Importa la clase List para trabajar con listas
import java.util.Map; // Importa la clase Map para trabajar con mapas

public class GenerateInfoFiles {

    public static void main(String[] args) {
        try {
            // Crear la carpeta 'src/files' si no existe
            File directory = new File("src/files");
            if (!directory.exists()) {
                directory.mkdir(); // Crea el directorio si no existe
            }
            // Generar archivos de información
            generateSalesmenInfoFile(); // Genera el archivo con información de los vendedores
            generateProductsInfoFile(); // Genera el archivo con información de los productos
            generateSalesInfoFile();    // Genera los archivos con información de las ventas
            generateConsolidatedSalesReport(); // Genera el reporte consolidado de ventas
            generateGeneralInfoFile(); // Genera el archivo con información general
            System.out.println("Archivos generados exitosamente.\n"); // Mensaje de éxito
        } catch (IOException e) {
            System.err.println("Error al generar los archivos: " + e.getMessage()); // Mensaje de error
        }
    }

    /**
     * Genera un archivo CSV con información de los vendedores.
     * El archivo se guarda en 'src/files/salesmen_info.csv'.
     * 
     * @throws IOException si ocurre un error de E/S
     */
    private static void generateSalesmenInfoFile() throws IOException {
        try (FileWriter writer = new FileWriter("src/files/salesmen_info.csv")) {
            writer.write("TipoDocumento,NumeroDocumento,Nombres,Apellidos\n"); // Escribe la cabecera del CSV
            writer.write("CC,123456789,Juan,Perez\n"); // Escribe la información de un vendedor
            writer.write("CC,987654321,Maria,Gomez\n"); // Escribe la información de un vendedor
            writer.write("CC,456789123,Carlos,Lopez\n"); // Escribe la información de un vendedor
            writer.write("CC,321654987,Ana,Martinez\n"); // Escribe la información de un vendedor
            writer.write("CC,654987321,Luis,Garcia\n"); // Escribe la información de un vendedor
            writer.write("CC,789123456,Sofia,Rodriguez\n"); // Escribe la información de un vendedor
        }
    }

    /**
     * Genera archivos CSV con información de las ventas por vendedor.
     * Cada archivo se guarda en 'src/files/sales_<NumeroDocumento>.csv'.
     * 
     * @throws IOException si ocurre un error de E/S
     */
    private static void generateSalesInfoFile() throws IOException {
        // Simulación de datos de ventas
        Map<String, List<String[]>> salesData = new HashMap<>(); // Crea un mapa para almacenar los datos de ventas
        salesData.put("123456789", Arrays.asList(
            new String[]{"CC", "123456789", "P001", "10"}, // Datos de una venta
            new String[]{"CC", "123456789", "P002", "5"} // Datos de otra venta
        ));
        salesData.put("987654321", Arrays.asList(
            new String[]{"CC", "987654321", "P003", "7"}, // Datos de una venta
            new String[]{"CC", "987654321", "P004", "3"} // Datos de otra venta
        ));
        salesData.put("456789123", Arrays.asList(
            new String[]{"CC", "456789123", "P005", "8"}, // Datos de una venta
            new String[]{"CC", "456789123", "P006", "2"} // Datos de otra venta
        ));
        salesData.put("321654987", Arrays.asList(
            new String[]{"CC", "321654987", "P001", "6"}, // Datos de una venta
            new String[]{"CC", "321654987", "P002", "4"} // Datos de otra venta
        ));
        salesData.put("654987321", Arrays.asList(
            new String[]{"CC", "654987321", "P003", "9"}, // Datos de una venta
            new String[]{"CC", "654987321", "P004", "1"} // Datos de otra venta
        ));
        salesData.put("789123456", Arrays.asList(
            new String[]{"CC", "789123456", "P005", "12"}, // Datos de una venta
            new String[]{"CC", "789123456", "P006", "5"} // Datos de otra venta
        ));

        // Generar archivos CSV para cada vendedor
        for (Map.Entry<String, List<String[]>> entry : salesData.entrySet()) {
            String salesmenId = entry.getKey(); // Obtiene el ID del vendedor
            List<String[]> sales = entry.getValue(); // Obtiene la lista de ventas del vendedor
            try (FileWriter writer = new FileWriter("src/files/sales_" + salesmenId + ".csv")) {
                writer.write("TipoDocumento,NumeroDocumento,IDProducto,CantidadVendida\n"); // Escribe la cabecera del CSV
                for (String[] sale : sales) {
                    writer.write(String.join(",", sale) + "\n"); // Escribe los datos de cada venta
                }
            }
        }
    }

    /**
     * Genera un archivo CSV con información de los productos.
     * El archivo se guarda en 'src/files/products_info.csv'.
     * 
     * @throws IOException si ocurre un error de E/S
     */
    private static void generateProductsInfoFile() throws IOException {
        try (FileWriter writer = new FileWriter("src/files/products_info.csv")) {
            writer.write("IDProducto,NombreProducto,PrecioPorUnidad\n"); // Escribe la cabecera del CSV
            writer.write("P001,Producto1,1000\n"); // Escribe la información de un producto
            writer.write("P002,Producto2,2000\n"); // Escribe la información de un producto
            writer.write("P003,Producto3,3000\n"); // Escribe la información de un producto
            writer.write("P004,Producto4,4000\n"); // Escribe la información de un producto
            writer.write("P005,Producto5,5000\n"); // Escribe la información de un producto
            writer.write("P006,Producto6,6000\n"); // Escribe la información de un producto
        }
    }

    private static void generateConsolidatedSalesReport() throws IOException {
        // Crear un mapa para almacenar las ventas consolidadas
        Map<String, Integer> consolidatedSales = new HashMap<>();
        // Agregar datos de ventas consolidadas al mapa
        consolidatedSales.put("P001", 50);
        consolidatedSales.put("P002", 30);
        consolidatedSales.put("P003", 20);
        consolidatedSales.put("P004", 40);
        consolidatedSales.put("P005", 60);
        consolidatedSales.put("P006", 10);

        // Crear un escritor de archivos para el reporte consolidado de ventas
        try (FileWriter writer = new FileWriter("src/files/consolidated_sales_report.csv")) {
            writer.write("IDProducto,CantidadTotalVendida\n"); // Escribir la cabecera del CSV
            // Escribir cada entrada del mapa en el archivo CSV
            for (Map.Entry<String, Integer> entry : consolidatedSales.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        }
    }

    // Método para generar un archivo con información general
    private static void generateGeneralInfoFile() throws IOException {
        // Crear un mapa para almacenar la información de los vendedores
        Map<String, String[]> salesmenMap = new HashMap<>();
        // Crear un mapa para almacenar los precios de los productos
        Map<String, Integer> productPrices = new HashMap<>();

        // Leer el archivo de información de los vendedores
        try (BufferedReader salesmenReader = new BufferedReader(new FileReader("src/files/salesmen_info.csv"))) {
            salesmenReader.readLine(); // Saltar la cabecera
            String line;
            // Leer cada línea del archivo y agregarla al mapa de vendedores
            while ((line = salesmenReader.readLine()) != null) {
                String[] parts = line.split(","); // Dividir la línea en partes
                salesmenMap.put(parts[1], parts); // Agregar la información al mapa
            }
        }

        // Leer el archivo de información de los productos
        try (BufferedReader productsReader = new BufferedReader(new FileReader("src/files/products_info.csv"))) {
            productsReader.readLine(); // Saltar la cabecera
            String line;
            // Leer cada línea del archivo y agregarla al mapa de precios de productos
            while ((line = productsReader.readLine()) != null) {
                String[] parts = line.split(","); // Dividir la línea en partes
                productPrices.put(parts[0], Integer.parseInt(parts[2])); // Agregar la información al mapa
            }
        }
    }
}


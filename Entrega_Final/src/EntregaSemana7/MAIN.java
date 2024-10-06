package EntregaSemana7;

import java.io.*;
import java.util.*;

import javax.swing.*;

public class MAIN {

    private static final String FILE_PATH = "src/files/";
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in); // Crear un objeto Scanner para leer la entrada del usuario
        while (true) { // Bucle infinito para mostrar el menú hasta que el usuario decida salir
          
            System.out.println("---- PRINCIPAL----\n");
            System.out.println("1. Generate Info Files");
            System.out.println("2. Generate Seller Report");
            System.out.println("3. Generate Total Sales Report");
            System.out.println("4. Delete file");
            System.out.println("5. Generate Test file");
            System.out.println("6. Exit!!!");
            System.out.print("\nSeleccione una opción:\n");// Mostrar el menú de opciones
            int option = scanner.nextInt(); // Leer la opción seleccionada por el usuario
            scanner.nextLine(); // Consumir la nueva línea

            switch (option) { // Evaluar la opción seleccionada
                case 1:
                    GenerateInfoFiles.main(new String[]{}); // Llamar al método main de GenerateInfoFiles para generar archivos de información
                    break;
                case 2:
                    try {
                        procesSellerReport(); // Llamar al método para generar el reporte de ventas por vendedor
                        System.out.println("\nReporte de ventas generado exitosamente."); // Mensaje de éxito
                    } catch (IOException e) {
                        System.err.println("\n !!!! Error al generar el reporte de ventas:\\n " + e.getMessage()); // Mensaje de error
                    }
                    break;
                case 3:
                    try {
                        procesSalesTotalReport(); // Llamar al método para generar el reporte de ventas totales
                        System.out.println("Reporte de ventas totales generado exitosamente."); // Mensaje de éxito
                    } catch (IOException e) {
                        System.err.println("\n !!!! Error al generar el reporte de ventas totales: " + e.getMessage()); // Mensaje de error
                    }
                    break;
                case 4:
                		fileDelete();
                    break;
                case 5:
				ejecutarPrueba(scanner);
                    break;
                case 6:
                    System.out.println("Salida exitosa"); // Mensaje de salida
                    scanner.close(); // Cerrar el objeto Scanner
                    return; // Salir del programa
                default:
                    System.out.println("!!!!!!!!! Opción no válida. Intente de nuevo.\n"); // Mensaje de opción no válida
            }
        }
    }

    // Método para generar el reporte de ventas por vendedor
    private static void procesSellerReport() throws IOException {
        Map<String, String> salesmenInfo = readSalesmenInfo(); // Leer la información de los vendedores
        Map<String, Integer> productPrices = readProductPrices(); // Leer los precios de los productos
        Map<String, Integer> salesBySalesman = calculateSalesBySalesman(productPrices); // Calcular las ventas por vendedor
        List<Map.Entry<String, Integer>> sortedSalesmen = sortSalesmenBySales(salesBySalesman); // Ordenar los vendedores por ventas
        generateSalesReport(salesmenInfo, sortedSalesmen); // Generar el reporte de ventas
    }

    // Método para generar el reporte de ventas totales
    private static void procesSalesTotalReport() throws IOException {
        Map<String, Integer> productSales = calculateProductSales(); // Calcular las ventas por producto
        List<Map.Entry<String, Integer>> sortedProducts = sortProductsBySales(productSales); // Ordenar los productos por ventas
        generateTotalSalesReport(sortedProducts); // Generar el reporte de ventas totales
    }

    // Método para leer la información de los vendedores desde un archivo CSV
    private static Map<String, String> readSalesmenInfo() throws IOException {
        Map<String, String> salesmenInfo = new HashMap<>(); // Crear un mapa para almacenar la información de los vendedores
        try (BufferedReader reader = new BufferedReader(new FileReader("src/files/salesmen_info.csv"))) {
            String line;
            reader.readLine(); // Saltar la cabecera
            while ((line = reader.readLine()) != null) { // Leer cada línea del archivo
                String[] parts = line.split(","); // Dividir la línea en partes
                salesmenInfo.put(parts[1], parts[2] + " " + parts[3]); // Agregar la información al mapa
            }
        }
        return salesmenInfo; // Devolver el mapa con la información de los vendedores
    }

    // Método para leer los precios de los productos desde un archivo CSV
    private static Map<String, Integer> readProductPrices() throws IOException {
        Map<String, Integer> productPrices = new HashMap<>(); // Crear un mapa para almacenar los precios de los productos
        try (BufferedReader reader = new BufferedReader(new FileReader("src/files/products_info.csv"))) {
            String line;
            reader.readLine(); // Saltar la cabecera
            while ((line = reader.readLine()) != null) { // Leer cada línea del archivo
                String[] parts = line.split(","); // Dividir la línea en partes
                productPrices.put(parts[0], Integer.parseInt(parts[2])); // Agregar la información al mapa
            }
        }
        return productPrices; // Devolver el mapa con los precios de los productos
    }

//-----------------------------------------------------------------------------------------
    private static Map<String, Integer> calculateSalesBySalesman(Map<String, Integer> productPrices) throws IOException {
        Map<String, Integer> salesBySalesman = new HashMap<>(); // Crear un mapa para almacenar las ventas por vendedor
        File directory = new File(FILE_PATH); // Crear un objeto File para el directorio de archivos
        File[] files = directory.listFiles((dir, name) -> name.startsWith("sales_") && name.endsWith(".csv")); // Obtener la lista de archivos de ventas

        if (files != null) { // Verificar si hay archivos en el directorio
            for (File file : files) { // Iterar sobre cada archivo
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) { // Crear un BufferedReader para leer el archivo
                    String line;
                    reader.readLine(); // Saltar la cabecera
                    while ((line = reader.readLine()) != null) { // Leer cada línea del archivo
                        String[] parts = line.split(","); // Dividir la línea en partes
                        String salesmanId = parts[1]; // Obtener el ID del vendedor
                        String productId = parts[2]; // Obtener el ID del producto
                        int quantity = Integer.parseInt(parts[3]); // Obtener la cantidad vendida
                        int price = productPrices.get(productId); // Obtener el precio del producto
                        // Calcular el total vendido por el vendedor y actualizar el mapa
                        salesBySalesman.put(salesmanId, salesBySalesman.getOrDefault(salesmanId, 0) + (price * quantity));
                    }
                }
            }
        }
        return salesBySalesman; // Devolver el mapa con las ventas por vendedor
    }

    // Método para ordenar los vendedores por ventas
    private static List<Map.Entry<String, Integer>> sortSalesmenBySales(Map<String, Integer> salesBySalesman) {
        List<Map.Entry<String, Integer>> sortedSalesmen = new ArrayList<>(salesBySalesman.entrySet()); // Crear una lista a partir de las entradas del mapa
        sortedSalesmen.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Ordenar la lista en orden descendente por valor
        return sortedSalesmen; // Devolver la lista ordenada
    }

    // Método para generar el reporte de ventas por vendedor
    private static void generateSalesReport(Map<String, String> salesmenInfo, List<Map.Entry<String, Integer>> sortedSalesmen) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH + "sales_report.csv"))) { // Crear un BufferedWriter para escribir el archivo
            writer.write("NombreVendedor;CantidadRecaudada\n"); // Escribir la cabecera del CSV
            for (Map.Entry<String, Integer> entry : sortedSalesmen) { // Iterar sobre cada entrada de la lista ordenada
                String salesmanName = salesmenInfo.get(entry.getKey()); // Obtener el nombre del vendedor
                int totalSales = entry.getValue(); // Obtener el total vendido
                writer.write(salesmanName + ";" + totalSales + "\n"); // Escribir la información en el archivo
            }
        }
    }

    // Método para calcular las ventas por producto
    private static Map<String, Integer> calculateProductSales() throws IOException {
        Map<String, Integer> productSales = new HashMap<>(); // Crear un mapa para almacenar las ventas por producto
        File directory = new File(FILE_PATH); // Crear un objeto File para el directorio de archivos
        File[] files = directory.listFiles((dir, name) -> name.startsWith("sales_") && name.endsWith(".csv")); // Obtener la lista de archivos de ventas

        if (files != null) { // Verificar si hay archivos en el directorio
            for (File file : files) { // Iterar sobre cada archivo
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) { // Crear un BufferedReader para leer el archivo
                    String line;
                    reader.readLine(); // Saltar la cabecera
                    while ((line = reader.readLine()) != null) { // Leer cada línea del archivo
                        String[] parts = line.split(","); // Dividir la línea en partes
                        if (parts.length >= 4) { // Asegurarse de que hay suficientes partes
                            String productId = parts[2]; // Obtener el ID del producto
                            int quantity = Integer.parseInt(parts[3]); // Obtener la cantidad vendida
                            // Calcular el total vendido por producto y actualizar el mapa
                            productSales.put(productId, productSales.getOrDefault(productId, 0) + quantity);
                        }
                    }
                }
            }
        }
        return productSales; // Devolver el mapa con las ventas por producto
    }

    // Método para ordenar los productos por ventas
    private static List<Map.Entry<String, Integer>> sortProductsBySales(Map<String, Integer> productSales) {
        List<Map.Entry<String, Integer>> sortedProducts = new ArrayList<>(productSales.entrySet()); // Crear una lista a partir de las entradas del mapa
        sortedProducts.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Ordenar la lista en orden descendente por valor
        return sortedProducts; // Devolver la lista ordenada
    }
//-----------------------------------------------------------------------------------
    private static void generateTotalSalesReport(List<Map.Entry<String, Integer>> sortedProducts) throws IOException {
        Map<String, String> productInfo = readProductInfo(); // Leer la información de los productos
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH + "total_sales_report.csv"))) { // Crear un BufferedWriter para escribir el archivo
            writer.write("NombreProducto;PrecioPorUnidad;CantidadVendida\n"); // Escribir la cabecera del CSV
            for (Map.Entry<String, Integer> entry : sortedProducts) { // Iterar sobre cada entrada de la lista ordenada de productos
                String productId = entry.getKey(); // Obtener el ID del producto
                String productName = productInfo.get(productId).split(";")[0]; // Obtener el nombre del producto
                String productPrice = productInfo.get(productId).split(";")[1]; // Obtener el precio del producto
                int totalQuantity = entry.getValue(); // Obtener la cantidad total vendida
                writer.write(productName + ";" + productPrice + ";" + totalQuantity + "\n"); // Escribir la información en el archivo
            }
        }
    }

    // Método para leer la información de los productos desde un archivo CSV
    private static Map<String, String> readProductInfo() throws IOException {
        Map<String, String> productInfo = new HashMap<>(); // Crear un mapa para almacenar la información de los productos
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH + "products_info.csv"))) { // Crear un BufferedReader para leer el archivo
            String line;
            reader.readLine(); // Saltar la cabecera
            while ((line = reader.readLine()) != null) { // Leer cada línea del archivo
                String[] parts = line.split(","); // Dividir la línea en partes
                productInfo.put(parts[0], parts[1] + ";" + parts[2]); // Agregar la información al mapa
            }
        }
        return productInfo; // Devolver el mapa con la información de los productos
    }


    
  
 // -----------------Método para eliminar un archivo seleccionado por el usuario-----------------
    private static void fileDelete() {
        Scanner scanner = new Scanner(System.in); // Crear un objeto Scanner para leer la entrada del usuario
        File directory = new File("src/files/"); // Crear un objeto File para el directorio 'src'

        while (true) {
            File[] files = directory.listFiles(); // Obtener la lista de archivos en el directorio

            if (files != null && files.length > 0) { // Verificar si hay archivos en el directorio
                System.out.println("Archivos disponibles para eliminar:");
                for (int i = 0; i < files.length; i++) { // Iterar sobre cada archivo
                    System.out.println((i + 1) + ". " + files[i].getName()); // Mostrar el nombre del archivo
                }

                System.out.print("Seleccione el número del archivo que desea eliminar (o 0 para salir): ");
                int fileIndex = scanner.nextInt() - 1; // Leer la opción seleccionada por el usuario
                
                
                
                clearScreen();
                if (fileIndex == -1) { // Si el usuario selecciona 0, salir del bucle
                    break;
                }

                if (fileIndex >= 0 && fileIndex < files.length) { // Verificar si la opción es válida
                    File fileToDelete = files[fileIndex]; // Obtener el archivo seleccionado
                    System.out.print("¿Está seguro de que desea eliminar el archivo " + fileToDelete.getName() + "? (s/n): ");
                    String confirmation = scanner.next(); // Leer la confirmación del usuario

                    if (confirmation.equalsIgnoreCase("s")) { // Verificar si el usuario confirmó la eliminación
                        if (fileToDelete.delete()) { // Intentar eliminar el archivo
                            System.out.println("Archivo eliminado exitosamente: " + fileToDelete.getName());
                            clearScreen();
                        } else {
                            System.out.println("Error al eliminar el archivo: " + fileToDelete.getName());
                        }
                    } else {
                        System.out.println("Eliminación cancelada.");
                    }
                } else {
                    System.out.println("Opción no válida.");
                }
            } else {
                System.out.println("No hay archivos disponibles para eliminar en el directorio 'src'.");
                break; // Salir del bucle si no hay archivos
            }
        }
    }
    //-------------------
    private static void ejecutarPrueba(Scanner scanner) throws IOException {
        System.out.println("-------Menu De Pruebas------");
        System.out.println("1. createTestSalesMenFile");
        System.out.println("2. createTestProductsCount");
        System.out.println("3. createSalesManInfoFile");
        System.out.println("4. Return to Principal ");
        System.out.println("Seleccione la prueba a ejecutar:=> ");
        int testOption = scanner.nextInt();

        switch (testOption) {
            case 1:
                int randomSalesCount = 0;
                while (true) {
                    System.out.println("Ingrese la cantidad de ventas:");
                    try {
                        randomSalesCount = scanner.nextInt();
                        if (randomSalesCount <= 0) {
                            System.err.println("La cantidad de ventas debe ser un número positivo.");
                            continue;
                        }
                        break;
                    } catch (InputMismatchException e) {
                        System.err.println("La cantidad de ventas debe ser un número.");
                        scanner.next(); // Consumir la entrada inválida
                    }
                }
                scanner.nextLine(); // Consumir la nueva línea

                String name;
                while (true) {
                    System.out.println("Ingrese el nombre del vendedor:");
                    name = scanner.nextLine();
                    if (!name.matches("[a-zA-Z]+")) {
                        System.err.println("El nombre solo debe contener letras.");
                    } else {
                        break;
                    }
                }

                String lastName;
                while (true) {
                    System.out.println("Ingrese el apellido del vendedor:");
                    lastName = scanner.nextLine();
                    if (!lastName.matches("[a-zA-Z]+")) {
                        System.err.println("El apellido solo debe contener letras.");
                    } else {
                        break;
                    }
                }

                long id = 0;
                while (true) {
                    System.out.println("Ingrese el ID del vendedor:");
                    try {
                        id = scanner.nextLong();
                        break;
                    } catch (InputMismatchException e) {
                        System.err.println("El ID debe ser un número.");
                        scanner.next(); // Consumir la entrada inválida
                    }
                }

                createTestSalesMenFile(randomSalesCount, name, lastName, id);
                break;
            case 2:
                System.out.println("Ingrese la cantidad de productos:");
                int productsCount = scanner.nextInt();
                createTestProductsCount(productsCount);
                break;
            case 3:                           
                
                Scanner scanner1 = new Scanner(System.in);
                int salesmanCount1 = 0;

                // Validar que el dato ingresado sea numérico y no negativo
                while (true) {
                    System.out.println("Ingrese la cantidad de vendedores:");
                    try {
                        salesmanCount1 = scanner1.nextInt();
                        if (salesmanCount1 <= 0) {
                            System.err.println("La cantidad de vendedores debe ser un número positivo.");
                            continue;
                        }
                        break;
                    } catch (InputMismatchException e) {
                        System.err.println("La cantidad de vendedores debe ser un número.");
                        scanner1.next(); // Consumir la entrada inválida
                    }
                }

                try {
                    createSalesManInfoFile(salesmanCount1);
                } catch (IOException e) {
                    System.err.println("Error al crear el archivo de información de vendedores: " + e.getMessage());
                }
                createSalesManInfoFile(salesmanCount1);
                break;
            case 4:
                clearScreen(); // Limpiar la pantalla y regresar al menú anterior
                break;
            default:
                System.out.println("Opción no válida. Intente de nuevo.");
        }
    }

    private static void createTestSalesMenFile(int randomSalesCount, String name, String lastName, long id) throws IOException {
        // Implementación para crear un archivo pseudoaleatorio de ventas de un vendedor
        try (FileWriter writer = new FileWriter("src/files/TestSalesMenFile.csv")) {
            // Escribir el nombre y apellido del vendedor en el archivo
            writer.write("Nombre: " + name + " " + lastName + "\n");
            // Escribir el ID del vendedor en el archivo
            writer.write("ID: " + id + "\n");
            // Escribir la cantidad de ventas en el archivo
            writer.write("Cantidad de ventas: " + randomSalesCount + "\n");

            // Generar datos de ventas pseudoaleatorios
            Random random = new Random();
            for (int i = 0; i < randomSalesCount; i++) {
                int productId = random.nextInt(1000) + 1; // ID de producto aleatorio
                double saleValue = random.nextDouble() * 100; // Valor de venta aleatorio
                writer.write("Venta " + (i + 1) + ": Producto ID " + productId + ", Valor " + String.format("%.2f", saleValue) + "\n");
            }
        }
        // Mostrar mensaje de éxito
        System.out.println("Archivo de ventas creado para " + name + " " + lastName + " con ID " + id + " y " + randomSalesCount + " ventas.");
    }

  private static void createTestProductsCount(int productsCount) throws IOException {
    // Implementación para crear un archivo con información pseudoaleatoria de productos
    try (FileWriter writer = new FileWriter("src/files/TestProductsCountFile.csv")) {
        // Escribir la cantidad de productos en el archivo
        writer.write("Cantidad de productos: " + productsCount + "\n");

        // Generar datos de productos pseudoaleatorios
        Random random = new Random();
        for (int i = 0; i < productsCount; i++) {
            int productId = random.nextInt(1000) + 1; // ID de producto aleatorio
            double productValue = random.nextDouble() * 100; // Valor unitario aleatorio
            writer.write("Producto " + (i + 1) + ": ID " + productId + ", Valor " + String.format("%.2f", productValue) + "\n");
        }
    }
    // Mostrar mensaje de éxito
    System.out.println("Archivo de productos creado con " + productsCount + " productos.");
}
//---------------------------------------------------------------------------------------
    
  private static void createSalesManInfoFile(int salesmanCount) throws IOException {
      // Implementación para crear un archivo con información pseudoaleatoria de vendedores
      try (FileWriter writer = new FileWriter("src/files/TestSalesManInfoFile.csv")) {
          // Escribir la cantidad de vendedores en el archivo
          writer.write("Cantidad de vendedores: " + salesmanCount + "\n");

          // Generar datos de vendedores pseudoaleatorios
          Random random = new Random();
          for (int i = 0; i < salesmanCount; i++) {
              long id = 1000000000L + random.nextInt(900000000); // ID de vendedor aleatorio
              String name = generateRandomName(random); // Nombre aleatorio
              String lastName = generateRandomLastName(random); // Apellido aleatorio
              writer.write("Vendedor " + (i + 1) + ": ID " + id + ", Nombre " + name + ", Apellido " + lastName + "\n");
          }
      }
      // Mostrar mensaje de éxito
      System.out.println("Archivo de información de vendedores creado con " + salesmanCount + " vendedores.");
  }

  private static String generateRandomName(Random random) {
      // Generar un nombre aleatorio
      String[] names = {"Juan", "Carlos", "Luis", "Pedro", "Jorge", "Miguel", "Andrés", "José", "Fernando", "Ricardo"};
      return names[random.nextInt(names.length)];
  }

  private static String generateRandomLastName(Random random) {
      // Generar un apellido aleatorio
      String[] lastNames = {"García", "Martínez", "Rodríguez", "López", "González", "Pérez", "Sánchez", "Ramírez", "Torres", "Flores"};
      return lastNames[random.nextInt(lastNames.length)];
  }

    
    
    
    
    //-------------createSalesManInfoFile-------------------------------------
  
 /*   private static void createSalesManInfoFile(int salesmanCount) throws IOException {
        // Implementación para crear un archivo con información pseudoaleatoria de vendedores
        try (FileWriter writer = new FileWriter("src/files/TestSalesManInfoFile.csv")) {
            // Escribir la cantidad de vendedores en el archivo
            writer.write("Cantidad de vendedores: " + salesmanCount + "\n");
            // Aquí puedes agregar más lógica para generar datos de vendedores pseudoaleatorios
        }
        // Mostrar mensaje de éxito
        System.out.println("Archivo de información de vendedores creado con " + salesmanCount + " vendedores.");
    }
*/
    //-----------------------------------------------------------------------

    private static void clearScreen() {
        // Método para limpiar la pantalla
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

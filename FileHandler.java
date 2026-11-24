import java.io.*;
import java.util.*;

public class FileHandler {

    private static final String FILE_NAME = "employees.txt";
    private static String resolvedPath = null;

    // Save employees to file
    public static void save(ArrayList<Employee> list) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(resolveFilePath()))) {
            for (Employee e : list) {
                pw.println(e.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    // Load employees from file
    public static ArrayList<Employee> load() {
        ArrayList<Employee> list = new ArrayList<>();
        File file = new File(resolveFilePath());

        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(Employee.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading file.");
        }

        return list;
    }

    // Allows servlets to specify an absolute storage location.
    public static synchronized void setStoragePath(String absolutePath) {
        if (absolutePath != null && !absolutePath.isBlank()) {
            resolvedPath = absolutePath;
        }
    }

    private static synchronized String resolveFilePath() {
        return (resolvedPath == null || resolvedPath.isBlank()) ? FILE_NAME : resolvedPath;
    }
}
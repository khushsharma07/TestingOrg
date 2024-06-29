import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.sql.SQLException;

import com.controller.AdminController;
import com.controller.ChefController;
import com.controller.Controller;
import com.controller.EmployeeController;
import com.factory.ControllerFactory;
import com.server.ServerConfiguration;

public class ClientCafeteria {

    private static final String HOST = "localhost";

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, ServerConfiguration.PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            login(out, in, stdIn);

        } catch (IOException | SQLException | ClassNotFoundException | InvocationTargetException |
                 NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void login(PrintWriter out, BufferedReader in, BufferedReader stdIn) throws IOException, SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        System.out.println("Enter your Employee ID:");
        String employeeId = stdIn.readLine();
        System.out.println("Enter your Password:");
        String password = stdIn.readLine();
        out.println("LOGIN_REQUEST;" + employeeId + ";" + password);

        String response = in.readLine();
        if (response != null) {
            String[] parts = response.split(";");
            String responseType = parts[0];

            if ("LOGIN_RESPONSE".equals(responseType)) {
                String status = parts[1];
                String message = parts[2];
                System.out.println(message);

                if ("SUCCESS".equals(status)) {
                    String role = parts[3];
                    displayRoleMenu(role, stdIn, out, in);
                } else {
                    System.out.println("Login failed");
                    System.exit(0);
                }
            }
        }
    }

    private static void displayRoleMenu(String role, BufferedReader stdIn, PrintWriter out, BufferedReader in) throws IOException, SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        Controller userController = ControllerFactory.getInstance(role);
        userController.displayMenu(stdIn, out, in);

//        switch (role) {
//            case "Admin":
//                AdminController.displayAdminMenu(stdIn, out, in);
//                break;
//            case "Chef":
//                ChefController.displayChefMenu(stdIn, out, in);
//                break;
//            case "Employee":
//                EmployeeController.displayEmployeeMenu(stdIn, out, in);
//                break;
//            default:
//                System.out.println("Unknown role");
//        }
    }
}
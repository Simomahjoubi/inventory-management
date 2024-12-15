package main.java.server;

import main.java.database.ProductDAO;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerLauncher {
    public static void main(String[] args) {
        try {
            ProductDAO dao = new ProductDAO("jdbc:mysql://localhost:3306/inventory_db", "root", "");
            InventoryRemote server = new InventoryServerImpl(dao);

            Registry registry = LocateRegistry.createRegistry(5555);
            registry.rebind("InventoryService", server);

            System.out.println("Serveur RMI démarré sur le port 5555");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package main.java.server;

import main.java.model.Product;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InventoryRemote extends Remote {
    boolean addProduct(Product product) throws RemoteException;
    List<Product> searchProducts(String name) throws RemoteException;
    boolean updateProduct(Product product) throws RemoteException;  // Ajout de la méthode update
    boolean deleteProduct(int productId) throws RemoteException; 
    boolean validateUser(String username, String password) throws RemoteException;
}

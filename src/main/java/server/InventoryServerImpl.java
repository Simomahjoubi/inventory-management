package main.java.server;

import main.java.database.ProductDAO;
import main.java.database.UserDAO;
import main.java.model.Product;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class InventoryServerImpl extends UnicastRemoteObject implements InventoryRemote {
    private ProductDAO productDAO;

    public InventoryServerImpl(ProductDAO productDAO) throws RemoteException {
        this.productDAO = productDAO;
    }

    @Override
    public boolean addProduct(Product product) throws RemoteException {
        return productDAO.addProduct(product);
    }

    @Override
    public List<Product> searchProducts(String name) throws RemoteException {
        return productDAO.searchProducts(name);
    }
    @Override
    public boolean updateProduct(Product product) throws RemoteException {
        return productDAO.updateProduct(product);  // Appel à la méthode du DAO
    }

    @Override
    public boolean deleteProduct(int productId) throws RemoteException {
        return productDAO.deleteProduct(productId);  // Appel à la méthode du DAO
    }
    @Override
    public boolean validateUser(String username, String password) throws RemoteException {
    UserDAO userDAO = new UserDAO(productDAO.getConnection()); // Utilise getConnection()
    return userDAO.validateUser(username, password);
    }


}

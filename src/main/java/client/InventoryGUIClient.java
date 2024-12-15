package main.java.client;

import main.java.model.Product;
import main.java.server.InventoryRemote;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.List;

public class InventoryGUIClient extends JFrame {
    private InventoryRemote inventoryService;
    private JTextField nameField, categoryField, quantityField, priceField, searchField;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public InventoryGUIClient() {
        // Connexion au serveur RMI
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 5555);
            inventoryService = (InventoryRemote) registry.lookup("InventoryService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion RMI");
            e.printStackTrace();
        }

        // Configuration de la fenêtre
        setTitle("Système de gestion d'inventaire");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialiser les composants graphiques
        initializeComponents();

        // Ajouter les composants à la fenêtre
        pack();
        setLocationRelativeTo(null); // Centrer la fenêtre
    }

    private void initializeComponents() {
        // Panneau d'entrée des informations du produit
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Détails du produit"));

        inputPanel.add(new JLabel("Nom:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Catégorie:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Quantité:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);

        inputPanel.add(new JLabel("Prix:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        // Panneau des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Ajouter le produit");
        addButton.addActionListener(e -> addProduct());
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Mettre à jour le produit");
        updateButton.addActionListener(e -> updateProduct());
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Supprimer le produit");
        deleteButton.addActionListener(e -> deleteProduct());
        buttonPanel.add(deleteButton);

        // Panneau de recherche
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> searchProducts());
        searchPanel.add(new JLabel("Rechercher:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Panneau du tableau
        String[] columnNames = {"ID", "Nom", "Catégorie", "Quantité", "Prix"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Ajouter un écouteur pour sélectionner une ligne dans le tableau
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                int selectedRow = productTable.getSelectedRow();
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                categoryField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                quantityField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                priceField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });

        // Ajouter les panneaux à la fenêtre
        add(inputPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void addProduct() {
        try {
            // Valider les entrées
            if (!validateInput()) return;

            // Créer un produit à partir des champs de texte
            Product product = new Product(
                0, // L'ID est généré par la base de données
                nameField.getText(),
                categoryField.getText(),
                Integer.parseInt(quantityField.getText()),
                Double.parseDouble(priceField.getText())
            );

            // Ajouter le produit au serveur via RMI
            boolean success = inventoryService.addProduct(product);
            if (success) {
                JOptionPane.showMessageDialog(this, "Produit ajouté avec succès");
                refreshTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du produit");
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion RMI");
            e.printStackTrace();
        }
    }

    private void updateProduct() {
        try {
            // Valider les entrées et s'assurer qu'une ligne est sélectionnée
            if (!validateInput() || productTable.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit à mettre à jour.");
                return;
            }

            // Récupérer l'ID du produit sélectionné dans le tableau
            int selectedId = (int) tableModel.getValueAt(productTable.getSelectedRow(), 0);

            // Créer un produit avec les nouvelles informations
            Product product = new Product(
                selectedId,
                nameField.getText(),
                categoryField.getText(),
                Integer.parseInt(quantityField.getText()),
                Double.parseDouble(priceField.getText())
            );

            // Mettre à jour le produit sur le serveur via RMI
            boolean success = inventoryService.updateProduct(product);
            if (success) {
                JOptionPane.showMessageDialog(this, "Produit mis à jour avec succès");
                refreshTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du produit");
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion RMI");
            e.printStackTrace();
        }
    }

    private void deleteProduct() {
        try {
            // Vérifier qu'une ligne est sélectionnée
            if (productTable.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit à supprimer.");
                return;
            }

            // Récupérer l'ID du produit sélectionné
            int selectedId = (int) tableModel.getValueAt(productTable.getSelectedRow(), 0);

            // Supprimer le produit du serveur via RMI
            boolean success = inventoryService.deleteProduct(selectedId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Produit supprimé avec succès");
                refreshTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du produit");
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion RMI");
            e.printStackTrace();
        }
    }

    private void searchProducts() {
        try {
            String searchTerm = searchField.getText().trim();
            List<Product> products = inventoryService.searchProducts(searchTerm);

            // Effacer les anciennes données du tableau
            tableModel.setRowCount(0);

            // Ajouter les résultats de la recherche au tableau
            for (Product product : products) {
                tableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    product.getQuantity(),
                    product.getPrice()
                });
            }

            if (products.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun produit trouvé.");
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion RMI");
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        try {
            // Rafraîchir le tableau avec tous les produits
            List<Product> products = inventoryService.searchProducts("");
            
            // Effacer les anciennes données du tableau
            tableModel.setRowCount(0);

            // Ajouter tous les produits au tableau
            for (Product product : products) {
                tableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    product.getQuantity(),
                    product.getPrice()
                });
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion RMI");
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        // Validation des champs de texte
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom ne peut pas être vide.");
            return false;
        }

        if (categoryField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La catégorie ne peut pas être vide.");
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity < 0) {
                JOptionPane.showMessageDialog(this, "La quantité doit être un nombre positif.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La quantité doit être un nombre valide.");
            return false;
        }

        try {
            double price = Double.parseDouble(priceField.getText());
            if (price < 0) {
                JOptionPane.showMessageDialog(this, "Le prix doit être un nombre positif.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Le prix doit être un nombre valide.");
            return false;
        }

        return true;
    }

    private void clearFields() {
        // Effacer tous les champs de texte
        nameField.setText("");
        categoryField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }

    public static void main(String[] args) {
        // Lancer l'interface graphique
        SwingUtilities.invokeLater(() -> {
            InventoryGUIClient client = new InventoryGUIClient();
            client.refreshTable(); // Initialiser le tableau avec les produits
            client.setVisible(true);
        });
    }
}

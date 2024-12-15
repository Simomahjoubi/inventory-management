package main.java.client;

import javax.swing.*;

import main.java.server.InventoryRemote;

import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private InventoryRemote inventoryService;

    public LoginGUI() {
        // Connexion au serveur RMI
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 5555);
            inventoryService = (InventoryRemote) registry.lookup("InventoryService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion RMI");
            e.printStackTrace();
            return;
        }

        // Configuration de la fenêtre
        setTitle("Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre

        // Initialiser les composants
        initializeComponents();
    }

    private void initializeComponents() {
        // Création du panneau principal avec un layout BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10)); // Marges autour du panneau principal
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges internes

        // Panneau pour les champs d'entrée
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Nom d'utilisateur :");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordField = new JPasswordField(15);

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        // Bouton de connexion
        loginButton = new JButton("Se connecter");
        loginButton.setPreferredSize(new Dimension(120, 30)); // Taille du bouton
        loginButton.addActionListener(e -> login());

        // Panneau pour le bouton (centré)
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);

        // Ajouter les panneaux au panneau principal
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Ajouter le panneau principal à la fenêtre
        add(panel);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
    
        try {
            // Appeler le serveur RMI pour valider l'utilisateur
            boolean isValidUser = inventoryService.validateUser(username, password);
            if (isValidUser) {
                //JOptionPane.showMessageDialog(this, "Connexion réussie !");
                // Lancer l'interface d'inventaire après une connexion réussie
                new InventoryGUIClient().setVisible(true);
                this.dispose(); // Fermer la fenêtre de login
            } else {
                JOptionPane.showMessageDialog(this, "Nom d'utilisateur ou mot de passe incorrect");
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion avec le serveur");
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginGUI().setVisible(true);
        });
    }
}

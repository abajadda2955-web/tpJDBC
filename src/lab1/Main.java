import java.sql.*;


public static void main(String[] args) {

    String URL = "jdbc:mysql://localhost:3306/atelier";
    String USER = "root";
    String PASSWORD = "";
    // Charger le driver (optionnel en JDBC moderne, mais utile pédagogiquement)
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        System.out.println("Driver MySQL introuvable. Vérifier l'import du .jar.");
        e.printStackTrace();
        return;
    }

    // Connexion + Statement
    try (Connection conn = DriverManager.getConnection( URL , USER  , PASSWORD);
         Statement stmt = conn.createStatement()) {

        System.out.println("Connexion MySQL OK.");

        // Réinitialiser la table
        stmt.executeUpdate("DROP TABLE IF EXISTS DevData");
        // Modifie ta ligne de création de table
        stmt.executeUpdate(
                "CREATE TABLE DevData (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT, " + // Ajout de l'ID avec 2 contraintes ( unique et AI)
                        "Developpeurs VARCHAR(32) NOT NULL, " +
                        "Jour VARCHAR(16) NOT NULL, " +
                        "NbScripts INT NOT NULL CHECK (NbScripts >= 0)" + // Ajout de la contrainte de validation
                        ")"
        );

        // Données de test
        stmt.executeUpdate("INSERT INTO DevData (Developpeurs , Jour , NbScripts) VALUES ('ALAMI', 'Lundi', 1)");
        stmt.executeUpdate("INSERT INTO DevData (Developpeurs , Jour , NbScripts) VALUES ('WAFI', 'Lundi', 2)");
        stmt.executeUpdate("INSERT INTO DevData (Developpeurs , Jour , NbScripts) VALUES ('SLAMI', 'Mardi', 9)");
        stmt.executeUpdate("INSERT INTO DevData (Developpeurs , Jour , NbScripts) VALUES  ('ALAMI', 'Mardi', 3)");
        stmt.executeUpdate("INSERT INTO DevData (Developpeurs , Jour , NbScripts) VALUES ('WAFI', 'Mardi', 4)");
        stmt.executeUpdate("INSERT INTO DevData (Developpeurs , Jour , NbScripts) VALUES ('SLAMI', 'Mercredi', 2)");

        System.out.println("Table créée + données insérées.");

        // Statistique 1 : max par jour
        System.out.println("\n--- Max scripts par jour ---");
        try (ResultSet rs = stmt.executeQuery(
                "SELECT Jour, Developpeurs, MAX(NbScripts) AS MaxScripts " +
                        "FROM DevData GROUP BY Jour"
        )) {
            while (rs.next()) {
                String jour = rs.getString("Jour");
                String dev = rs.getString("Developpeurs");
                int max = rs.getInt("MaxScripts");
                System.out.println(jour + " | " + dev + " | " + max);
            }
        }

        // Statistique 2 : classement par total décroissant
        System.out.println("\n--- Classement des développeurs (total scripts) ---");
        try (ResultSet rs = stmt.executeQuery(
                "SELECT Developpeurs, SUM(NbScripts) AS Total " +
                        "FROM DevData GROUP BY Developpeurs ORDER BY Total DESC"
        )) {
            while (rs.next()) {
                String dev = rs.getString("Developpeurs");
                int total = rs.getInt("Total");
                System.out.println(dev + " | " + total);
            }
        }

        // Statistique 3 : total semaine
        System.out.println("\n--- Total scripts semaine ---");
        try (ResultSet rs = stmt.executeQuery("SELECT SUM(NbScripts) AS TotalSemaine FROM DevData")) {
            if (rs.next()) {
                System.out.println("Total semaine : " + rs.getInt("TotalSemaine"));
            }
        }

        // Statistique 4 : total pour un développeur (PreparedStatement)
        System.out.println("\n--- Total scripts pour un développeur (PreparedStatement) ---");
        String devRecherche = "ALAMI";

        String sql = "SELECT SUM(NbScripts) AS TotalDev FROM DevData WHERE Developpeurs = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, devRecherche);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int totalDev = rs.getInt("TotalDev");
                    System.out.println("Total pour " + devRecherche + " : " + totalDev);
                }
            }
        }
        //demander le nom de dev et calculer la moyenne de script par jour
        Scanner sc = new Scanner(System.in);
        System.out.println("entrer le nom du developpeur :");
        String nomSaisi = sc.nextLine();
        String sql2 = "select AVG(NbScripts) as moyenne from DevData where Developpeurs = ? " ;
        try (PreparedStatement ps = conn.prepareStatement(sql2)) {
            ps.setString(1 ,nomSaisi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Déplace le curseur sur la première ligne de résultat
                    int Moy = rs.getInt("moyenne");
                    System.out.println("la moyenne pour " + nomSaisi + " : " + Moy);
                } else {
                    System.out.println("Aucune donnée trouvée pour ce développeur.");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }


    } catch (SQLException e) {
        System.out.println("Erreur SQL : vérifier MySQL (base, user/password, port).");
        e.printStackTrace();
    }
}




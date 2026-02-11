package lab2.service;

import lab2.dao.EmployeDao;
import lab2.entities.Employe;
import lab2.entities.Machine ;

import java.util.List;

public class EmployeService {
    private final EmployeDao dao = new EmployeDao();

    public Employe getEmploye(Employe e) throws Exception {
        return dao.findById(e.getId());
    }

    public List<Employe> listEmployes() throws Exception {
        return dao.findAll();
    }

    public Employe createEmploye(Employe e) throws Exception {
        // Validation : nom non vide
        if (e.getNom() == null || e.getNom().trim().isEmpty()) {
            throw new Exception("Erreur de validation : Le nom de l'employé ne peut pas être vide.");
        }

        // Validation : poste non vide
        if (e.getPoste() == null || e.getPoste().trim().isEmpty()) {
            throw new Exception("Erreur de validation : Le poste de l'employé ne peut pas être vide.");
        }

        dao.insert(e);
        return e;
    }

    public boolean updateEmploye(Employe e) throws Exception {
        return dao.update(e);
    }

    public boolean deleteEmploye(Employe e) throws Exception {
        return dao.delete(e.getId());
    }

}

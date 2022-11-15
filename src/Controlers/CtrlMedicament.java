package Controlers;

import Entities.Consultation;
import Entities.Medicament;
import Tools.ConnexionBDD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CtrlMedicament
{
    private Connection cnx;
    private PreparedStatement ps;
    private ResultSet rs;

    public CtrlMedicament() {
        cnx = ConnexionBDD.getCnx();
    }

    public ArrayList<Medicament> GetAllMedicamentsByIdConsultations(int idConsultation)
    {
        ArrayList<Medicament> medicaments = new ArrayList<>();

        try {
            ps = cnx.prepareStatement("SELECT idMedoc, nomMedoc, prixMedoc FROM medicament, prescrire WHERE medicament.idMedoc = prescrire.numMedoc AND prescrire.numConsult = ?");
            ps.setInt(1, idConsultation);
            rs = ps.executeQuery();
            while (rs.next()){
                Medicament medicament = new Medicament(
                        rs.getInt("idMedoc"),
                        rs.getString("nomMedoc"),
                        rs.getDouble("prixMedoc")
                );

                medicaments.add(medicament);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return medicaments;
    }
    public ArrayList<Medicament> getAllMedicaments()
    {
        ArrayList<Medicament> medicaments = new ArrayList<>();

        try {
            ps = cnx.prepareStatement("SELECT idMedoc, nomMedoc, prixMedoc FROM medicament");

            rs = ps.executeQuery();
            while (rs.next()){
                Medicament medicament = new Medicament(
                        rs.getInt("idMedoc"),
                        rs.getString("nomMedoc"),
                        rs.getDouble("prixMedoc")
                );

                medicaments.add(medicament);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return medicaments;
    }
}

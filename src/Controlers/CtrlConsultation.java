package Controlers;

import Entities.Consultation;
import Tools.ConnexionBDD;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CtrlConsultation
{
    private Connection cnx;
    private PreparedStatement ps;
    private ResultSet rs;

    public CtrlConsultation() {
        cnx = ConnexionBDD.getCnx();
    }

    public ArrayList<Consultation> GetAllConsultations()
    {
        ArrayList<Consultation> consultations = new ArrayList<>();

        try {
            ps = cnx.prepareStatement("SELECT consultation.idConsult, consultation.dateConsult, patient.nomPatient, medecin.nomMedecin, SUM(prescrire.quantite * (medicament.prixMedoc - medicament.prixMedoc * vignette.tauxRemb/100)) as montant\n" +
                    "FROM medicament, vignette, consultation, prescrire, patient, medecin\n" +
                    "WHERE medicament.numVignette = vignette.idVignette\n" +
                    "AND consultation.idConsult = prescrire.numConsult\n" +
                    "AND prescrire.numMedoc = medicament.idMedoc\n" +
                    "AND consultation.numMedecin = medecin.idMedecin\n" +
                    "AND consultation.numPatient = patient.idPatient\n" +
                    "GROUP BY consultation.idConsult\n" +
                    "ORDER BY consultation.idConsult");

            rs = ps.executeQuery();

            while (rs.next()){
                Consultation consultation = new Consultation(
                        rs.getInt("idConsult"),
                        rs.getString("dateConsult"),
                        rs.getString("nomPatient"),
                        rs.getString("nomMedecin"),
                        rs.getDouble("montant")
                );
                consultations.add(consultation);
            }

            ps.close();
            rs.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return consultations;
    }
    public int getLastNumberOfConsultation()
    {
        int nbConsult = 0;

        try {
            ps = cnx.prepareStatement("SELECT COUNT(*) as nbConsult FROM consultation");
            rs = ps.executeQuery();

            while (rs.next()){
                nbConsult = rs.getInt("nbConsult") + 1;
            }


            ps.close();
            rs.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return nbConsult;
    }
    public void InsertConsultation(int idConsult, String dateConsultation, int numPatient,int numMedecin)
    {

        try {
            ps = cnx.prepareStatement("insert into consultation(idConsult, dateConsult, numPatient, numMedecin) " +
                    "values (?,?,?,?)");
            ps.setInt(1, idConsult);
            ps.setDate(2, Date.valueOf(dateConsultation));
            ps.setInt(3,numPatient);
            ps.setInt(4,numMedecin);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

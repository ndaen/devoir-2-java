package Controlers;

import Tools.ConnexionBDD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CtrlMedecin
{
    private Connection cnx;
    private PreparedStatement ps;
    private ResultSet rs;

    public CtrlMedecin() {
        cnx = ConnexionBDD.getCnx();
    }

    public ArrayList<String> getAllMedecins()
    {
        ArrayList<String> medecins = new ArrayList<>();

        try {
            ps = cnx.prepareStatement("SELECT nomMedecin FROM medecin");
            rs = ps.executeQuery();
            while (rs.next()){
                medecins.add(rs.getString("nomMedecin"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return medecins;
    }

    public int getIdMedecinByName(String nomMed)
    {
        int idMedecin = 0;
        try {
            ps = cnx.prepareStatement("SELECT idMedecin FROM medecin WHERE nomMedecin = ?");
            ps.setString(1,nomMed);
            rs = ps.executeQuery();
            while (rs.next()){
                idMedecin = rs.getInt("idMedecin");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return idMedecin;
    }
}

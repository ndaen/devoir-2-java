package Controlers;

import Entities.Consultation;
import Tools.ConnexionBDD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CtrlPatient
{
    private Connection cnx;
    private PreparedStatement ps;
    private ResultSet rs;

    public CtrlPatient() {
        cnx = ConnexionBDD.getCnx();
    }

    public ArrayList<String> getAllPatients()
    {

        ArrayList<String> patients = new ArrayList<>();

        try {
            ps = cnx.prepareStatement("SELECT nomPatient FROM patient");
            rs = ps.executeQuery();
            while (rs.next()){
                patients.add(rs.getString("nomPatient"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return patients;
    }
    public int getIdPatientByName(String nomPat)
    {
        int idPatient = 0;
        try {
            ps = cnx.prepareStatement("SELECT idPatient FROM patient WHERE nomPatient = ?");
            ps.setString(1,nomPat);
            rs = ps.executeQuery();
            while (rs.next()){
                idPatient = rs.getInt("idPatient");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return idPatient;
    }
}

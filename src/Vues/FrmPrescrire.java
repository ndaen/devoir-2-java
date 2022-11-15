package Vues;

import Controlers.*;
import Entities.Consultation;
import Entities.Medicament;
import Tools.ModelJTable;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FrmPrescrire extends JFrame
{
    private JPanel pnlRoot;
    private JLabel lblTitre;
    private JLabel lblNumero;
    private JLabel lblDate;
    private JLabel lblNomMedecin;
    private JTextField txtNumeroConsultation;
    private JComboBox cboPatients;
    private JComboBox cboMedecins;
    private JButton btnInserer;
    private JTable tblMedicaments;
    private JPanel pnlDate;
    private JLabel lblNomPatient;
    private JLabel lblMedicaments;
    private JDateChooser dcDateConsultation;
    private CtrlConsultation ctrlConsultation;
    private CtrlMedecin ctrlMedecin;
    private CtrlPatient ctrlPatient;
    private CtrlMedicament ctrlMedicament;
    private CtrlPrescrire ctrlPrescrire;
    private ModelJTable modelJTable;
    public FrmPrescrire()
    {
        this.setTitle("Prescrire");
        this.setContentPane(pnlRoot);
        this.pack();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // initialisation des controllers

        ctrlConsultation = new CtrlConsultation();
        ctrlMedecin = new CtrlMedecin();
        ctrlPatient = new CtrlPatient();
        ctrlMedicament = new CtrlMedicament();
        ctrlPrescrire = new CtrlPrescrire();

        // initialisation du model Jtable

        modelJTable = new ModelJTable();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                dcDateConsultation = new JDateChooser();
                dcDateConsultation.setDateFormatString("yyyy-MM-dd");
                pnlDate.add(dcDateConsultation);

                // A vous de jouer

                // On set le numéro de consultation
                txtNumeroConsultation.setText(String.valueOf(ctrlConsultation.getLastNumberOfConsultation()));

                // Remplissage des combobox avec les noms des patients et les noms des médecins
                for (String nomPatient: ctrlPatient.getAllPatients()){
                    cboPatients.addItem(nomPatient);
                }
                for (String nomMedecin: ctrlMedecin.getAllMedecins()){
                    cboMedecins.addItem(nomMedecin);
                }

                // Remplissage du tableau des medicaments
                modelJTable.loadDataMedocQte(ctrlMedicament.getAllMedicaments());
                tblMedicaments.setModel(modelJTable);
            }
        });

        btnInserer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // A vous de jouer
                // Verrification si la date est saisie
                if (dcDateConsultation.getDate() == null){
                    JOptionPane.showMessageDialog(null,"Saisir une date");
                }else{

                    ArrayList<Medicament> medicaments = new ArrayList<>();

                    // on reccupere les medicaments qui ont été prescrit
                    for (int i = 0; i<tblMedicaments.getRowCount(); i++){
                        if ( Integer.parseInt(tblMedicaments.getValueAt(i, 3).toString()) !=  0){
                                Medicament medicament = new Medicament(
                                        (Integer) tblMedicaments.getValueAt(i, 0),
                                        (String) tblMedicaments.getValueAt(i,1),
                                        (Double) tblMedicaments.getValueAt(i, 2),
                                        Integer.parseInt(tblMedicaments.getValueAt(i,3).toString()));

                                medicaments.add(medicament);
                        }
                    }
                    // on ajoute la consultation en premier pour pouvoir faire les insert into de prescrire
                    ctrlConsultation.InsertConsultation(
                            Integer.parseInt(txtNumeroConsultation.getText()),
                            sdf.format(dcDateConsultation.getDate()),
                            ctrlPatient.getIdPatientByName(cboPatients.getSelectedItem().toString()),
                            ctrlMedecin.getIdMedecinByName(cboMedecins.getSelectedItem().toString())
                    );

                    // On ajoute dasns prescrire chaque medicament prescrit
                    for (Medicament medicament: medicaments){
                        ctrlPrescrire.InsertPrescrire(
                                Integer.parseInt(txtNumeroConsultation.getText()),
                                medicament.getNumero(),
                                medicament.getQuantite()
                        );
                    }
                    // message de confirmation
                    JOptionPane.showMessageDialog(null, "Consultation bien ajouté");

                    // on met à jour les élements de la page
                    txtNumeroConsultation.setText(String.valueOf(ctrlConsultation.getLastNumberOfConsultation()));
                    for (String nomPatient: ctrlPatient.getAllPatients()){
                        cboPatients.addItem(nomPatient);
                    }
                    for (String nomMedecin: ctrlMedecin.getAllMedecins()){
                        cboMedecins.addItem(nomMedecin);
                    }
                    modelJTable.loadDataMedocQte(ctrlMedicament.getAllMedicaments());
                    tblMedicaments.setModel(modelJTable);
                }
            }
        });
    }
}

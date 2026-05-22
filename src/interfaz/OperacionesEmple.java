package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logica.Departamento;
import logica.Empleado;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

@SuppressWarnings("serial")
public class OperacionesEmple extends JDialog {

    private static final String BBDD = "Empleados.dat";
    private JPanel contentPane;
    private JTextField txNumEmple, txNombre, txPoblacion, txOficio, txSalario, txNumDepart;
    private JLabel lblResultado;

    public OperacionesEmple() {

        setTitle("Operaciones con Empleados");
        setBounds(100, 100, 470, 361);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblEmpleados = new JLabel("Operaciones EMPLEADOS");
        lblEmpleados.setForeground(Color.BLUE);
        lblEmpleados.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblEmpleados.setBounds(120, 11, 232, 35);
        contentPane.add(lblEmpleados);

        txNumEmple = new JTextField();
        txNumEmple.setBounds(164, 71, 98, 20);
        contentPane.add(txNumEmple);

        txNombre = new JTextField();
        txNombre.setBounds(164, 96, 218, 20);
        contentPane.add(txNombre);

        txPoblacion = new JTextField();
        txPoblacion.setBounds(164, 121, 218, 20);
        contentPane.add(txPoblacion);

        txOficio = new JTextField();
        txOficio.setBounds(164, 146, 218, 20);
        contentPane.add(txOficio);

        txSalario = new JTextField();
        txSalario.setBounds(164, 171, 218, 20);
        contentPane.add(txSalario);

        txNumDepart = new JTextField();
        txNumDepart.setBounds(164, 196, 218, 20);
        contentPane.add(txNumDepart);

        JButton btnInsertar = new JButton("Insertar");
        btnInsertar.setBounds(21, 274, 120, 25);
        contentPane.add(btnInsertar);

        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.setBounds(293, 70, 100, 25);
        contentPane.add(btnConsultar);

        JButton btnBorrar = new JButton("Borrar");
        btnBorrar.setBounds(150, 274, 120, 25);
        contentPane.add(btnBorrar);

        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBounds(280, 274, 120, 25);
        contentPane.add(btnModificar);

        lblResultado = new JLabel("");
        lblResultado.setBounds(37, 230, 350, 20);
        contentPane.add(lblResultado);

        // 🔥 NUEVOS LISTENERS
        btnInsertar.addActionListener(new InsertarListener());
        btnConsultar.addActionListener(new ConsultarListener());
        btnBorrar.addActionListener(new BorrarListener());
        btnModificar.addActionListener(new ModificarListener());
    }

    // ================= CLASES ANIDADAS =================

    class InsertarListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ODB odb = ODBFactory.open(BBDD);
            try {
                int num = Integer.parseInt(txNumEmple.getText());
                double sal = Double.parseDouble(txSalario.getText());
                int depNum = Integer.parseInt(txNumDepart.getText());

                IQuery q = new CriteriaQuery(Departamento.class, Where.equal("dept_no", depNum));

                if (!odb.getObjects(q).isEmpty()) {
                    Departamento d = (Departamento) odb.getObjects(q).getFirst();
                    odb.store(new Empleado(num, txNombre.getText(), txPoblacion.getText(), txOficio.getText(), sal, d));
                    lblResultado.setText("Insertado");
                }
            } catch (Exception ex) {
                lblResultado.setText("Error");
            } finally {
                odb.close();
            }
        }
    }

    class ConsultarListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ODB odb = ODBFactory.open(BBDD);
            try {
                int num = Integer.parseInt(txNumEmple.getText());
                IQuery q = new CriteriaQuery(Empleado.class, Where.equal("emp_no", num));

                if (!odb.getObjects(q).isEmpty()) {
                    Empleado emp = (Empleado) odb.getObjects(q).getFirst();
                    txNombre.setText(emp.getNombre());
                    lblResultado.setText("OK");
                }
            } catch (Exception ex) {
                lblResultado.setText("Error");
            } finally {
                odb.close();
            }
        }
    }

    class BorrarListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ODB odb = ODBFactory.open(BBDD);
            try {
                int num = Integer.parseInt(txNumEmple.getText());
                IQuery q = new CriteriaQuery(Empleado.class, Where.equal("emp_no", num));

                if (!odb.getObjects(q).isEmpty()) {
                    odb.delete(odb.getObjects(q).getFirst());
                    lblResultado.setText("Borrado");
                }
            } catch (Exception ex) {
                lblResultado.setText("Error");
            } finally {
                odb.close();
            }
        }
    }

    class ModificarListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ODB odb = ODBFactory.open(BBDD);
            try {
                int num = Integer.parseInt(txNumEmple.getText());
                IQuery q = new CriteriaQuery(Empleado.class, Where.equal("emp_no", num));

                if (!odb.getObjects(q).isEmpty()) {
                    Empleado emp = (Empleado) odb.getObjects(q).getFirst();
                    emp.setNombre(txNombre.getText());
                    odb.store(emp);
                    lblResultado.setText("Modificado");
                }
            } catch (Exception ex) {
                lblResultado.setText("Error");
            } finally {
                odb.close();
            }
        }
    }
}

package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logica.Departamento;
import logica.Excepciones.NumDepartDuplicado;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class OperacionesDepart extends JDialog implements Interfazdepart {
	private static final String BBDD="Empleados.dat";
	private JPanel contentPane;
	private JTextField txNumDepart;
	private JTextField txNombre;
	private JTextField txPoblacion;
	private JLabel lblRespuesta;

	public OperacionesDepart() {
		setTitle("Operaciones departamentos.");
		setModal(true);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblOperaciones = new JLabel("Operaciones DEPARTAMENTOS");
		lblOperaciones.setBackground(Color.YELLOW);
		lblOperaciones.setHorizontalAlignment(SwingConstants.CENTER);
		lblOperaciones.setForeground(Color.BLUE);
		lblOperaciones.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblOperaciones.setBounds(75, 11, 292, 35);
		contentPane.add(lblOperaciones);
		
		JLabel lblNumDepart = new JLabel("Num Departamento");
		lblNumDepart.setBounds(36, 74, 112, 16);
		contentPane.add(lblNumDepart);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(36, 102, 55, 16);
		contentPane.add(lblNombre);
		
		JLabel lblPoblacion = new JLabel("Poblaci\u00F3n");
		lblPoblacion.setBounds(36, 130, 69, 16);
		contentPane.add(lblPoblacion);
		
		txNumDepart = new JTextField();
		txNumDepart.setBounds(166, 72, 95, 20);
		contentPane.add(txNumDepart);
		txNumDepart.setColumns(10);
		
		txNombre = new JTextField();
		txNombre.setBounds(166, 100, 215, 20);
		contentPane.add(txNombre);
		txNombre.setColumns(10);
		
		txPoblacion = new JTextField();
		txPoblacion.setBounds(166, 128, 215, 20);
		contentPane.add(txPoblacion);
		txPoblacion.setColumns(10);
		
		JButton btnConsultar = new JButton("Consultar");
		btnConsultar.setFont(new Font("Dialog", Font.BOLD, 10));
		btnConsultar.setBounds(292, 69, 89, 23);
		contentPane.add(btnConsultar);
		
		lblRespuesta = new JLabel("---------------------------------------------------------------------");
		lblRespuesta.setFont(new Font("Dialog", Font.BOLD, 14));
		lblRespuesta.setForeground(Color.RED);
		lblRespuesta.setBounds(34, 169, 345, 14);
		contentPane.add(lblRespuesta);
		
		JButton btnInsertarDepartamento = new JButton("Insertar Departamento");
		btnInsertarDepartamento.setMargin(new Insets(2, 4, 2, 4));
		btnInsertarDepartamento.setFont(new Font("Dialog", Font.BOLD, 10));
		btnInsertarDepartamento.setBounds(12, 224, 124, 26);
		contentPane.add(btnInsertarDepartamento);
		
		JButton btnBorrarDepartamento = new JButton("Borrar Departamento");
		btnBorrarDepartamento.setMargin(new Insets(2, 4, 2, 4));
		btnBorrarDepartamento.setFont(new Font("Dialog", Font.BOLD, 10));
		btnBorrarDepartamento.setBounds(150, 224, 124, 26);
		contentPane.add(btnBorrarDepartamento);
		
		JButton btnModifcarDepartamento = new JButton("Modifcar Departamento");
		btnModifcarDepartamento.setMargin(new Insets(2, 4, 2, 4));
		btnModifcarDepartamento.setFont(new Font("Dialog", Font.BOLD, 10));
		btnModifcarDepartamento.setBounds(288, 224, 129, 26);
		contentPane.add(btnModifcarDepartamento);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(152, 251, 152));
		panel.setForeground(Color.BLUE);
		panel.setBounds(12, 45, 399, 156);
		contentPane.add(panel);
		
		btnInsertarDepartamento.addActionListener(e -> insertardep());
		btnBorrarDepartamento.addActionListener(e -> borrardep());
		btnConsultar.addActionListener(e -> consultardep());
		btnModifcarDepartamento.addActionListener(e -> modificardep());
	}
	
	private void comprobarNumDepart(ODB odb, int num) throws NumDepartDuplicado{
		IQuery query=new CriteriaQuery(Departamento.class, Where.equal("dept_no", num));
		Objects<Departamento> dep=odb.getObjects(query);
		
		if(num<1)
			throw new NumDepartDuplicado("numero de departamento no valido");
		if(!dep.isEmpty())
			throw new NumDepartDuplicado("numero de departamento duplicado");
	}
	public void insertardep() {
	    int num;
	    String nom, pob;
	    ODB odb = ODBFactory.open(BBDD);

	    try {
	        num = Integer.parseInt(txNumDepart.getText());
	        comprobarNumDepart(odb, num);
	        if (!txNombre.getText().equals("") && !txPoblacion.getText().equals("")) {
	            nom = txNombre.getText();
	            pob = txPoblacion.getText();
	            odb.store(new Departamento(num, nom, pob));
	            lblRespuesta.setText("Departamento insertado correctamente");
	        } else {
	            lblRespuesta.setText("Campos vacios");
	        }
	    } catch (Exception e) {
	        lblRespuesta.setText("Error");
	    } finally {
	        odb.close();
	    }
	    
	}
	public void borrardep() {
	    int num;
	    ODB odb = ODBFactory.open(BBDD);
	    
	    try {
	        num = Integer.parseInt(txNumDepart.getText());
	        IQuery query = new CriteriaQuery(Departamento.class, Where.equal("dept_no", num));
	       
	        if (!odb.getObjects(query).isEmpty()) {
	            Departamento dep = (Departamento) odb.getObjects(query).getFirst();
	            odb.delete(dep);
	            lblRespuesta.setText("Departamento borrado");
	        } else {
	            lblRespuesta.setText("Departamento no existe");
	        }
	    } catch (NumberFormatException e) {
	        lblRespuesta.setText("Numero incorrecto");
	    } finally {
	        odb.close();
	    }
	    
	}
	public void consultardep() {
	    int num;
	    ODB odb = ODBFactory.open(BBDD);

	    try {
	        num = Integer.parseInt(txNumDepart.getText());
	        IQuery query = new CriteriaQuery(Departamento.class, Where.equal("dept_no", num));
	        if (!odb.getObjects(query).isEmpty()) {
	            Departamento dep = (Departamento) odb.getObjects(query).getFirst();
	            txNombre.setText(dep.getDnombre());
	            txPoblacion.setText(dep.getLoc());
	            lblRespuesta.setText("Consulta correcta");
	        } else {
	            lblRespuesta.setText("Departamento no existe");
	        }
	    } catch (NumberFormatException e) {
	        lblRespuesta.setText("Numero incorrecto");
	    } finally {
	        odb.close();
	    }
	    
	}
	public void modificardep() {

	    int num;
	    String nom, pob;
	    ODB odb = ODBFactory.open(BBDD);
	    
	    try {
	        num = Integer.parseInt(txNumDepart.getText());
	        IQuery query = new CriteriaQuery(
	                Departamento.class,
	                Where.equal("dept_no", num));
	        if (!odb.getObjects(query).isEmpty()) {
	            Departamento dep = (Departamento) odb.getObjects(query).getFirst();
	            nom = txNombre.getText();
	            pob = txPoblacion.getText();
	            dep.setDnombre(nom);
	            dep.setLoc(pob);
	            odb.store(dep);
	            lblRespuesta.setText("Departamento modificado");
	        } else {
	            lblRespuesta.setText("Departamento no existe");
	        }
	    } catch (NumberFormatException e) {
	        lblRespuesta.setText("Numero incorrecto");
	    } finally {
	        odb.close();
	    }
	    
	}
	
}


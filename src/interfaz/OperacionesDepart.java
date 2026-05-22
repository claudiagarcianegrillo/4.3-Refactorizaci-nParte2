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
		
		configurarVentana();
        crearComponentes();
        configurarEventos();
	}
		
	
	private void Insertardep(ODB odb) throws NumDepartDuplicado {
		num=Integer.parseInt(txNumDepart.getText());
		comprobarNumDepart(odb, num);
		if(!txNombre.getText().equals("")){
			if(!txPoblacion.getText().equals("")){
				nom=txNombre.getText();
				pob=txPoblacion.getText();
				odb.store(new Departamento(num,nom,pob));
				
				lblRespuesta.setText("Departamento insertado correctamente");
			}
			else
				lblRespuesta.setText("Error, poblacion vacia");
		}
		else
			lblRespuesta.setText("Error, nombre de departamento vacio");
	}

	private void borrardep(ODB odb) {
		int num=0;
		num=Integer.parseInt(txNumDepart.getText());
		IQuery query=new CriteriaQuery(Departamento.class, Where.equal("dept_no", num));
		Objects<Departamento> dep=odb.getObjects(query);
		if(!dep.isEmpty()){
			IQuery query2=new CriteriaQuery(Empleado.class, 
					Where.equal("dept.dept_no", dep.getFirst().getDept_no()));
			Objects<Empleado> emp=odb.getObjects(query2);
			for(Empleado e:emp){
				e.setDept(null);
				odb.store(e);
			}
			odb.delete(dep.getFirst());
			lblRespuesta.setText("Departamento borrado correctamente");
		}
		else
			lblRespuesta.setText("Error, el departamento no existe");
	}

	private void consultardep(ODB odb) {
		int num;
		num=Integer.parseInt(txNumDepart.getText());
		IQuery query=new CriteriaQuery(Departamento.class, Where.equal("dept_no", num));
		Objects<Departamento> dep=odb.getObjects(query);
		if(!dep.isEmpty()){
			txNombre.setText(dep.getFirst().getDnombre());
			txPoblacion.setText(dep.getFirst().getLoc());
			lblRespuesta.setText("Consulta satisfactoria");
		}
		else
			lblRespuesta.setText("Error, el departamento no existe");
	}

	private void modificardep(ODB odb) {
		int num;
		num=Integer.parseInt(txNumDepart.getText());
		IQuery query=new CriteriaQuery(Departamento.class, Where.equal("dept_no", num));
		Objects<Departamento> dep=odb.getObjects(query);
		if(!dep.isEmpty()){
			if(!txNombre.getText().equals("")){
				if(!txPoblacion.getText().equals("")){
					Departamento depar;
					depar=dep.getFirst();
					depar.setDnombre(txNombre.getText());
					depar.setLoc(txPoblacion.getText());
					odb.store(depar);
					lblRespuesta.setText("Modifcacion satisfactoria");
				}
				else
					lblRespuesta.setText("Error, poblacion vacia");
			}
			else
				lblRespuesta.setText("Error, nombre de departamento vacio");
		}
		else
			lblRespuesta.setText("Error, el departamento no existe");
	}
	
	private void configurarVentana() { 
		setTitle("Operaciones departamentos.");
	    setModal(true);
	    setBounds(100, 100, 450, 300);
	    contentPane = new JPanel();
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);
	    contentPane.setLayout(null);
	}

	private void crearComponentes() { 
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
	    
	    txNombre = new JTextField();
	    txNombre.setBounds(166, 100, 215, 20);
	    contentPane.add(txNombre);
	    
	    txPoblacion = new JTextField();
	    txPoblacion.setBounds(166, 128, 215, 20);
	    contentPane.add(txPoblacion);

	    lblRespuesta = new JLabel("--------------------------------------------------");
	    lblRespuesta.setFont(new Font("Dialog", Font.BOLD, 14));
	    lblRespuesta.setForeground(Color.RED);
	    lblRespuesta.setBounds(34, 169, 345, 14);
	    contentPane.add(lblRespuesta);

	    JPanel panel = new JPanel();
	    panel.setBackground(new Color(152, 251, 152));
	    panel.setBounds(12, 45, 399, 156);
	    contentPane.add(panel);
	}

	private void configurarEventos() { 
		// BOTÓN CONSULTAR
        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.setBounds(292, 69, 89, 23);
        btnConsultar.addActionListener(e -> {
            ODB odb = ODBFactory.open(BBDD);
            try { consultardep(odb); } 
            catch (NumberFormatException ex) { lblRespuesta.setText("Error número"); }
            finally { odb.close(); }
        });
        contentPane.add(btnConsultar);

        // BOTÓN INSERTAR
        JButton btnInsertar = new JButton("Insertar Departamento");
        btnInsertar.setBounds(12, 224, 124, 26);
        btnInsertar.addActionListener(e -> {
            ODB odb = ODBFactory.open(BBDD);
            try { Insertardep(odb); } 
            catch (NumDepartDuplicado ex) { lblRespuesta.setText("Error: " + ex.getMessage()); }
            catch (NumberFormatException ex) { lblRespuesta.setText("Error número"); }
            finally { odb.close(); }
        });
        contentPane.add(btnInsertar);

        // BOTÓN BORRAR
        JButton btnBorrar = new JButton("Borrar Departamento");
        btnBorrar.setBounds(150, 224, 124, 26);
        btnBorrar.addActionListener(e -> {
            ODB odb = ODBFactory.open(BBDD);
            try { borrardep(odb); } 
            catch (NumberFormatException ex) { lblRespuesta.setText("Error número"); }
            finally { odb.close(); }
        });
        contentPane.add(btnBorrar);

        // BOTÓN MODIFICAR
        JButton btnModificar = new JButton("Modificar Departamento");
        btnModificar.setBounds(288, 224, 129, 26);
        btnModificar.addActionListener(e -> {
            ODB odb = ODBFactory.open(BBDD);
            try { modificardep(odb); } 
            catch (NumberFormatException ex) { lblRespuesta.setText("Error número"); }
            finally { odb.close(); }
        });
        contentPane.add(btnModificar);
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


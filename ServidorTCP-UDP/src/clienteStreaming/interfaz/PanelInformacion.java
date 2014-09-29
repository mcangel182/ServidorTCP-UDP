package clienteStreaming.interfaz;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class PanelInformacion extends JPanel implements ActionListener
{

	public final static String STREAM = "STREAM";
	
	public InterfazPrincipal principal;
	public JLabel labDireccion;
	public JLabel labPuerto;
	public JTextField txtDireccion;
	public JTextField txtPuerto;
	
	public JButton btnStream;
	
	public PanelInformacion(InterfazPrincipal interfaz)
	{
		principal = interfaz;
		
		setLayout(new GridLayout(4, 2));
		TitledBorder border= BorderFactory.createTitledBorder("Dirección - Puerto");
		setBorder(border);
		
		labDireccion = new JLabel("Ingrese la dirección que transmite: ");
		labPuerto = new JLabel("Ingrese el puerto: ");
		
		txtDireccion = new JTextField();
		txtPuerto = new JTextField();
		
		add(labDireccion);
		add(txtDireccion);
		add(labPuerto);
		add(txtPuerto);
		
		btnStream = new JButton("Transmitir");
		btnStream.setActionCommand(STREAM);
		btnStream.addActionListener(this);
		
	}

	public void actionPerformed(ActionEvent e) 
	{
		String comando=e.getActionCommand();
        if (comando.equals(STREAM))
        {
        	if (txtDireccion.getText()!="" && txtPuerto.getText()!="");
//        	principal.stream(txtDireccion.getText(), txtPuerto.getText());
        }
	}
	
}

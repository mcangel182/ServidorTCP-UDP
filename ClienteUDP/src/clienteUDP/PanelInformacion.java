package clienteUDP;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class PanelInformacion extends JPanel implements ActionListener
{
	
	public final static String STREAM = "STREAM";
	
	private Interfaz principal;
	
	private JTextField txtDireccion;
	
	private JTextField txtPuerto;
	
	private JButton btnStream;
	
	
	public PanelInformacion(Interfaz interfaz)
	{
		principal = interfaz;
		
		setLayout(new GridLayout(5,1));
	
		JLabel labDireccion = new JLabel("Dirección Streaming");
		JLabel labPuerto = new JLabel("Puerto");
		labDireccion.setHorizontalAlignment( SwingConstants.CENTER );
		labPuerto.setHorizontalAlignment(SwingConstants.CENTER);
		
		txtDireccion = new JTextField();
		txtPuerto = new JTextField();
		txtDireccion.setHorizontalAlignment( SwingConstants.CENTER );
		txtPuerto.setHorizontalAlignment( SwingConstants.CENTER );
		
		add(labDireccion);
		add(txtDireccion);
		add(labPuerto);
		add(txtPuerto);
		
		btnStream = new JButton("Stream");
		btnStream.setActionCommand(STREAM);
		btnStream.addActionListener(this);
	
		add(btnStream);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand().equals(STREAM))
		{
			String dir = txtDireccion.getText();
			String puerto = txtPuerto.getText();
			
			if (dir!=null && !dir.equals("") && puerto!=null && !puerto.equals(""))
			{
				principal.Stream(txtDireccion.getText(), puerto);
			}
		}
	}
	
}

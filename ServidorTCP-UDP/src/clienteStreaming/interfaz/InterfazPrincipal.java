package clienteStreaming.interfaz;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class InterfazPrincipal extends JFrame
{
	
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	
//	private final PanelInformacion panelInformacion;
	
	public InterfazPrincipal()
	{
		 JFrame frame = new JFrame("Cliente UDP");
		 
		 mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

         frame.setContentPane(mediaPlayerComponent);

         frame.setLocation(100, 100);
         frame.setSize(1050, 600);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);

//         mediaPlayerComponent.getMediaPlayer().playMedia();
		 
//		 panelInformacion = new PanelInformacion(this);
         
		 
         
//         frame.add(panelInformacion);
		 
		 
         
//         mediaPlayerComponent.getMediaPlayer().playMedia("");
         
	}
	
//	public void stream(String dir, String puerto)
//	{
//		
//	}
	
	public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InterfazPrincipal();
            }
        });
    }
	
}

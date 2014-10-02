package clienteUDP;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

@SuppressWarnings("serial")
public class Interfaz extends JFrame
{

	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

	private Interfaz(String dir, String puerto) 
	{
		JFrame frame = new JFrame("Ejemplo");

		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

		frame.setContentPane(mediaPlayerComponent);

		frame.setLocation(100, 100);
		frame.setSize(1050, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		mediaPlayerComponent.getMediaPlayer().playMedia("http://"+dir+":"+puerto);
	}

	public static void main(final String[] args) {
		System.loadLibrary("jawt");
		NativeLibrary.addSearchPath(
				RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib"
				);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

		
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				String dir = JOptionPane.showInputDialog("Ingrese la dirección IP de la transmisión", JOptionPane.QUESTION_MESSAGE);
				String puerto = JOptionPane.showInputDialog("Ingrese el puerto de la transmisión", JOptionPane.QUESTION_MESSAGE);

				new Interfaz(dir, puerto);
			}
		});
	}

}


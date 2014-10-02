package clienteUDP;

import java.awt.BorderLayout;

import javax.swing.JFrame;
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
	
	private PanelInformacion panelInfo;

	private Interfaz() 
	{
		JFrame frame = new JFrame("Cliente UDP");
		
		panelInfo = new PanelInformacion(this);
		
		setLayout(new BorderLayout());

		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

		frame.add(BorderLayout.CENTER, mediaPlayerComponent);
		frame.add(BorderLayout.SOUTH, panelInfo);

		frame.setLocation(100, 100);
		frame.setSize(1050, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public void Stream(String dir, String puerto)
	{
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
				
				new Interfaz();
			}
		});
	}

}


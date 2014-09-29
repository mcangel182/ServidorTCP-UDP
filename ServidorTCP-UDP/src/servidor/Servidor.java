package servidor;

import javax.net.ssl.*;

import java.util.LinkedList;
import java.util.Queue;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.test.*;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class Servidor extends VlcjTest
{
	private static int numeroDeThreadsActivos=0;
	private static int limiteThreads=300;
	private static Queue<ThreadServidor> cola=new LinkedList<>();
	private static long timepoEnCola = 0;
	private static long timepoEnAtencion = 0;
	private static int clientesDesencolados = 0;
	private static int clientesAtendidos = 0;

	public static void main(String[] args) throws Exception
	{
		NativeLibrary.addSearchPath(
				RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib"
				);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		//streamingVideo();    
		SSLServerSocketFactory sslServerSocketFactory =
				(SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		SSLServerSocket sslServerSocket =
				(SSLServerSocket) sslServerSocketFactory.createServerSocket(9999);
		//ServerSocket servidor = new ServerSocket(9999);
		while (true)
		{
			SSLSocket socket = (SSLSocket) sslServerSocket.accept();
			//Socket socket=servidor.accept();
			ThreadServidor hilo=new ThreadServidor(socket);
			atenderCliente(hilo);
		}
	}

	public static synchronized void atenderCliente(ThreadServidor hilo){
		if (numeroDeThreadsActivos<limiteThreads){			
			numeroDeThreadsActivos++;
			hilo.start();	
		}
		else {
			cola.offer(hilo);
		}
	}

	public static synchronized void terminarConexion(long demora){
		clientesAtendidos++;
		timepoEnAtencion += demora;
		System.out.println("Tiempo promedio de atenci�n: "+timepoEnAtencion/clientesAtendidos);
		numeroDeThreadsActivos--;
		if (!cola.isEmpty()){
			ThreadServidor hilo2 = cola.poll();
			timepoEnCola += (System.currentTimeMillis()-hilo2.darTiempoDeCreacion());
			clientesDesencolados++;
			System.out.println("Tiempo promedio en cola: "+timepoEnCola/clientesDesencolados);
			atenderCliente(hilo2);
		}
		//streamingVideo();
	}

//	public static void streamingVideo(){
//		String media = "./data/0.mp4";
//		
//		String options = formatHttpStream("127.0.0.1", puertoUdp);
//
//		String[] args = {media};
//
//		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(args);
//		HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
//
//		mediaPlayer.playMedia(media, options);
//
//		try {
//			Thread.currentThread().join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		puertoUdp++;
//	}
//
//	private static String formatHttpStream(String serverAddress, int serverPort) {
//		StringBuilder sb = new StringBuilder(60);
//		sb.append(":sout=#duplicate{dst=std{access=http,mux=ts,");
//		sb.append("dst=");
//		sb.append(serverAddress);
//		sb.append(':');
//		sb.append(serverPort);
//		sb.append("}}");
//		return sb.toString();
//	}
}

package servidor;

import javax.net.ssl.*;
import java.io.*;
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
	private static int limiteThreads=50;
	private static int limiteCola=100000;
	private static Queue<ThreadServidor> cola=new LinkedList<>();
	private static long tiempoEnCola = 0;
	private static long tiempoEnAtencion = 0;
	private static int clientesDesencolados = 0;
	private static int clientesAtendidos = 0;
  private static File archivoEstadisticas = new File("./data/estadisticas.csv");

	public static void main(String[] args) throws Exception
	{
	  archivoEstadisticas.delete();
    escribirEstadisticas("numeroClientesAtendidos;numeroClientesQuePasaronPorLaCola;tiempoPromedioAtencionClientes;numeroClientesEnCola;numeroClientesEnAtencion;tiempoPromedioEsperaEnCola");
	  
	  
	  NativeLibrary.addSearchPath(
				RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib"
				);
    NativeLibrary.addSearchPath(
        RuntimeUtil.getLibVlcLibraryName(), "C:/Program Files (x86)/VideoLAN/VLC"
        );
		
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
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
		else if (cola.size()<limiteCola) {
			cola.offer(hilo);
		}
	}

	public static synchronized void terminarConexion(long demora){
		clientesAtendidos++;
		tiempoEnAtencion += demora;
		numeroDeThreadsActivos--;
		if (!cola.isEmpty()){
			ThreadServidor hilo2 = cola.poll();
			tiempoEnCola += (System.currentTimeMillis()-hilo2.darTiempoDeCreacion());
			clientesDesencolados++;
			atenderCliente(hilo2);
		}
		int numeroClientesAtendidos=clientesAtendidos;
    int numeroClientesQuePasaronPorLaCola=clientesDesencolados;
		long tiempoPromedioAtencionClientes=clientesAtendidos>0?tiempoEnAtencion/clientesAtendidos:0;
		int numeroClientesEnCola=cola.size();
		int numeroClientesEnAtencion=numeroDeThreadsActivos;
		long tiempoPromedioEsperaEnCola=clientesDesencolados>0?tiempoEnCola/clientesDesencolados:0;
		escribirEstadisticas(numeroClientesAtendidos+";"+numeroClientesQuePasaronPorLaCola+";"+tiempoPromedioAtencionClientes+";"+numeroClientesEnCola+";"+numeroClientesEnAtencion+";"+tiempoPromedioEsperaEnCola);
	}
	
	public static synchronized void escribirEstadisticas(String fila){
	  BufferedWriter escritor=null;
	  try
	  {
	    escritor=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivoEstadisticas,true)));
	    escritor.write(fila+"\r\n");
	  }
	  catch(Exception e)
	  {
	  }finally
	  {
	    try {
        escritor.close();
      }
      catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
	  }
	}
	
}

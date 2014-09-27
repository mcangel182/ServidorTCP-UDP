package servidor;

import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

public class Servidor
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
		ServerSocket servidor = new ServerSocket(9999);
		while (true)
		{
			Socket socket=servidor.accept();
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
		System.out.println("Tiempo promedio de atenci—n: "+timepoEnAtencion/clientesAtendidos);
		numeroDeThreadsActivos--;
		if (!cola.isEmpty()){
			ThreadServidor hilo2 = cola.poll();
			timepoEnCola += (System.currentTimeMillis()-hilo2.darTiempoDeCreacion());
			clientesDesencolados++;
			System.out.println("Tiempo promedio en cola: "+timepoEnCola/clientesDesencolados);
			atenderCliente(hilo2);
		}
	}

}

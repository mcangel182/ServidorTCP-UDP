package servidor;

import java.io.*;


import java.net.*;
import java.nio.file.*;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

import com.xuggle.mediatool.*;

public class ThreadServidor extends Thread
{

	private static int proximoConsecutivo=0;
	private long tiempoDeCreacion=0;
	private static int puertoUdp = 5554;

	private Socket socket;

	public ThreadServidor(Socket socket)
	{
		this.socket=socket;
		asignarTiempoDeCreacion(System.currentTimeMillis());
	}

	public void run()
	{
		int consecutivo=proximoConsecutivo++;
		long tiempoInicialEnMilisegundos=System.currentTimeMillis();
		System.out.println("Atendiendo cliente #"+consecutivo);
		try (ObjectOutputStream salida=new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream entrada=new ObjectInputStream(socket.getInputStream());)
		{
			Path archivoTemporalOriginal=Paths.get("./data/"+consecutivo+".avi");
			Path archivoTemporalConvertido=Paths.get("./data/"+consecutivo+".mp4");
			archivoTemporalOriginal.toFile().createNewFile();
			archivoTemporalConvertido.toFile().createNewFile();
			byte[] contenidoVideoOriginal=(byte[])(entrada.readObject());
			Files.write(archivoTemporalOriginal,contenidoVideoOriginal);
			convertirVideo(archivoTemporalOriginal.toString(),archivoTemporalConvertido.toString());
			byte[] contenidoVideoConvertido=Files.readAllBytes(archivoTemporalConvertido);
			salida.writeObject(contenidoVideoConvertido);
		}
		catch (Exception excepcion)
		{
			excepcion.printStackTrace();
		}
		long tiempoFinalEnMilisegundos=System.currentTimeMillis();
		long demoraEnMilisegundos=tiempoFinalEnMilisegundos-tiempoInicialEnMilisegundos;
		streamingVideo("./data/"+consecutivo+".mp4");
		Servidor.terminarConexion(demoraEnMilisegundos);
	}

	public static void convertirVideo(String archivoTemporalOriginal, String archivoTemporalConvertido)
	{
		IMediaReader mediaReader=ToolFactory.makeReader(archivoTemporalOriginal);
		IMediaWriter mediaWriter=ToolFactory.makeWriter(archivoTemporalConvertido,mediaReader);
		mediaReader.addListener(mediaWriter);
		while (mediaReader.readPacket()==null)
		{
		}
	}

	public static void streamingVideo(String media){
		puertoUdp++;
		String options = formatHttpStream("127.0.0.1", puertoUdp);
		String[] args = {media};

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(args);
		HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();

		mediaPlayer.playMedia(media, options);

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String formatHttpStream(String serverAddress, int serverPort) {
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#duplicate{dst=std{access=http,mux=ts,");
		sb.append("dst=");
		sb.append(serverAddress);
		sb.append(':');
		sb.append(serverPort);
		sb.append("}}");
		return sb.toString();
	}
	
	public long darTiempoDeCreacion() {
		return tiempoDeCreacion;
	}

	public void asignarTiempoDeCreacion(long tiempoDeCreacion) {
		this.tiempoDeCreacion = tiempoDeCreacion;
	}

}

package servidor;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventAdapter;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;

import com.xuggle.mediatool.*;

public class ThreadServidor extends Thread
{
	private static int proximoConsecutivo=0;
	private long tiempoDeCreacion=0;
	private static int puertoUdp = 5554;
	private static ConvertidorVideo convertidor;

	private Socket socket;

	public ThreadServidor(Socket socket)
	{
		this.socket=socket;
		asignarTiempoDeCreacion(System.currentTimeMillis());
		convertidor = new ConvertidorVideo();
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
			//convertirVideo(archivoTemporalOriginal.toString(),archivoTemporalConvertido.toString());
			convertidor.convertirVideo(archivoTemporalOriginal.toString(),archivoTemporalConvertido.toString());
			byte[] contenidoVideoConvertido=Files.readAllBytes(archivoTemporalConvertido);
			salida.writeObject(contenidoVideoConvertido);
		}
		catch (Exception excepcion)
		{
			excepcion.printStackTrace();
		}
		long tiempoFinalEnMilisegundos=System.currentTimeMillis();
		long demoraEnMilisegundos=tiempoFinalEnMilisegundos-tiempoInicialEnMilisegundos;
		Servidor.terminarConexion(demoraEnMilisegundos);
		streamingVideo("./data/"+consecutivo+".mp4");
	}

	public static void convertirVideo(String archivoTemporalOriginal, String archivoTemporalConvertido)
	{
		System.out.println("comienza conversion");
		IMediaReader mediaReader=ToolFactory.makeReader(archivoTemporalOriginal);
		IMediaWriter mediaWriter=ToolFactory.makeWriter(archivoTemporalConvertido,mediaReader);
		mediaReader.addListener(mediaWriter);
		while (mediaReader.readPacket()==null)
		{
		}
		System.out.println("termina conversion");
	}

	public static void streamingVideo(String media){
		puertoUdp++;
		String options = formatHttpStream("127.0.0.1", puertoUdp);
		String[] args = {media};
		
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        MediaListPlayer mediaListPlayer = mediaPlayerFactory.newMediaListPlayer();
		
		MediaList mediaList = mediaPlayerFactory.newMediaList();
        mediaList.addMedia(media, options);

        mediaListPlayer.setMediaList(mediaList);
        mediaListPlayer.setMode(MediaListPlayerMode.LOOP);

        mediaListPlayer.play();

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

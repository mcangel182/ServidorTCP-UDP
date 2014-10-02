package servidor;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;

public class ConvertidorVideo {

	public ConvertidorVideo(){
		
	}
	
	public synchronized void convertirVideo(String archivoTemporalOriginal, String archivoTemporalConvertido)
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
}

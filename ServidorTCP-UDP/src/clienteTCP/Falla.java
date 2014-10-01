package clienteTCP;

import com.xuggle.mediatool.*;

public class Falla extends Thread 
{
  
  private static int numeroConversionesVideo=10;
 
  private int identificador;
  
  public Falla(int identificador)
  {
    this.identificador=identificador;
  }

  public void run()
  {
    String archivo="C:/pruebas/video.avi";
    convertirVideo(archivo,archivo+"-"+identificador+".mp4");
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

  public static void main(String[] args)
  {
    for (int i=0; i<numeroConversionesVideo; i++)
    {
      Falla t=new Falla(i);
      t.start();
    }
  }
  
}

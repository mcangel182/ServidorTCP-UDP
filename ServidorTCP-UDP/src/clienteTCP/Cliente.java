package clienteTCP;

import java.io.*;
import javax.net.ssl.*;
//import java.net.*;
import java.nio.file.*;
import java.util.*;

public class Cliente
{

  public static void main(String[] args) throws Exception
  {
    try (Scanner lectorUsuario=new Scanner(System.in);)
    {
      System.out.print("Direcci�n IP servidor: ");
      String direccionIPServidor=lectorUsuario.nextLine().trim();
      System.out.print("Puerto: ");
      int puertoServidor=Integer.parseInt(lectorUsuario.nextLine().trim());
      System.out.print("Archivo: ");
      String archivo=lectorUsuario.nextLine().trim();
      if (direccionIPServidor.isEmpty()) direccionIPServidor="localhost";
      convertirVideo(direccionIPServidor,puertoServidor,archivo,true);
    }
  }

  public static void convertirVideo(String direccionIPServidor, int puertoServidor, String archivo, boolean debeEscribirVideoResultado) throws Exception
  {
    if (!archivo.endsWith(".avi")) throw new Exception("El archivo debe ser .avi");
    
    //Socket socket=new Socket(direccionIPServidor,puertoServidor);
    SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket("localhost", 9999);
    
    try (ObjectOutputStream salida=new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream entrada=new ObjectInputStream(socket.getInputStream());)
    {
      byte[] buffer=new byte[1024];
      FileInputStream fileInputStream=new FileInputStream(archivo);
      while (true)
      {
        int c=fileInputStream.read(buffer);
        if (c==-1) break;
        salida.writeObject(Arrays.copyOf(buffer,c));
      }
      salida.writeObject(null);
      fileInputStream.close();
      if (debeEscribirVideoResultado)
      {
        String archivoResultado=archivo.replaceAll("\\.avi$",".mp4");
        FileOutputStream fileOutputStream=new FileOutputStream(archivoResultado);
        while (true)
        {
          byte[] fragmento=(byte[])(entrada.readObject());
          if (fragmento==null) break;
          fileOutputStream.write(fragmento);
        }
        fileOutputStream.close();
      }
      else
      {
        while (true)
        {
          byte[] fragmento=(byte[])(entrada.readObject());
          if (fragmento==null) break;
        }
      }
    }
  }

}

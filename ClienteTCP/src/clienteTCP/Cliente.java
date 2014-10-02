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
      convertirVideo(direccionIPServidor,puertoServidor,archivo);
    }
  }

  public static void convertirVideo(String direccionIPServidor, int puertoServidor, String archivo) throws Exception
  {
	  if (!archivo.endsWith(".avi")) throw new Exception("El archivo debe ser .avi");
	    
	    //Socket socket=new Socket(direccionIPServidor,puertoServidor);
	    SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
	    SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket("localhost", 9999);
	    
	    try (ObjectOutputStream salida=new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream entrada=new ObjectInputStream(socket.getInputStream());)
	    {
	      byte[] contenidoVideoOriginal=Files.readAllBytes(Paths.get(archivo));
	      salida.writeObject(contenidoVideoOriginal);
	      byte[] contenidoVideoConvertido=(byte[])(entrada.readObject());
	      String archivoResultado=archivo.replaceAll("\\.avi$",".mp4");
	      Files.write(Paths.get(archivoResultado),contenidoVideoConvertido);
	    }
  }

}

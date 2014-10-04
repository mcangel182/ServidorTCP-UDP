package clienteTCP;

public class Pruebas
{
  public static int numeroClientes=300;
  public static void main(String[] args)
  {
    for (int i=0; i<numeroClientes; i++)
    {
      Thread t=new Thread()
      {
        public void run()
        {
          String direccionIPServidor="localhost";
          int puertoServidor=9999;
          String archivo="/Users/Maca/Desktop/videos/fish.avi";
          try
          {
            Cliente.convertirVideo(direccionIPServidor,puertoServidor,archivo);
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }
        }
      };
      t.start();
    }

  }
}

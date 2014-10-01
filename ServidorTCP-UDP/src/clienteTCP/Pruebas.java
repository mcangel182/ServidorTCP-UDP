package clienteTCP;

public class Pruebas
{
  public static int numeroClientes=4;
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
          String archivo="C:/pruebas/video.avi";
          try
          {
            Cliente.convertirVideo(direccionIPServidor,puertoServidor,archivo,false);
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

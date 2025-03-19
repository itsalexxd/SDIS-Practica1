package sdis.broker.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class clienteInteractivo {
    public static final int PUERTO = 2000;

    public static void main(String[] args) {
        String welcome = null;
        String mensaje = null;
        String username = null;
        String password = null;
        String comprobacion = null;
        boolean logeado = false;
        String linea;

        try{
            java.io.BufferedReader teclado = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            java.net.Socket miSocket = new java.net.Socket("localhost", PUERTO);

            java.io.BufferedReader entradaServer = new java.io.BufferedReader(new java.io.InputStreamReader(miSocket.getInputStream()));
            java.io.PrintStream salidaServer = new java.io.PrintStream(miSocket.getOutputStream());


            welcome = entradaServer.readLine();
            System.out.println(welcome);

            while(logeado == false) {

                username = teclado.readLine();

                salidaServer.println(username);

                mensaje = entradaServer.readLine();

                System.out.println(mensaje);

                password = teclado.readLine();

                salidaServer.println(password);

                comprobacion = entradaServer.readLine();

                System.out.println(comprobacion);

                if(comprobacion.equals("User successfully logged in")){

                    logeado = true;
                }

            }



            while((linea = teclado.readLine()) != null){
                salidaServer.println(linea);
                linea = entradaServer.readLine();
                System.out.println("Se ha enviado la linea: " + linea);

            }

        }catch(Exception e){
            e.printStackTrace();
        }// Fin primer try
    }
}

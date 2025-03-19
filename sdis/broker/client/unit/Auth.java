package sdis.broker.client.unit;

import java.io.*;
import java.net.*;

import sdis.broker.common.*;

public class Auth {
    private static final String SERVIDOR = "localhost";
    final private int PUERTO = 2000;

    public static boolean autenticar(String usuario, String clave){
        try(Socket socket = new Socket(SERVIDOR, PUERTO));
        BufferedReader inClinete = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
        PrintWriter outCliente = new PrintWriter(socket.getOutputStream(), true) {

            // XAUTH: Usuario:
                System.out.println(inCliente.readLine());
                outCliente.println(usuario);

            // XAUTH: Password:
                System.out.println(inCliente.readLine());
            outCliente(password);

            String respuesta = inCliente.readLine();
                System.out.println(respuesta);

                return respuesta.contains("exitosa");

        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

}

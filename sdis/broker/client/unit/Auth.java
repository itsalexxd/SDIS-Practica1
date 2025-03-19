package sdis.broker.client.unit;

import java.io.*;
import java.net.*;

import sdis.broker.common.*;

public class Auth {
    private static final String SERVIDOR = "localhost";
    private static final int PUERTO = 2000;

    public static boolean autenticar(String usuario, String password){
        try(Socket socket = new Socket(SERVIDOR, PUERTO)){

            // Recogemos la entrada y salida por parte del usuario
            BufferedReader inCliente = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outCliente = new PrintWriter(socket.getOutputStream(), true);

            // XAUTH: Usuario:
                System.out.println(inCliente.readLine());
                outCliente.println(usuario);

            // XAUTH: Password:
                System.out.println(inCliente.readLine());
                outCliente.println(password);

                String respuesta = inCliente.readLine();
                System.out.println(respuesta);

                return respuesta.contains("exitosa");

        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

}

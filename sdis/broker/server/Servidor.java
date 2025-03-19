package sdis.broker.server;

import sdis.utils.MultiMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Servidor {

    private static final int PUERTO = 2000;
    private static final int MAX_CLIENTES = 5;
    private static final int MAX_INTENTOS = 3;
    private static final int MAX_CONEXIONES_IP = 3;

    private static final ConcurrentHashMap<String, String> user_password = new ConcurrentHashMap<>();

    private static BlackListManager blacklistLogin = new BlackListManager();
    private static BlackListManager blacklistConexion = new BlackListManager();

    public static void main(String[] args){
        // Agregamos los usuarios y passwords al HashMap
        user_password.put("cllamas", "qwerty");
        user_password.put("hector", "lkjlkj");
        user_password.put("sdis", "987123");
        user_password.put("admin", "$%&/()=");

        // Maximo 5 hilos (uno para cada cliente)
        int NThreads = 5;

        // MultiMap
        MultiMap<String, String> mapa = new MultiMap<>();

        // Executor
        ExecutorService exec = Executors.newFixedThreadPool(NThreads);

        try {
            // Creamos el socket del servidor
            ServerSocket servidor  = new ServerSocket(PUERTO);

            // Mostramos por pantalla
            System.out.println("Servidor: WHILE [INICIANDO]");

            // Creamos un hilo por cada cliente que se quiera conectar
            Thread mainServer = new Thread(() -> {
                try{
                    // Bucle infinito para que el cliente inicie sesion
                    while(true){
                        // Cliente conectado correctamente
                        Socket socket = servidor.accept();
                        String ipCliente = socket.getRemoteSocketAddress().toString();

                        // Mostramos la conexion del cliente
                        System.out.println("CLIENTE CONECTADO" + socket.getRemoteSocketAddress());

                        // Recogemos los streams del cliente
                        BufferedReader inCliente = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintStream outCliente = new PrintStream(socket.getOutputStream());


                        // Incrementamos el contador de conexiones para la IP
                        blacklistConexion.registrarIntento(ipCliente);

                        //Si la ip lleva 3 conexiones, no deja seguir conectandose
                        if(blacklistConexion.getIntentos().get(ipCliente) < 3){

                            // Mostramos por pantalla al cliente un mensaje de bienvenida y que debe hacer
                            outCliente.println("Welcome, please type your credentials to LOG IN");

                            //Llama para hacer el login
                            boolean errorLogin = manejoClientes(user_password, inCliente, outCliente, ipCliente, blacklistLogin);

                            if(!errorLogin){

                                String linea;

                                // Bucle para el echo del server
                                while((linea = inCliente.readLine())!=null){
                                    System.out.println(linea);
                                    outCliente.println(linea);
                                }


                                //try{
                                // sdis.broker.server.Sirviente serv = new sdis.broker.server.Sirviente(socket, mapa);
                                // exec.execute(serv);

                                // } catch (java.io.IOException ioe) {
                                // System.err.println("Servidor: WHILE [Error.E/S]");
                                // }


                            }else{

                                //Mensaje de superar el numero de logins
                                outCliente.println("Err Max Number of login attempts reached");

                            }



                        }else{

                            //Mensaje de superar el numero de conexioens
                            outCliente.println("Err Max Number of connections reached");

                        }


                    }

                }catch (IOException e){
                    System.err.println("Cerrando socket del cliente");
                    e.printStackTrace(System.err);
                }

            }, "RUN (WHILE)"); // fin-newThread separado para el servidor
            mainServer.start();
            System.out.println("Servidor: [CORRIENDO]");
        }catch (IOException ioe){
            System.err.println("Servidor: [ERR SOKET]");
        }

    }



    public static boolean manejoClientes(ConcurrentHashMap<String, String> user_password, BufferedReader inCliente, PrintStream outCliente, String ipCliente, BlackListManager blacklistLogin) {

        int intentos = 0;   // Máximo -> 3 intentos
        String username = "";
        String password = "";

        // Incrementamos el contador de intentos fallidos para la IP
        blacklistLogin.registrarIntento(ipCliente);


        if(blacklistLogin.getIntentos().get(ipCliente) < 3){

            while(intentos < 3) {

                try {   // Recogemos el usuario insertado por el cliente

                    username = inCliente.readLine();
                    outCliente.println("OK: password?");
                    password = inCliente.readLine();

                }catch (IOException e){

                    outCliente.println("Error recogiendo los datos");

                }


                // Comprobamos si los datos insertados son validos
                if (user_password.containsKey(username) && user_password.get(username).equals(password)) {
                    // Si lo son, mostramos por pantalla que se ha iniciado sesion correctamente y salimos del bucle
                    outCliente.println("User successfully logged in");
                    return false;
                }
                // Si no se ha logueado correctamente, mostramos mensaje de error por su pantalla
                outCliente.println("Credentials does not match our records.     Enter username again: ");
                // Incrementamos el contador de intentos fallidos para la IP
                blacklistLogin.registrarIntento(user_password.get(ipCliente));

                intentos ++;
            }

            // Si sale del bucle, intentos maximos superados
            return true;


        }else{

            return true;


        }


    }


}


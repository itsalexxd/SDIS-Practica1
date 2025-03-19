package sdis.broker.server;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class BlackListManager {


    private final ConcurrentHashMap<String, Integer> intentos;

    public BlackListManager() {

        intentos = new ConcurrentHashMap<>();

    }


    public ConcurrentHashMap<String, Integer> getIntentos() {

        return intentos;

    }

    public void registrarIntento(String ip){

        intentos.merge(ip, 1, Integer::sum);

    }

}

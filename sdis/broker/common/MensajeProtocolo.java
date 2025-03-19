package sdis.broker.common;

import sdis.broker.client.unit.Auth;

public class MensajeProtocoloProvisional {
    private final Primitiva primitiva;
    private final String mensaje; /* HELLO, PUSH, PULL_OK */
    private final String idCola;  /* PUSH, PULL_WAIT, PULL_NOWAIT */

    /* Constructor para PUSH_OK, NOTHING, NOTUNDERSTAND */
    /* ADDED(PUSH_OK) */
    public MensajeProtocoloProvisional(Primitiva p) throws MalMensajeProtocoloException {
        if (p == Primitiva.ADDED) {
            this.primitiva = p;
            this.mensaje = this.idCola = null;
        } else
            throw new MalMensajeProtocoloException();
    }

    /* Constructor para HELLO, PULL_OK, PULL_WAIT, PULL_NOWAIT */
    /* XAUTH,  */
    public MensajeProtocoloProvisional(Primitiva p, String mensaje) throws MalMensajeProtocoloException {
        if (p == Primitiva.XAUTH || p == Primitiva.PULL_OK) {
            this.mensaje = mensaje;
            this.idCola  = null;
        } else if (p == Primitiva.PULL_WAIT || p == Primitiva.PULL_NOWAIT) {
            this.idCola  = mensaje;
            this.mensaje = null;
        } else
            throw new MalMensajeProtocoloException();
        this.primitiva = p;
    }

    /* Constructor para ADDMSG */
    public MensajeProtocoloProvisional(Primitiva p, String idCola, String mensaje) throws MalMensajeProtocoloException {
        if (p == Primitiva.ADDMSG) {
            this.primitiva = p;
            this.mensaje = mensaje;
            this.idCola = idCola;
        } else
            throw new MalMensajeProtocoloException();
    }

    public Primitiva getPrimitiva() { return this.primitiva; }
    public String getMensaje() { return this.mensaje; }
    public String getIdCola() { return this.idCola; }

    public String toString() { /* prettyPrinter de la clase */
        switch (this.primitiva) {
            // Da la bienvenida al usuario
            case Primitiva.INFO:
                return "Welcome, please type yuor credentials to LOG in";

            // cliente solicita al servidor que añada un mensaje de texto val a la cola key
            case Primitiva.ADDMSG:
                return String.format("Welcome to %s: %s", this.primitiva, this.mensaje);

            // Autentica el inicio de sesion del usuario
            case Primitiva.XAUTH:
                return Auth.autenticar(usuario,password) ? "OK" : "ERROR";

            // En caso de que la primitiva no este definida
            default :
                return this.primitiva+" NO SOPORTADA";
        }
    }
}

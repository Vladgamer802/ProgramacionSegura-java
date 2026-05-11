import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.Base64;

public class ReceptorSeguro {

    public static void main(String[] args) throws Exception {

        //Se conecta a servidor
        Socket conexion = new Socket("localhost", 6000);
        DataInputStream in = new DataInputStream(conexion.getInputStream());

        //Se reciben los datos cifrados
        int tamañoClave = in.readInt();
        byte [] claveBytes = new byte[tamañoClave];
        in.readFully(claveBytes);
        
        int tamañoMensaje = in.readInt();
        byte[] mensajeCifrado = new byte[tamañoMensaje];
        in.readFully(mensajeCifrado);


        SecretKey claveRecibida = new SecretKeySpec(claveBytes, "AES");
        //Se descifra el mensaje
        Cipher cifrador = Cipher.getInstance("AES");
        cifrador.init(Cipher.DECRYPT_MODE, claveRecibida);

        byte[] mensajeDescifrado = cifrador.doFinal(mensajeCifrado);
        String cadenaMensaje = new String(mensajeDescifrado);

        System.out.println("Clave recibida: " + Base64.getEncoder().encodeToString(claveBytes));
        System.out.println("Mensaje cifrado recibido: " + Base64.getEncoder().encodeToString(mensajeCifrado));
        System.out.println("Mensaje recibido: " + cadenaMensaje);


    }
}

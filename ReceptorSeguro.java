import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.Base64;

public class ReceptorSeguro {

    public static void main(String[] args) throws Exception {

        // 1. Conectarse al emisor
        Socket socket = new Socket("localhost", 6000);
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // 2. Leer clave
        int tamClave = in.readInt();
        byte[] claveBytes = new byte[tamClave];
        in.readFully(claveBytes);

        System.out.println("Clave recibida (Base64): " +
                Base64.getEncoder().encodeToString(claveBytes));

        // Reconstruir clave AES
        SecretKey clave = new SecretKeySpec(claveBytes, "AES");

        // 3. Leer mensaje cifrado
        int tamMensaje = in.readInt();
        byte[] mensajeCifrado = new byte[tamMensaje];
        in.readFully(mensajeCifrado);

        System.out.println("Mensaje cifrado recibido (Base64): " +
                Base64.getEncoder().encodeToString(mensajeCifrado));

        // 4. Descifrar mensaje
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, clave);
        byte[] mensajeDescifrado = cipher.doFinal(mensajeCifrado);

        System.out.println("Mensaje descifrado: " +
                new String(mensajeDescifrado));

        in.close();
        socket.close();
    }
}

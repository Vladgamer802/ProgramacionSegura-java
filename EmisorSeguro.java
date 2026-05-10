import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

public class EmisorSeguro {

    public static void main(String[] args) throws Exception {

        // 1. Generar clave AES de 128 bits
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey clave = keyGen.generateKey();

        System.out.println("Clave generada (Base64): " +
                Base64.getEncoder().encodeToString(clave.getEncoded()));

        // 2. Texto a cifrar
        String mensaje = "La reunión empieza a las 10:30";

        // 3. Cifrar mensaje
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, clave);
        byte[] mensajeCifrado = cipher.doFinal(mensaje.getBytes());

        System.out.println("Mensaje cifrado (Base64): " +
                Base64.getEncoder().encodeToString(mensajeCifrado));

        // 4. Abrir ServerSocket y enviar clave + mensaje cifrado
        ServerSocket server = new ServerSocket(6000);
        System.out.println("Esperando conexión del receptor...");

        Socket socket = server.accept();
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Enviar clave
        out.writeInt(clave.getEncoded().length);
        out.write(clave.getEncoded());

        // Enviar mensaje cifrado
        out.writeInt(mensajeCifrado.length);
        out.write(mensajeCifrado);

        System.out.println("Datos enviados correctamente.");

        out.close();
        socket.close();
        server.close();
    }
}

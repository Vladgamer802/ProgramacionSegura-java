import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

public class EmisorSeguro {

    public static void main(String[] args) throws Exception {

        //Generar clave simetrica AES de 128 bits
        KeyGenerator generador = KeyGenerator.getInstance("AES");
        generador.init(128);
        SecretKey clavePrivada = generador.generateKey();

        Cipher cifrador = Cipher.getInstance("AES");
        cifrador.init(Cipher.ENCRYPT_MODE, clavePrivada);

        //Se declara el mensaje cifrado
        String mensaje = "La reunion empieza a las 10:30";
        byte[] mensajeBytes = mensaje.getBytes();
        byte[] mensajeCifrado = cifrador.doFinal(mensajeBytes);
        byte[] claveBytes = clavePrivada.getEncoded();

        ServerSocket servidor = new ServerSocket(6000);
        System.out.println("Servidor esperando conexion en puerto 6000");

        Socket conexion = servidor.accept();
        DataOutputStream out = new DataOutputStream(conexion.getOutputStream());
        
        
        out.writeInt(clavePrivada.getEncoded().length);
        out.write(clavePrivada.getEncoded());

        out.writeInt(mensajeCifrado.length);
        out.write(mensajeCifrado);

        System.out.println("Datos enviados correctamente");

        out.close();
        conexion.close();
        servidor.close();

    }

}

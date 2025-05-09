import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

//El recibidor (Receiver)
public class Receiver {
    static int PORT = 19000;

    public static void main(String[] args) {
        try {
            SSLSocketFactory factory = getSocketFactory();
            SSLSocket socket = (SSLSocket) factory.createSocket("localhost", PORT);
            System.out.println("Estableciendo la conexion completamente segura");

            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            while (true) {
                Persona p = (Persona) ois.readObject();
                System.out.println("Recibido: " + p.getFullname() + ", Edad: " + p.getAge() + ", UUID: " + p.getUuid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SSLSocketFactory getSocketFactory() throws Exception {
        char[] storePassword = "password".toCharArray();

        File file = new File("LaClave.jks");
        if (!file.exists()) {
            //No deberia existir el problema de las carpetas que solo funcionan en mi laptop
            throw new FileNotFoundException("No se encontró 'LaClave.jks' en: " + new File(".").getAbsolutePath());
        }

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(file), storePassword);

        //Segun lei esto es solo el "Estandar" como algoritmo para estos casos
        //Existe el PKIX pero mejor no me arriesgo si ya funciona
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, tmf.getTrustManagers(), null);
        return ctx.getSocketFactory();
    }
}

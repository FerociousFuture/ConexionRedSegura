import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Random;
import java.util.UUID;

//El mandador (Sender)
public class Sender {
    //Puerto
    static int PORT = 19000;

    //Honestamente esa parte me gustaria verla mas en clase
    //La seguridad y eso en programación

    public static void main(String[] args) throws IOException {
        SocketUtils utils = new SocketUtils();
        Random random = new Random();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        //Podria haber sido con una lista de nombres pero tampoco es tan importante
        while (true) {
            Persona p = new Persona();
            p.setFullname("Generar Nombre (ID): " + random.nextInt(1000));
            p.setAge(random.nextInt(55));
            p.setUuid(UUID.randomUUID().toString());

            oos.writeObject(p);
            oos.flush();

            byte[] bytes = baos.toByteArray();
            System.out.println("Enviando objeto con UUID: " + p.getUuid());
            utils.Send(bytes);

            baos.reset();
        }
    }

    private static class SocketUtils {
        private final Socket socket;

        SocketUtils() {
            try {
                SSLServerSocketFactory factory = getServerSocketFactory();
                SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(PORT);
                System.out.println("Esperando la conexion completamente segura (Claro");
                socket = serverSocket.accept();
                System.out.println("Cliente conectado de forma segura");
            } catch (Exception e) {
                throw new RuntimeException("Error al establecer el servidor", e);
            }
        }

        void Send(byte[] data) throws IOException {
            OutputStream os = socket.getOutputStream();
            os.write(data);
            os.flush();
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }

        private static SSLServerSocketFactory getServerSocketFactory() throws Exception {
            //Iba a poner contraseña "Contraseña" pero mejor dejarlo diferente
            char[] storePassword = "password".toCharArray();
            char[] keyPassword = "Paradigmas".toCharArray();

            File file = new File("LaClave.jks");
            if (!file.exists()) {
                throw new FileNotFoundException("No se encontró 'LaClave.jks' en: " + new File(".").getAbsolutePath());
            }

            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(file), storePassword);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, keyPassword);

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(kmf.getKeyManagers(), null, null);
            return ctx.getServerSocketFactory();
        }
    }
}

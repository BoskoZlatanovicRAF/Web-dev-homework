import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static final int PORT = 9001;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(Main.PORT);
            while (true) {
                System.out.println("Server ocekujem konekciju...");
                Socket socket = serverSocket.accept();
                System.out.println("Klijent se povezao!");
                Thread thread = new Thread(new ServerThread(socket));
                thread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("Server je zavrsio sa radom!");
            serverSocket.close();
        }
        


    }
}

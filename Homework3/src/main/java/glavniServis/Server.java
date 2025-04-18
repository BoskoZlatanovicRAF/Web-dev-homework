package glavniServis;

import service.QuotesService;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 8080;

    public static void main(String[] args) {
        QuotesService quotesService = new QuotesService();
        try{
            ServerSocket ss = new ServerSocket(PORT);
            while(true){
                Socket sock = ss.accept();
                new Thread(new ServerThread(sock, quotesService)).start();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

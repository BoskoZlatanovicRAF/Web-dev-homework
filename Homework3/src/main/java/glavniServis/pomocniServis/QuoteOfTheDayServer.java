package glavniServis.pomocniServis;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import model.Quote;

public class QuoteOfTheDayServer {

    public static final int PORT = 8081;
    private ServerSocket serverSocket;
    private List<Quote> quotes;

    public QuoteOfTheDayServer() throws IOException{
        serverSocket = new ServerSocket(PORT);
        quotes = new ArrayList<>();
        quotes.add(new Quote("Albert Einstein", "Life is like riding a bicycle. To keep your balance you must keep moving."));
        quotes.add(new Quote("Nikola Tesla", "The present is theirs; the future, for which I really worked, is mine."));
        quotes.add(new Quote("Isaac Newton", "If I have seen further it is by standing on the shoulders of Giants."));
        quotes.add(new Quote("Marie Curie", "Nothing in life is to be feared, it is only to be understood."));
        quotes.add(new Quote("Charles Darwin", "It is not the strongest of the species that survive, nor the most intelligent, but the one most responsive to change."));
    }

    public void start() throws IOException{
        while(true){
            try{
                Socket socket = serverSocket.accept();
                new Thread(new QuoteOfTheDayServerThread(socket, quotes)).start();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        QuoteOfTheDayServer server = new QuoteOfTheDayServer();
        server.start();
    }

}

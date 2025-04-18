package glavniServis.pomocniServis;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;

import model.Quote;

public class QuoteOfTheDayServerThread implements Runnable {
    private Socket socket;
    private List<Quote> quotes;
    private Gson gson;

    public QuoteOfTheDayServerThread(Socket socket, List<Quote> quotes) {
        this.socket = socket;
        this.quotes = quotes;
        this.gson = new Gson();
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            Quote quote = quotes.get((int) (Math.random() * quotes.size()));
            String json = gson.toJson(quote);
            out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

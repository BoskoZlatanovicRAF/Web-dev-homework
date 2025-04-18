package glavniServis;

import controller.RequestHandler;
import http.HttpMethod;
import http.request.Request;
import http.response.Response;
import model.Quote;
import service.QuotesService;

import java.io.*;
import java.net.Socket;

import com.google.gson.Gson;

public class ServerThread implements Runnable{

    private final Socket client;

    private BufferedReader in;

    private PrintWriter out;

    private final QuotesService quotesService;


    public ServerThread(Socket sock, QuotesService quotesService){
        this.client = sock;
        this.quotesService = quotesService;


        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Quote getQuoteOfTheDay() throws IOException {
        try (Socket quoteSocket = new Socket("localhost", 8081)) {
            BufferedReader quoteIn = new BufferedReader(new InputStreamReader(quoteSocket.getInputStream()));
            String json = quoteIn.readLine();
            return new Gson().fromJson(json, Quote.class);
        }
    }

    @Override
    public void run() {
        try{
            String requestLine = in.readLine();

            // na primer GET /quotes HTTP/1.1
            String method = requestLine.split(" ")[0];
            String path = requestLine.split(" ")[1];

            System.out.println("\nHTTP ZAHTEV KLIJENTA:\n");

            // ovo radimo sve dok ne dodjemo do praznog reda, odnosno /r/n
            do {
                System.out.println(requestLine);
                requestLine = in.readLine();
            } while (!requestLine.trim().equals(""));

            // citamo telo za POST metodu
            // na primer {"quote":"Ovo je neki citat","author":"Ja"}
            StringBuilder body = new StringBuilder();
            if (method.equals(HttpMethod.POST.toString())) {
                char[] buffer = new char[1024];
                in.read(buffer);
                body.append(new String(buffer).trim());
            }

            Request request = new Request(HttpMethod.valueOf(method), path, body.toString());


            RequestHandler requestHandler = new RequestHandler(quotesService);
            Response response = requestHandler.handle(request);
            

            System.out.println("\nHTTP odgovor:\n");
            System.out.println(response.getResponseString());

            out.println(response.getResponseString());

            in.close();
            out.close();
            client.close();


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class ServerThread implements Runnable{
    private Socket socket;
    private PrintWriter out;
    private String username;

    private static ConcurrentHashMap<String, PrintWriter> activeUsers = new ConcurrentHashMap<>();
    private Set<String> censoredWords = new HashSet<>(Arrays.asList("Zdravo", "Eee", "Cao"));
    private static ConcurrentLinkedQueue<String> messageHistory = new ConcurrentLinkedQueue<>();
    
    public ServerThread(Socket socket){
        this.socket = socket;
    }
    
    @Override
    public void run() {
        BufferedReader in = null;
        

        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            username = in.readLine();
            while (activeUsers.containsKey(username)) {
                out.println("Username already in use. Please connect again with a different username.");
                username = in.readLine();
            }

            activeUsers.put(username, out);
            out.println("Welcome " + username + "!"); // poruka dobrodoslice    

            for (PrintWriter writer : activeUsers.values()) { // obavestavanje svih klijenata o dolasku novog
                if (writer != out) {
                    writer.println(username + " has joined the chat.");
                }
            }

            for (String message : messageHistory) { // slanje istorije poruka
                out.println(message);
            }

            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }
                for (String word : censoredWords) {
                    if (message.contains(word)) {
                        message = message.replaceAll(word, word.charAt(0) + "*".repeat(word.length() - 2) + word.charAt(word.length() - 1));
                    }
                }
                messageHistory.add(username + ": " + message);
                if (messageHistory.size() > 100) {
                    messageHistory.poll();
                }
                for (PrintWriter writer : activeUsers.values()) {
                    writer.println(username + ": " + message);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
       } finally {
            activeUsers.remove(username);
            out.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

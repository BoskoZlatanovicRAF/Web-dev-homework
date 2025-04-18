import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 9001);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

            while(true){
                System.out.print("Enter your username: ");
                String username = userIn.readLine();
                out.println(username); // saljem username serveru da bi ga on proverio
                String response = in.readLine();
                if(response.equals("Welcome " + username + "!")){ // ako je username validan, izlazim iz petlje
                    System.out.println(response);
                    break;
                }
                System.out.println(response);
            }


            Thread readThread = new Thread(() -> { //thread koji cita sa servera poruke
                try {
                    while (true) {
                        String message = in.readLine();
                        if (message == null) {
                            break;
                        }
                        System.out.println(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            readThread.start();

            while (true) {  // client salje poruke serveru, koji ce kasnije proslediti poruku svim userima
                String message = userIn.readLine();
                out.println(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
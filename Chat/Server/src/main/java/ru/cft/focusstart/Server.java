package ru.cft.focusstart;

import ru.cft.focusstart.dto.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    static List<Client> potentialClients;
    static List<Client> clients;
    static Users users;

    public static void main(String[] args) throws IOException {
        //TODO: не забыть брать порт из ресурсов
        ServerSocket serverSocket = new ServerSocket(1111);

        potentialClients = new ArrayList<>();
        clients = new ArrayList<>();
        users = new Users();


        runConnectionsListener();
        runMessageListener();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                serverSocket.close();
                for (Client clientItem : clients) {
                    clientItem.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }));

        //Подключение новых клиентов
        while (true) {
            Socket clientSocket = serverSocket.accept();
            potentialClients.add(new Client(clientSocket));
        }
    }

    private static void runConnectionsListener() {
        Thread connectListenerThread = new Thread(() -> {
            boolean interrupted = false;
            while (!interrupted) {
                try {
                    String message = null;
                    Client client = null;
                    for (Client clientItem : potentialClients) {
                        if (clientItem.getReader().ready()) {
                            client = clientItem;
                            message = clientItem.getReader().readLine();
                            break;
                        }
                    }

                    if (message != null) {
                        Communication communication = Serialization.fromJson(message);
                        if (communication.getClass().getName() == User.class.getName()) {
                            User user = (User) communication;
                            ServerMessage serverMessage;
                            if (users.getUsers().contains(user)) {
                                serverMessage = new ServerMessage("ERROR", ServerEvent.ERROR);
                            } else {
                                serverMessage = new ServerMessage("GOOD", ServerEvent.SUCCESS);
                                potentialClients.remove(client);
                                client.setUser(user);
                                clients.add(client);
                                users.addUser(user);
                            }
                            client.sendMessage(Serialization.toJson(serverMessage));
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        });
        connectListenerThread.start();
    }


    private static void runMessageListener() {
        Thread messageListenerThread = new Thread(() -> {
            boolean interrupted = false;
            while (!interrupted) {
                try {
                    String message = null;
                    for (Client clientItem : clients) {
                        if (clientItem.getReader().ready()) {
                            message = clientItem.getReader().readLine();
                            break;
                        }
                    }

                    if (message != null) {
                        System.out.println("new message received: " + message);
                        for (Client clientItem : clients) {
                            clientItem.sendMessage(message);
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        });
        messageListenerThread.start();
    }
}


package vsbserv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author Vitor - Savio - Brenner
 */
public class VSBserv extends Thread {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        clientes = new Vector();
        try {
            ServerSocket s = new ServerSocket(5000);

            while (true) {
                System.out.println("Esperando alguem se conectar...");
                Socket conexao = s.accept();
                System.out.println("Alguém entrou no chat!");

                Thread t = new VSBserv(conexao);
                t.start();
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    private static Vector clientes;
    private Socket conexao;
    private String meuNome;

    public VSBserv(Socket s) {
        conexao = s;
    }

    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            PrintStream saida = new PrintStream(conexao.getOutputStream());

            meuNome = entrada.readLine();
            if (meuNome == null) {
                return;
            }
            clientes.add(saida);
            String linha = entrada.readLine();
            while (linha != null && !(linha.trim().equals(""))) {
                sendToAll(saida, " disse: ", linha);
                linha = entrada.readLine();
            }
            sendToAll(saida, " saiu ", "do chat!");
            clientes.remove(saida);
            conexao.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    public void sendToAll(PrintStream saida, String acao, String linha) throws IOException {
        // Caminho do txt
        File f = new File("C:/Users/Brenn/Documents/Code/Unifei/Paralelos/sistemas-paralelos-chat/messages.txt");

        FileWriter fw = new FileWriter(f, true);

        PrintWriter pw = new PrintWriter(fw);

        Enumeration e = clientes.elements();
        while (e.hasMoreElements()) {
            PrintStream chat = (PrintStream) e.nextElement();
            if (chat != saida) {
                // realmente escreve no arquivo
                pw.println(meuNome + acao + linha);

                // fecha a conexão do arquivo
                fw.close();
                chat.println(meuNome + acao + linha);
            }
        }
    }
}
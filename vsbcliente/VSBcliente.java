
package vsbcliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Vitor - Sávio - Brenner
 */
public class VSBcliente extends Thread {

    public static boolean done = false;

    public static void main(String[] args) {
        try {

            Socket conexao = new Socket("127.0.0.1", 5000);

            PrintStream saida = new PrintStream(conexao.getOutputStream());
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Entre com o seu nome: ");
            String meuNome = teclado.readLine();
            saida.println(meuNome);

            Thread t = new VSBcliente(conexao);
            t.start();

            String linha;
            while (true) {
                System.out.println("> ");
                linha = teclado.readLine();
                if (done) {
                    break;
                }
                saida.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Exception do main");
            System.out.println("IOException: " + e);
        }
    }

    private Socket conexao;

    public VSBcliente(Socket s) {
        conexao = s;
    }

    // execução da thread
    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            String linha;
            while (true) {
                linha = entrada.readLine();

                if (linha == null) {
                    System.out.println("Conexão encerrada!");
                    break;
                }
                System.out.println();
                System.out.println(linha);
               System.out.println("---> ");
            }
        } catch (IOException e) {
            System.out.println("Exception do run");
            System.out.println("IOException> " + e);
        }
        done = true;
    }
}
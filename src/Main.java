
import Criptografia.AES;
import Criptografia.Chaves;
import Criptografia.Vernam;
import Gateway.GatewayInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Main {
    static GatewayInterface stub;
    static String ipCliente = "5557";

    public static void main(String[] args) throws Exception {

        try {
            Registry registro = LocateRegistry.getRegistry("localhost", 6002);
            stub = (GatewayInterface) registro.lookup("Gateway");

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        boolean parar = false;

        Chaves chave = new Chaves();
        chave.CHAVE_AES = "achoquenaovai123";
        chave.CHAVE_VERNAM = "achoquenaovai123";


        stub.receberChave(ipCliente, chave);
        while (!parar) {
            System.out.println("Bem vindo, você está conectado a porta: " + ipCliente + ".");
            System.out.println("Escolha uma opção: ");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Logar");
            System.out.println("3 - Sacar");
            System.out.println("4 - Depositar");
            System.out.println("5 - Transferir");
            System.out.println("6 - Saldo");
            System.out.println("7 - Investir na Poupança");
            System.out.println("8 - Investir na Renda Fixa");
            System.out.println("9 - Sair");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("Digite o nome: ");
                    String nome = scanner.nextLine();
                    System.out.println("Digite o CPF: ");
                    String cpf = scanner.nextLine();
                    System.out.println("Digite a senha: ");
                    String senha = scanner.nextLine();
                    System.out.println("Digite o email: ");
                    String email = scanner.nextLine();
                    String saldo = "0.0";

                    String dados = nome + "-" + cpf + "-" + senha + "-" + email + "-" + saldo;
                    String dadosCriptografados = Vernam.cifrar(dados, chave.CHAVE_AES);
                    dadosCriptografados = AES.cifrar(dadosCriptografados, chave.CHAVE_VERNAM);

                    try {
                        stub.cadastro(ipCliente, dadosCriptografados);
                        System.out.println("Dados: " + dados);
                        System.out.println("Dados criptografados: " + dadosCriptografados);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2:
                    System.out.println("Digite o CPF: ");
                    cpf = scanner.nextLine();
                    System.out.println("Digite a senha: ");
                    senha = scanner.nextLine();

                    String cpfCriptografado = Vernam.cifrar(cpf, chave.CHAVE_AES);
                    cpfCriptografado = AES.cifrar(cpfCriptografado, chave.CHAVE_VERNAM);
                    String senhaCriptografada = Vernam.cifrar(senha, chave.CHAVE_AES);
                    senhaCriptografada = AES.cifrar(senhaCriptografada, chave.CHAVE_VERNAM);

                    try {
                        stub.logar(ipCliente, cpfCriptografado, senhaCriptografada);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3:
                    System.out.println("Digite o CPF: ");
                    cpf = scanner.nextLine();
                    System.out.println("Digite o valor: ");
                    String valor = scanner.nextLine();

                    cpfCriptografado = Vernam.cifrar(cpf, chave.CHAVE_AES);
                    cpfCriptografado = AES.cifrar(cpfCriptografado, chave.CHAVE_VERNAM);
                    String valorCriptografado = Vernam.cifrar(valor, chave.CHAVE_AES);
                    valorCriptografado = AES.cifrar(valorCriptografado, chave.CHAVE_VERNAM);

                    try {
                        stub.sacar(ipCliente, cpfCriptografado, valorCriptografado);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 4:
                    System.out.println("Digite o CPF: ");
                    cpf = scanner.nextLine();
                    System.out.println("Digite o valor: ");
                    valor = scanner.nextLine();
                    try {
                        stub.depositar(ipCliente, cpf, valor);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 5:
                    System.out.println("Digite a conta de origem: ");
                    String contaOrigem = scanner.nextLine();
                    System.out.println("Digite a conta de destino: ");
                    String contaDestino = scanner.nextLine();
                    System.out.println("Digite o valor: ");
                    valor = scanner.nextLine();
                    try {
                        stub.transferir(ipCliente, contaOrigem, contaDestino, valor);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 6:
                    System.out.println("Digite o CPF: ");
                    cpf = scanner.nextLine();
                    try {
                        stub.getSaldo(ipCliente, cpf);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 7:
                    System.out.println("Digite o CPF: ");
                    cpf = scanner.nextLine();
                    System.out.println("Digite o valor: ");
                    valor = scanner.nextLine();
                    try {
                        stub.investirPoupanca(" ", cpf, valor);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 8:
                    System.out.println("Digite o CPF: ");
                    cpf = scanner.nextLine();
            }
        }
    }
}
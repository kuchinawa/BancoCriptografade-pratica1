package Conta;

import model.Usuario;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Banco  implements BancoInterface {
    private static final String ARQUIVO_DADOS = "dados_banco.txt";
    public Map<String, Usuario> contas;

    public Banco() throws RemoteException {
        contas = new HashMap<>();
        carregarDados();
    }

    @Override
    public Usuario autenticar(String cpf, String senha) throws RemoteException {
        if (contas.containsKey(cpf)) {
            Usuario usuario = contas.get(cpf);
            if (usuario.getSenha().equals(senha)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public void criarConta(String dados) throws RemoteException {
        String[] dadosUsuario = dados.split("-");
        String cpf = dadosUsuario[1];

        if (!contas.containsKey(cpf)){
            Usuario usuario = new Usuario(dados);
            contas.put(usuario.getCpf(), usuario);
            salvarDados();
        }
    }

    @Override
    public boolean sacar(String cpf, String valor) throws RemoteException {
        if (!contas.containsKey(cpf)) {
            return false;
        }
        Usuario usuario = contas.get(cpf);
        double saldo = Double.parseDouble(usuario.getSaldo());
        double valorSacado = Double.parseDouble(valor);
        if (saldo >= valorSacado) {
            usuario.setSaldo(String.valueOf(saldo - valorSacado));
            salvarDados();
            return true;
        }
        return false;
    }

    @Override
    public void depositar(String cpf, String valor) throws RemoteException {
        if (!contas.containsKey(cpf)) {
            return;
        }
        Usuario usuario = contas.get(cpf);
        double saldo = Double.parseDouble(usuario.getSaldo());
        double valorDepositado = Double.parseDouble(valor);
        usuario.setSaldo(String.valueOf(saldo + valorDepositado));
        salvarDados();
    }

    @Override
    public boolean transferir(String cpfOrigem, String cpfDestino, String valor) throws RemoteException {
        if (!contas.containsKey(cpfOrigem) || !contas.containsKey(cpfDestino)) {
            return false;
        }
        Usuario usuarioOrigem = contas.get(cpfOrigem);
        Usuario usuarioDestino = contas.get(cpfDestino);
        double saldoOrigem = Double.parseDouble(usuarioOrigem.getSaldo());
        double valorTransferido = Double.parseDouble(valor);
        if (saldoOrigem >= valorTransferido) {
            usuarioOrigem.setSaldo(String.valueOf(saldoOrigem - valorTransferido));
            double saldoDestino = Double.parseDouble(usuarioDestino.getSaldo());
            usuarioDestino.setSaldo(String.valueOf(saldoDestino + valorTransferido));
            salvarDados();
            return true;
        }
        return false;
    }

    @Override
    public double getSaldo(String cpf) throws RemoteException {
        if (!contas.containsKey(cpf)) {
            return 0.0;
        }
        Usuario usuario = contas.get(cpf);
        return Double.parseDouble(usuario.getSaldo());
    }
    @Override
    public void investirPoupanca(String cpf, String valor) throws RemoteException {
        if (!contas.containsKey(cpf)) {
            return;
        }
        Usuario usuario = contas.get(cpf);
        double saldo = Double.parseDouble(usuario.getSaldo());
        double valorInvestido = Double.parseDouble(valor);
        usuario.setSaldo(String.valueOf(saldo + valorInvestido * 0.005));
        salvarDados();
    }

    @Override
    public void investirRendaFixa(String cpf, String valor) throws RemoteException {
        if (!contas.containsKey(cpf)) {
            return;
        }
        Usuario usuario = contas.get(cpf);
        double saldo = Double.parseDouble(usuario.getSaldo());
        double valorInvestido = Double.parseDouble(valor);
        usuario.setSaldo(String.valueOf(saldo + valorInvestido * 0.015));
        salvarDados();
    }

    private void salvarDados() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            outputStream.writeObject(contas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarDados() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            Object obj = inputStream.readObject();
            if (obj instanceof Map) {
                contas = (Map<String, Usuario>) obj;
            } else {
                System.out.println("Erro ao carregar dados do arquivo.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Arquivo de dados ainda não existe ou ocorreu um erro ao ler os dados.");
        }
    }

    public static void main(String[] args) {
        try {
            Banco banco = new Banco();
            BancoInterface RefServer = (BancoInterface) UnicastRemoteObject
                    .exportObject(banco, 6001);
            Registry registro = LocateRegistry.createRegistry(6001);
            registro.bind("Banco", RefServer);
        }catch (Exception e) {
            System.err.println("Banco: " + e.toString());
            e.printStackTrace();
        }
    }
}

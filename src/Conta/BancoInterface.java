package Conta;

import model.Usuario;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BancoInterface extends Remote {
        // Autenticação de usuários
        Usuario autenticar(String cpf, String senha) throws RemoteException;

        // Criação de conta corrente
        void criarConta(String dados) throws RemoteException;

        // Saque
        boolean sacar(String cpf, String valor) throws RemoteException;

        // Depósito
        void depositar(String cpf, String valor) throws RemoteException;

        // Transferência
        boolean transferir(String contaOrigem, String contaDestino, String valor) throws RemoteException;

        // Saldo
        double getSaldo(String cpf) throws RemoteException;

        // Investimentos
        void investirPoupanca(String cpf, String valor) throws RemoteException;
        void investirRendaFixa(String cpf, String valor) throws RemoteException;
}

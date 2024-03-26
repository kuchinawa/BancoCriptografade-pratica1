package Gateway;

import Conta.BancoInterface;
import Criptografia.Chaves;
import model.Usuario;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GatewayInterface extends Remote{
    Usuario logar(String ipCliente, String cpf, String senha) throws RemoteException;

    // Criação de conta corrente
    void cadastro(String ipCliente, String dados) throws Exception;

    // Saque
    boolean sacar(String ipCliente, String cpf, String valor) throws Exception;

    // Depósito
    void depositar(String ipCliente, String cpf, String valor) throws Exception;

    // Transferência
    boolean transferir(String ipCliente, String contaOrigem, String contaDestino, String valor) throws Exception;

    void receberChave(String ipCliente, Chaves chave) throws RemoteException;
    double getSaldo(String ipCliente, String cpf) throws Exception;

    void investirPoupanca(String ipCliente, String cpf, String valor) throws RemoteException;
    void investirRendaFixa(String ipCliente, String cpf, String valor) throws RemoteException;

}

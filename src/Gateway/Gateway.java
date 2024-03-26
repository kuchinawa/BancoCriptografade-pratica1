package Gateway;

import Conta.BancoInterface;
import Criptografia.AES;
import Criptografia.Chaves;
import Criptografia.Vernam;
import model.Usuario;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Gateway implements GatewayInterface{
    public Map<String, Chaves> clientes = new HashMap<>();;
    static BancoInterface stubBancoInterface;

    public Usuario logar(String ipCliente, String cpf, String senha) throws RemoteException {
        String usuario = cpf + "-" + senha;
        return null;
    }

    @Override
    public void cadastro(String ipCliente, String dados) throws Exception {
        if (clientes.containsKey(ipCliente)){
            Chaves chave = clientes.get(ipCliente);
            String dadosDescriptografados = AES.decifrar(dados, chave.CHAVE_AES);
            dadosDescriptografados = Vernam.decifrar(dadosDescriptografados, chave.CHAVE_VERNAM);
            stubBancoInterface.criarConta(dadosDescriptografados);
        }

    }

    @Override
    public boolean sacar(String ipCliente, String cpf, String valor) throws Exception {
        if (clientes.containsKey(ipCliente)){
            Chaves chave = clientes.get(ipCliente);
            String cpfDescriptografado = AES.decifrar(cpf, chave.CHAVE_AES);
            cpfDescriptografado = Vernam.decifrar(cpfDescriptografado, chave.CHAVE_VERNAM);
            String valorDescriptografado = AES.decifrar(valor, chave.CHAVE_AES);
            valorDescriptografado = Vernam.decifrar(valorDescriptografado, chave.CHAVE_VERNAM);
            return stubBancoInterface.sacar(cpfDescriptografado, valorDescriptografado);
        }
        return false;
    }

    @Override
    public void depositar(String ipCliente, String cpf, String valor) throws Exception {
        if (clientes.containsKey(ipCliente)){
            Chaves chave = clientes.get(ipCliente);
            String cpfDescriptografado = AES.decifrar(cpf, chave.CHAVE_AES);
            cpfDescriptografado = Vernam.decifrar(cpfDescriptografado, chave.CHAVE_VERNAM);
            String valorDescriptografado = AES.decifrar(valor, chave.CHAVE_AES);
            valorDescriptografado = Vernam.decifrar(valorDescriptografado, chave.CHAVE_VERNAM);
            stubBancoInterface.depositar(cpfDescriptografado, valorDescriptografado);
        }
    }

    @Override
    public boolean transferir(String ipCliente, String cpfOrigem, String cpfDestino, String valor) throws Exception {
        if (clientes.containsKey(ipCliente)){
            Chaves chave = clientes.get(ipCliente);
            String cpfOrigemDescriptografado = AES.decifrar(cpfOrigem, chave.CHAVE_AES);
            cpfOrigemDescriptografado = Vernam.decifrar(cpfOrigemDescriptografado, chave.CHAVE_VERNAM);
            String cpfDestinoDescriptografado = AES.decifrar(cpfDestino, chave.CHAVE_AES);
            cpfDestinoDescriptografado = Vernam.decifrar(cpfDestinoDescriptografado, chave.CHAVE_VERNAM);
            String valorDescriptografado = AES.decifrar(valor, chave.CHAVE_AES);
            valorDescriptografado = Vernam.decifrar(valorDescriptografado, chave.CHAVE_VERNAM);
            return stubBancoInterface.transferir(cpfOrigemDescriptografado, cpfDestinoDescriptografado, valorDescriptografado);
        }
        return false;
    }

    @Override
    public double getSaldo(String ipCliente, String cpf) throws Exception {
        if (clientes.containsKey(ipCliente)){
            Chaves chave = clientes.get(ipCliente);
            String cpfDescriptografado = AES.decifrar(cpf, chave.CHAVE_AES);
            cpfDescriptografado = Vernam.decifrar(cpfDescriptografado, chave.CHAVE_VERNAM);
            return stubBancoInterface.getSaldo(cpfDescriptografado);
        }
        return 0.0;
    }

    @Override
    public void receberChave(String ipCliente, Chaves chave) throws RemoteException {
        clientes.put(ipCliente, chave);
    }

    @Override
    public void investirPoupanca(String ipCliente, String cpf, String valor) throws RemoteException {

    }

    @Override
    public void investirRendaFixa(String ipCliente, String cpf, String valor) throws RemoteException {

    }

    public Gateway(){
        try {
            Registry registro1 = LocateRegistry.getRegistry("localhost", 6001);
            stubBancoInterface = (BancoInterface) registro1.lookup("Banco");

            Gateway refObjetoRemoto = new Gateway();
            GatewayInterface RefServer = (GatewayInterface) UnicastRemoteObject
                    .exportObject(refObjetoRemoto, 6002);
            Registry registro = LocateRegistry.createRegistry(6002);
            registro.bind("Gateway", RefServer);


        }catch (Exception e) {
            System.err.println("Gateway: " + e.toString());
            e.printStackTrace();
        }
    }

}

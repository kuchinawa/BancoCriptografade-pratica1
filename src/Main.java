
import Gateway.GatewayInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            GatewayInterface stub = (GatewayInterface) registry.lookup("Gateway");
            stub.cadastro(" ", " ");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

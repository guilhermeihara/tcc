package hyperledger.cefetmg.tcc.services;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractEvent;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import hyperledger.cefetmg.tcc.dto.DtoAsset;
import hyperledger.cefetmg.tcc.dto.DtoTransaction;
import hyperledger.cefetmg.tcc.interfaces.IHyperledgerService;

@Service
public class HyperledgerService implements IHyperledgerService {

	private Logger logger = Logger.getLogger("HyperledgerService");
	Gson gson = new Gson();

	private String HYPERLEDGER_CHANNEL = "iot-devices";
	private String HYPERLEDGER_CONTRACT = "tcc-contract";

	private Gateway.Builder builder;
	private Gateway gateway;
	private Network network;
	private Contract contract;

	static {
		//System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}

	public HyperledgerService() {
		try {
			connectToLedger();
			listener();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getAssets() {
		String assets = "";
		// Create a gateway connection
		try {
			byte[] result;
			System.out.println("\n");
//			result = contract.evaluateTransaction("GetAllAssets");
			result = contract.evaluateTransaction("GetAllAssets");
			assets = new String(result);
			System.out.println("Evaluate Transaction: GetAllAssets, result: " + assets);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return assets;
	}

	public String getAssetValue(Long deviceId) {
		String assets = "";
		try {
			byte[] result;
			System.out.println("\n");

			System.out.println("Evaluate Transaction: ReadAsset " + String.valueOf(deviceId));
			// ReadAsset returns an asset with given assetID
			result = contract.evaluateTransaction("ReadAsset", String.valueOf(deviceId));
			System.out.println("result: " + new String(result));

			assets = new String(result);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return assets;
	}

	public boolean updateAsset(DtoAsset asset) {
		byte[] result;

		try {
			result = contract.submitTransaction("UpdateAsset", asset.getId(), asset.getName(), asset.getValue());
			System.out.println(new String(result));
			return true;
		} catch (ContractException contractE) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean createAsset(DtoAsset asset) {

		try {
			logger.info("Creating device on Hyperledger: " + asset.toString());
			contract.submitTransaction("CreateAsset", asset.getId(), asset.getAddress(), asset.getName(), asset.getValue());
			return true;
		} catch (ContractException contractE) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void listener() {

		logger.info("Creating Events Listener");
		Consumer<ContractEvent> listener = new Consumer<ContractEvent>() {
			@Override
			public void accept(ContractEvent t) {
				if (t.getPayload().get() != null) {
					DtoTransaction transaction = gson.fromJson(new String(t.getPayload().get()), DtoTransaction.class);
					transaction.setTransactionId(t.getTransactionEvent().getTransactionID());
					transaction.setStatus(t.getTransactionEvent().isValid() ? "Valid" : "Invalid");
					transaction.setTimestamp(t.getTransactionEvent().getTimestamp().toString());
					transaction.setOrigin(t.getTransactionEvent().getCreator().getMspid());
					transaction.setContract(t.getName());
					logger.info("Event Transaction -> " + gson.toJson(transaction, DtoTransaction.class));
				}
			}
		};

		contract.addContractListener(listener);
	}

	private void connectToLedger() throws IOException {

		// Load an existing wallet holding identities used to access the network.
		Path walletDirectory = Paths.get("wallet");
		Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

		// Path to a common connection profile describing the network.
		Path networkConfigFile = Paths.get("org1msp_profile.json"); // connection-org1.yaml

		// Configure the gateway connection used to access the network.
		builder = Gateway.createBuilder().identity(wallet, "takahashi").networkConfig(networkConfigFile);
		//discovery(true);

		gateway = builder.connect();
		network = gateway.getNetwork(HYPERLEDGER_CHANNEL);
		contract = network.getContract(HYPERLEDGER_CONTRACT);
	}

//	private void connectToLedger() throws IOException {
//
//		// enrolls the admin and registers the user
//		try {
//			enrollAdmin();
//			registerUser();
//		} catch (Exception e) {
//			System.err.println(e);
//		}
//
//		// Load an existing wallet holding identities used to access the network.
//		Path walletDirectory = Paths.get("wallet");
//		Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);
//
//		// Path to a common connection profile describing the network.
//		Path networkConfigFile = Paths.get("connection-org1.json"); // connection-org1.yaml
//
//		// Configure the gateway connection used to access the network.
//		builder = Gateway.createBuilder().identity(wallet, "appUser").networkConfig(networkConfigFile).discovery(true);
//
//		gateway = builder.connect();
//		network = gateway.getNetwork(HYPERLEDGER_CHANNEL);
//		contract = network.getContract(HYPERLEDGER_CONTRACT);
//	}

}

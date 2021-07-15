package hyperledger.cefetmg.tcc.services;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.Set;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.X509Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.springframework.stereotype.Service;

import hyperledger.cefetmg.tcc.dto.DtoAsset;
import hyperledger.cefetmg.tcc.interfaces.IHyperledgerService;

@Service
public class HyperledgerService implements IHyperledgerService {

	private Gateway.Builder builder;
	static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}

	public HyperledgerService() {
		try {
			connectToLedger();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getAssets() {
		String assets = "";
		// Create a gateway connection
		try {
			Gateway gateway = builder.connect();
//			 Obtain a smart contract deployed on the network.
			Network network = gateway.getNetwork("mychannel");
//			Contract contract = network.getContract("basic");
			Contract contract = network.getContract("test");

//			contract.submitTransaction("InitLedger");

			byte[] result;
			System.out.println("\n");
//			result = contract.evaluateTransaction("GetAllAssets");
			result = contract.evaluateTransaction("GetAllAssets");
			assets = new String(result);
			System.out.println("Evaluate Transaction: GetAllAssets, result: " + assets);

//			result = contract.
//			contract.submitTransaction("CreateAsset", "asset13", "yellow", "5", "Tom", "1300");
//			result = contract.submitTransaction("UpdateAsset", "asset1", "blue", "50", "Tomoko", "300");
//
//			// Submit transactions that store state to the ledger.
//			byte[] createCarResult = contract.createTransaction("CreateAsset").submit("asset13", "yellow", "5", "Tom",
//					"1300");
//			System.out.println(new String(createCarResult, StandardCharsets.UTF_8));
//
//			// Evaluate transactions that query state from the ledger. 
//			byte[] queryAllCarsResult = contract.evaluateTransaction("GetAllAssets");
//			System.out.println(new String(queryAllCarsResult, StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("OIE");
		return assets;
	}

	public void createAsset(DtoAsset asset) {
		try (Gateway gateway = builder.connect()) {

//			 Obtain a smart contract deployed on the network.
			Network network = gateway.getNetwork("mychannel");
			Contract contract = network.getContract("test");

//			contract.submitTransaction("InitLedger");

//			byte[] result;
//			contract.submitTransaction("createMyAsset", asset.getAssetID(), asset.getColor(),
//					String.valueOf(asset.getSize()), asset.getOwner(), String.valueOf(asset.getAppraisedValue()));
			contract.submitTransaction("CreateAsset", asset.getAssetID(), "teste1", "teste2", "teste3", "teste4",
					"teste5");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void connectToLedger() throws IOException {

		// enrolls the admin and registers the user
		try {
			enrollAdmin();
			registerUser();
		} catch (Exception e) {
			System.err.println(e);
		}

		// Load an existing wallet holding identities used to access the network.
		Path walletDirectory = Paths.get("wallet");
		Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

		// Path to a common connection profile describing the network.
		Path networkConfigFile = Paths.get("connection-org1.json"); // connection-org1.yaml

		// Configure the gateway connection used to access the network.
		builder = Gateway.createBuilder().identity(wallet, "appUser").networkConfig(networkConfigFile).discovery(true);
	}

	private void enrollAdmin() throws Exception {
		// Create a CA client for interacting with the CA.
		Properties props = new Properties();
		props.put("pemFile", "certificate/ca.org1.example.com-cert.pem");
		props.put("allowAllHostNames", "true");
		HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
		CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
		caClient.setCryptoSuite(cryptoSuite);

		// Create a wallet for managing identities
		Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));

		// Check to see if we've already enrolled the admin user.
		if (wallet.get("admin") != null) {
			System.out.println("An identity for the admin user \"admin\" already exists in the wallet");
			return;
		}

		// Enroll the admin user, and import the new identity into the wallet.
		final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
		enrollmentRequestTLS.addHost("localhost");
		enrollmentRequestTLS.setProfile("tls");
		Enrollment enrollment = caClient.enroll("admin", "adminpw", enrollmentRequestTLS);
		Identity user = Identities.newX509Identity("Org1MSP", enrollment);
		wallet.put("admin", user);
		System.out.println("Successfully enrolled user \"admin\" and imported it into the wallet");
	}

	private void registerUser() throws Exception {

		// Create a CA client for interacting with the CA.
		Properties props = new Properties();
		props.put("pemFile", "certificate/ca.org1.example.com-cert.pem");
		props.put("allowAllHostNames", "true");
		HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
		CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
		caClient.setCryptoSuite(cryptoSuite);

		// Create a wallet for managing identities
		Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));

		// Check to see if we've already enrolled the user.
		if (wallet.get("appUser") != null) {
			System.out.println("An identity for the user \"appUser\" already exists in the wallet");
			return;
		}

		X509Identity adminIdentity = (X509Identity) wallet.get("admin");
		if (adminIdentity == null) {
			System.out.println("\"admin\" needs to be enrolled and added to the wallet first");
			return;
		}
		User admin = new User() {

			@Override
			public String getName() {
				return "admin";
			}

			@Override
			public Set<String> getRoles() {
				return null;
			}

			@Override
			public String getAccount() {
				return null;
			}

			@Override
			public String getAffiliation() {
				return "org1.department1";
			}

			@Override
			public Enrollment getEnrollment() {
				return new Enrollment() {

					@Override
					public PrivateKey getKey() {
						return adminIdentity.getPrivateKey();
					}

					@Override
					public String getCert() {
						return Identities.toPemString(adminIdentity.getCertificate());
					}
				};
			}

			@Override
			public String getMspId() {
				return "Org1MSP";
			}

		};

		// Register the user, enroll the user, and import the new identity into the
		// wallet.
		RegistrationRequest registrationRequest = new RegistrationRequest("appUser");
		registrationRequest.setAffiliation("org1.department1");
		registrationRequest.setEnrollmentID("appUser");
		String enrollmentSecret = caClient.register(registrationRequest, admin);
		Enrollment enrollment = caClient.enroll("appUser", enrollmentSecret);
		Identity user = Identities.newX509Identity("Org1MSP", enrollment);
		wallet.put("appUser", user);
		System.out.println("Successfully enrolled user \"appUser\" and imported it into the wallet");
	}

}

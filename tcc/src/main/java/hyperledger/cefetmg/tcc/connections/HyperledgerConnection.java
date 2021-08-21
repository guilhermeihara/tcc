package hyperledger.cefetmg.tcc.connections;

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
import org.springframework.stereotype.Repository;

import hyperledger.cefetmg.tcc.interfaces.IHyperledgerService;

@Repository
public class HyperledgerConnection implements IHyperledgerService {

	private String HYPERLEDGER_CHANNEL = "mychannel";

	private Gateway.Builder builder;
	private Gateway gateway;
	private Network network;

	static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}

	public HyperledgerConnection() {
		try {
			connectToLedger(HYPERLEDGER_CHANNEL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	private void connectToLedger() throws IOException {
//
//		// Load an existing wallet holding identities used to access the network.
//		Path walletDirectory = Paths.get("wallet");
//		Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);
//
//		// Path to a common connection profile describing the network.
//		Path networkConfigFile = Paths.get("org1msp_profile.json"); // connection-org1.yaml
//
//		// Configure the gateway connection used to access the network.
//		builder = Gateway.createBuilder().identity(wallet, "takahashi").networkConfig(networkConfigFile);
//		//discovery(true);
//
//		gateway = builder.connect();
//		network = gateway.getNetwork(HYPERLEDGER_CHANNEL);
//		contract = network.getContract(HYPERLEDGER_CONTRACT);
//	}

	private void connectToLedger(String channel) throws IOException {

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
//		builder = Gateway.createBuilder().identity(wallet, "appUser").networkConfig(networkConfigFile).discovery(true);
		builder = Gateway.createBuilder().identity(wallet, "appUser").networkConfig(networkConfigFile);

		gateway = builder.connect();
		network = gateway.getNetwork(channel);
	}

	public Contract connectToContract(String contractName) {
		return network.getContract(contractName);
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

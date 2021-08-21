package hyperledger.cefetmg.tcc.services;

import java.util.logging.Logger;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import hyperledger.cefetmg.tcc.connections.HyperledgerConnection;
import hyperledger.cefetmg.tcc.dto.DtoUser;
import hyperledger.cefetmg.tcc.interfaces.IHyperledgerService;

@Service
public class HyperledgerUserService implements IHyperledgerService {

	private Logger logger = Logger.getLogger("HyperledgerUserService");

	private HyperledgerConnection _hyperledgerConnection;

	private Contract _userContract;
	Gson gson = new Gson();

	private String HYPERLEDGER_CONTRACT = "user";

	public HyperledgerUserService() {
		_hyperledgerConnection = new HyperledgerConnection();
		_userContract = _hyperledgerConnection.connectToContract(HYPERLEDGER_CONTRACT);
	}

	public boolean createUser(DtoUser user) {
		try {
			logger.info("Creating user on Hyperledger: " + user.getEmail());
			_userContract.submitTransaction("CreateUser", user.getEmail(), user.getPassword(), user.getName());
			return true;
		} catch (ContractException contractE) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateUser(DtoUser user) {
//		byte[] result;

		try {
			logger.info("Update user on Hyperledger: " + user.getEmail());
			_userContract.submitTransaction("UpdateUser", user.getEmail(), user.getPassword(), user.getName());
			return true;
		} catch (ContractException contractE) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteUser(String email) {

		try {
			logger.info("Deleting user on Hyperledger: " + email);
			_userContract.submitTransaction("DeleteUser", email);
			return true;
		} catch (ContractException contractE) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getAllUsers() {

		String assets = "";
		// Create a gateway connection
		try {
			byte[] result;
			System.out.println("\n");
//			result = contract.evaluateTransaction("GetAllAssets");
			result = _userContract.evaluateTransaction("GetAllUsers");
			assets = new String(result);
			System.out.println("Evaluate Transaction: GetAllUsers, result: " + assets);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return assets;
	}
	

}

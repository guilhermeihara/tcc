package hyperledger.cefetmg.tcc.services;

import java.util.logging.Logger;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import hyperledger.cefetmg.tcc.connections.HyperledgerConnection;
import hyperledger.cefetmg.tcc.dto.DtoAsset;
import hyperledger.cefetmg.tcc.interfaces.IHyperledgerService;

@Service
public class HyperledgerDeviceService implements IHyperledgerService {

	private Logger logger = Logger.getLogger("HyperledgerUserService");

	private HyperledgerConnection _hyperledgerConnection;

	private Contract _deviceContract;
	Gson gson = new Gson();

	private String HYPERLEDGER_CONTRACT = "basic";

	public HyperledgerDeviceService() {
		_hyperledgerConnection = new HyperledgerConnection();
		_deviceContract = _hyperledgerConnection.connectToContract(HYPERLEDGER_CONTRACT);
	}

	public boolean createAsset(DtoAsset asset) {
		try {
			logger.info("Creating device on Hyperledger: " + asset.toString());
			_deviceContract.submitTransaction("CreateAsset", asset.getAddress(), asset.getOwner(), asset.getName(), asset.getValue());
			return true;
		} catch (ContractException contractE) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateAsset(DtoAsset asset) {
		
		try {
			_deviceContract.submitTransaction("UpdateAsset", asset.getAddress(), asset.getName(), asset.getValue());
			return true;
		} catch (ContractException contractE) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteAsset(String assetId) {
		
		try {
			logger.info("Deleting device on Hyperledger: " + assetId);
			_deviceContract.submitTransaction("DeleteAsset", assetId);
			return true;
		} catch (ContractException contractE) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}

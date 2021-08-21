package hyperledger.cefetmg.tcc.services;

import java.util.function.Consumer;
import java.util.logging.Logger;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import hyperledger.cefetmg.tcc.connections.HyperledgerConnection;
import hyperledger.cefetmg.tcc.dto.DtoTransaction;
import hyperledger.cefetmg.tcc.interfaces.IHyperledgerService;

@Service
@Scope("singleton")
public class HyperledgerListener implements IHyperledgerService {

	private Logger logger = Logger.getLogger("HyperledgerUserService");

	private HyperledgerConnection _hyperledgerConnection;

	private Contract _userContract;
	private Contract _deviceContract;
	Gson gson = new Gson();

	private String HYPERLEDGER_USER_CONTRACT = "user";
	private String HYPERLEDGER_DEVICE_CONTRACT = "basic";
	public HyperledgerListener() {
		_hyperledgerConnection = new HyperledgerConnection();
		_userContract = _hyperledgerConnection.connectToContract(HYPERLEDGER_USER_CONTRACT);
		_deviceContract = _hyperledgerConnection.connectToContract(HYPERLEDGER_DEVICE_CONTRACT);
		listener();
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

		_userContract.addContractListener(listener);
		_deviceContract.addContractListener(listener);
	}
}

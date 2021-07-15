package hyperledger.cefetmg.tcc.interfaces;

import hyperledger.cefetmg.tcc.dto.DtoAsset;

public interface IHyperledgerService {

	public String getAssets();
	public void createAsset(DtoAsset asset);	
}

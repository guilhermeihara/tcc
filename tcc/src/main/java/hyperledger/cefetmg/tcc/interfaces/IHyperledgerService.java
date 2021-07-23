package hyperledger.cefetmg.tcc.interfaces;

import hyperledger.cefetmg.tcc.dto.DtoAsset;

public interface IHyperledgerService {

	public String getAssets();
	public boolean createAsset(DtoAsset asset);
	public boolean updateAsset(DtoAsset asset);
}

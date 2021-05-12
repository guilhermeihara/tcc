package hyperledger.cefetmg.tcc.dto;

public class DtoAsset {

	private String assetID;

	private String color;

	private int size;

	private String owner;

	private int appraisedValue;

	public DtoAsset() {
	}

	public DtoAsset(String assetID, String color, int size, String owner, int appraisedValue) {
		super();
		this.assetID = assetID;
		this.color = color;
		this.size = size;
		this.owner = owner;
		this.appraisedValue = appraisedValue;
	}

	public String getAssetID() {
		return assetID;
	}

	public void setAssetID(String assetID) {
		this.assetID = assetID;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getAppraisedValue() {
		return appraisedValue;
	}

	public void setAppraisedValue(int appraisedValue) {
		this.appraisedValue = appraisedValue;
	}

	@Override
	public String toString() {
		return "DtoAsset [assetID=" + assetID + ", color=" + color + ", size=" + size + ", owner=" + owner
				+ ", appraisedValue=" + appraisedValue + "]";
	}
}

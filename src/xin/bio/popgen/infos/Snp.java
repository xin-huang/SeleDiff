package xin.bio.popgen.infos;

public class Snp {
	
	private final byte[] id;
	private final byte[] refAllele;
	private final byte[] altAllele;
	
	public Snp(String id, String refAllele, String altAllele) {
		this.id = id.getBytes();
		this.refAllele = refAllele.getBytes();
		this.altAllele = altAllele.getBytes();
	}
	
	public byte[] getId() { return id; }
	
	public byte[] getRefAllele() { return refAllele; }
	
	public byte[] getAltAllele() { return altAllele; }

}

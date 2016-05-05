package xin.bio.popgen.datatype;

public class Individual {

	private String pop_id;      // the population name of the individual
	private String id;          // the id of the individual
	private String paternal_id; // the id of the individual's father
	private String maternal_id; // the id of the individual's mother
	private String sex;         // the sex of the individual
	private String phenotype;   // the phenotype of the individual
	
	/**
	 * Initialize an Individual object
	 * @param pop_id       the population name of the individual
	 * @param id           the id of the individual
	 * @param paternal_id  the id of the individual's father
	 * @param maternal_id  the id of the individual's mother
	 * @param sex          the sex of the individual
	 * @param phenotype    the phenotype of the individual
	 * @param genotypes    the genotypes of the individual
	 */
	public Individual(String pop_id, String id, String paternal_id,
			String maternal_id, String sex, String phenotype) {
		super();
		this.pop_id = pop_id;
		this.id = id;
		this.paternal_id = paternal_id;
		this.maternal_id = maternal_id;
		this.sex = sex;
		this.phenotype = phenotype;
	}

	/**
	 * Return the population name of the individual
	 * @return the population name of the individual
	 */
	public String getPopId() {
		return pop_id;
	}

	/**
	 * Return the id of the individual
	 * @return the id of the individual
	 */
	public String getId() {
		return id;
	}

	/**
	 * Return the id of the individual's father
	 * @return the id of the individual's father
	 */
	public String getPaternalId() {
		return paternal_id;
	}

	/**
	 * Return the id of the individual's mother
	 * @return the id of the individual's mother
	 */
	public String getMaternalId() {
		return maternal_id;
	}

	/**
	 * Return the sex of the individual
	 * @return the sex of the individual
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * Return the phenotype of the individual
	 * @return the phenotype of the individual
	 */
	public String getPhenotype() {
		return phenotype;
	}


	/**
	 * Set the population name of the individual
	 * @param pop_id  the population name of the individual
	 */
	public void setPopId(String pop_id) {
		this.pop_id = pop_id;
	}

	/**
	 * Set the id of the individual
	 * @param id  the id of the individual
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Set the id of the individual's father
	 * @param paternal_id  the id of the individual's father
	 */
	public void setPaternalId(String paternal_id) {
		this.paternal_id = paternal_id;
	}

	/**
	 * Set the id of the individual's mother
	 * @param maternal_id  the id of the individual's mother
	 */
	public void setMaternalId(String maternal_id) {
		this.maternal_id = maternal_id;
	}

	/**
	 * Set the sex of the individual
	 * @param sex  the sex of the individual
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * Set the phenotype of the individual
	 * @param phenotype  the phenotype of the individual
	 */
	public void setPhenotype(String phenotype) {
		this.phenotype = phenotype;
	}

	@Override
	public String toString() {
		return pop_id + " " + id + " " + paternal_id + " " + maternal_id + " " + sex + " " + phenotype;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}


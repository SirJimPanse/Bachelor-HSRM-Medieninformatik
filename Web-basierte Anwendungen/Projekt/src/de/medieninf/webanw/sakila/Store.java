package de.medieninf.webanw.sakila;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="store")
public class Store implements Serializable {
	private static final long serialVersionUID = -642799459966839049L;
	
	protected int storeId;
	protected int version;
	protected Staff manager;
	protected Paddress paddress;
	
    public Store() {     	
    }
    
    @Id
    @SequenceGenerator(name="StoreIdGen", sequenceName="store_store_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="StoreIdGen")
    @Column(name="store_id")
    public int getStoreId() {
    	return this.storeId;
    }
    public void setStoreId(int storeId) {
    	this.storeId = storeId;
    }
    
    @Version
    @Column(name="version")
    public int getVersion() {
    	return this.version;
    }
    public void setVersion(int version) {
    	this.version = version;
    }

    @ManyToOne
    @JoinColumn(name="manager")
    public Staff getManager() {
    	return this.manager;
    }
    public void setManager(Staff manager) {
    	this.manager = manager;
    }

    @ManyToOne
    @JoinColumn(name = "p_address_id")
    public Paddress getPaddress() {
    	return this.paddress;
    }
    public void setPaddress(Paddress paddress) {
    	this.paddress = paddress;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((paddress == null) ? 0 : paddress.hashCode());
		result = prime * result + storeId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Store other = (Store) obj;
		if (paddress == null) {
			if (other.paddress != null)
				return false;
		} else if (!paddress.equals(other.paddress))
			return false;
		if (storeId != other.storeId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Store["+storeId + "]";
	}
}

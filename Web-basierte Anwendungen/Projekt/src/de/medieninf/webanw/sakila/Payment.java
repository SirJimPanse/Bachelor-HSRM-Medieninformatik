package de.medieninf.webanw.sakila;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="payment")
public class Payment implements Serializable {
	private static final long serialVersionUID = 4002958748755989843L;
	
	protected int paymentId;
    protected int version;
    protected Customer customer;
    protected Staff staff;
    protected BigDecimal amount;
    protected Date paymentDate;

    public Payment() { 
    	
    }

    @Id
    @SequenceGenerator(name="PaymentIdGen", sequenceName="payment_payment_id_seq",
                    allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PaymentIdGen")
    @Column(name="payment_id")
    public int getPaymentId() {
    	return this.paymentId;
    }
    public void setPaymentId(int paymentId) {
    	this.paymentId = paymentId;
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
    @JoinColumn(name="customer_id")
    public Customer getCustomer() {
    	return this.customer;
    }
    public void setCustomer(Customer customer) {
    	this.customer = customer;
    }
    
    @ManyToOne
    @JoinColumn(name="staff_id")
    public Staff getStaff() {
    	return this.staff;
    }
    public void setStaff(Staff staff) {
    	this.staff = staff;
    }

    @Column(name="amount")
    public BigDecimal getAmount() {
    	return this.amount;
    }
    public void setAmount(BigDecimal amount) {
    	this.amount = amount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="payment_date")
    public Date getPaymentDate() {
    	return this.paymentDate;
    }
    public void setPaymentDate(final Date paymentDate) {
    	this.paymentDate = paymentDate;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result
				+ ((customer == null) ? 0 : customer.hashCode());
		result = prime * result
				+ ((paymentDate == null) ? 0 : paymentDate.hashCode());
		result = prime * result + paymentId;
		result = prime * result + ((staff == null) ? 0 : staff.hashCode());
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
		Payment other = (Payment) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (paymentDate == null) {
			if (other.paymentDate != null)
				return false;
		} else if (!paymentDate.equals(other.paymentDate))
			return false;
		if (paymentId != other.paymentId)
			return false;
		if (staff == null) {
			if (other.staff != null)
				return false;
		} else if (!staff.equals(other.staff))
			return false;
		return true;
	}

}

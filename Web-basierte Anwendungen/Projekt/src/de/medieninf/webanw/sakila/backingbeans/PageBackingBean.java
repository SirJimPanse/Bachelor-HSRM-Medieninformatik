package de.medieninf.webanw.sakila.backingbeans;

import java.io.Serializable;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import de.medieninf.webanw.sakila.SakilaBean;

public abstract class PageBackingBean<E> implements Serializable {

	private static final long serialVersionUID = -5340681180122722745L;
	
	protected List<E> model;
	
	public <T> T gb(String s, Class<T> c) { return FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{" + s + "}", c); }
	public SakilaBean gsb() 			  { return this.gb("sakila",SakilaBean.class); }
	
	public List<E> getModel() 			  { return model; }
	public void setModel(List<E> model)   { this.model = model; }

	public abstract void update(ActionEvent ae); // sollte die zugrunde liegende Liste model neu aus SakilaBean holen
}

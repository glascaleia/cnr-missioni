package it.cnr.missioni.model.configuration;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.geosdi.geoplatform.experimental.el.api.model.Document;

import it.cnr.missioni.model.missione.TrattamentoMissioneEsteraEnum;

/**
 * @author Salvia Vito
 */
@XmlRootElement(name = "tipologiaSpesa")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "value","tipo","occorrenzeGiornaliere","tipoTrattamento","voceSpesa" })
public class TipologiaSpesa implements Document {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8287063179690294163L;
	/**
	 * 
	 */
	private String id;
	@NotNull
	private String value;
	@NotNull
	private TipoSpesaEnum tipo;
	@NotNull
	private TrattamentoMissioneEsteraEnum tipoTrattamento;
	@NotNull
	private VoceSpesaEnum voceSpesa;

	public enum VoceSpesaEnum{
		PASTO,TRASPORTO,ALLOGGIO,RIMBORSOKM,ALTRO;
	}
	
	public enum TipoSpesaEnum{
		ESTERA,ITALIA;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.geosdi.geoplatform.experimental.el.api.model.Document#isIdSetted()
	 */
	@Override
	public Boolean isIdSetted() {
		return ((this.id != null) && !(this.id.isEmpty()));
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the tipo
	 */
	public TipoSpesaEnum getTipo() {
		return tipo;
	}

	/**
	 * @param tipo 
	 */
	public void setTipo(TipoSpesaEnum tipo) {
		this.tipo = tipo;
	}





	/**
	 * @return the tipoTrattamento
	 */
	public TrattamentoMissioneEsteraEnum getTipoTrattamento() {
		return tipoTrattamento;
	}

	/**
	 * @param tipoTrattamento 
	 */
	public void setTipoTrattamento(TrattamentoMissioneEsteraEnum tipoTrattamento) {
		this.tipoTrattamento = tipoTrattamento;
	}

	/**
	 * @return the voceSpesa
	 */
	public VoceSpesaEnum getVoceSpesa() {
		return voceSpesa;
	}

	/**
	 * @param voceSpesa 
	 */
	public void setVoceSpesa(VoceSpesaEnum voceSpesa) {
		this.voceSpesa = voceSpesa;
	}

	/**
	 * @return
	 */
	@Override
	public String toString() {
		return "TipologiaSpesa [id=" + id + ", value=" + value + ", tipo=" + tipo + ", tipoTrattamento="
				+ tipoTrattamento + ", voceSpesa=" + voceSpesa + "]";
	}








}

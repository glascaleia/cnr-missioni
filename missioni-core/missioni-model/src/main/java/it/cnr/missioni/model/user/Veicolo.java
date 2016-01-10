package it.cnr.missioni.model.user;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Salvia Vito
 */
public class Veicolo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8495194560567649985L;
	/**
	 * 
	 */
	@NotBlank
	private String tipo;
	@NotBlank
	private String targa;
	@NotBlank
	private String cartaCircolazione;
	@NotBlank
	private String polizzaAssicurativa;

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the targa
	 */
	public String getTarga() {
		return targa;
	}

	/**
	 * @param targa
	 */
	public void setTarga(String targa) {
		this.targa = targa;
	}

	/**
	 * @return the cartaCircolazione
	 */
	public String getCartaCircolazione() {
		return cartaCircolazione;
	}

	/**
	 * @param cartaCircolazione
	 */
	public void setCartaCircolazione(String cartaCircolazione) {
		this.cartaCircolazione = cartaCircolazione;
	}

	/**
	 * @return the polizzaAssicurativa
	 */
	public String getPolizzaAssicurativa() {
		return polizzaAssicurativa;
	}

	/**
	 * @param polizzaAssicurativa
	 */
	public void setPolizzaAssicurativa(String polizzaAssicurativa) {
		this.polizzaAssicurativa = polizzaAssicurativa;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Veicolo [tipo=" + tipo + ", targa=" + targa + ", cartaCircolazione=" + cartaCircolazione
				+ ", polizzaAssicurativa=" + polizzaAssicurativa + "]";
	}

}
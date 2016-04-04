package it.cnr.missioni.el.model.search.builder;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;

import it.cnr.missioni.el.model.search.BooleanModelSearch;
import it.cnr.missioni.el.model.search.DateRangeSearch;
import it.cnr.missioni.el.model.search.EnumBooleanType;
import it.cnr.missioni.el.model.search.ExactSearch;
import it.cnr.missioni.el.model.search.ExistFieldSearch;
import it.cnr.missioni.el.model.search.MultiMatchSearch;

/**
 * @author Salvia Vito
 */
public class MissioneSearchBuilder implements ISearchBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4387150079550066147L;

	private BooleanModelSearch booleanModelSearch;
	private DateTime fromDataInserimento = null;
	private DateTime toDataInserimento = null;
	private String idUser = null;
	private String stato = null;
	private Long numeroOrdineRimborso = null;
	private DateTime fromDataRimbroso = null;
	private DateTime toDataRimbroso = null;
	private String idMissione = null;
	private String oggetto = null;
	private String multiMatchValue;
	private String fieldExist;
	private String fieldNotExist;
	private String fieldMultiMatch = "missione.localita,missione.oggetto,missione.id,missione.shortUser";
	private int size = 10;
	private int from = 0;
	private String fieldSort = SearchConstants.MISSIONE_FIELD_DATA_INSERIMENTO;
	private SortOrder sortOrder = SortOrder.DESC;

	private MissioneSearchBuilder() {
		booleanModelSearch = new BooleanModelSearch();
	}

	public static MissioneSearchBuilder getMissioneSearchBuilder() {
		return new MissioneSearchBuilder();
	}

	public MissioneSearchBuilder withRangeDataInserimento(DateTime fromDataInserimento, DateTime toDataInserimento) {

		this.fromDataInserimento = fromDataInserimento;
		this.toDataInserimento = toDataInserimento;
		if (fromDataInserimento != null || toDataInserimento != null)
			booleanModelSearch.getListaSearch().add(new DateRangeSearch(SearchConstants.MISSIONE_FIELD_DATA_INSERIMENTO,
					fromDataInserimento, toDataInserimento));
		return self();
	}
	


	public MissioneSearchBuilder withIdUser(String idUser) {
		this.idUser = idUser;

		if (idUser != null && !idUser.trim().equals(""))
			booleanModelSearch.getListaSearch().add(new ExactSearch(SearchConstants.MISSIONE_FIELD_ID_USER, idUser));
		return self();
	}

	public MissioneSearchBuilder withStato(String stato) {
		this.stato = stato;
		if (stato != null && !stato.trim().equals(""))
			booleanModelSearch.getListaSearch().add(new ExactSearch(SearchConstants.MISSIONE_FIELD_STATO, stato));
		return self();
	}

	public MissioneSearchBuilder withNumeroOrdineMissione(Long numeroOrdineRimborso) {
		this.numeroOrdineRimborso = numeroOrdineRimborso;
		if (numeroOrdineRimborso != null && !numeroOrdineRimborso.equals(""))
			booleanModelSearch.getListaSearch()
					.add(new ExactSearch(SearchConstants.MISSIONE_FIELD_RIMBORSO_NUMERO_ORDINE, numeroOrdineRimborso));
		return self();
	}

	public MissioneSearchBuilder withRangeDataRimborso(DateTime fromDataRimborso, DateTime toDataRimborso) {
		this.fromDataRimbroso = fromDataRimborso;
		this.toDataRimbroso = toDataRimborso;
		if (fromDataRimbroso != null || toDataRimbroso != null)
			booleanModelSearch.getListaSearch().add(new DateRangeSearch(
					SearchConstants.MISSIONE_FIELD_RIMBORSO_DATA_RIMBORSO, fromDataRimbroso, toDataRimbroso));
		return self();
	}

	public MissioneSearchBuilder withIdMissione(String idMissione) {
		this.idMissione = idMissione;
		if (idMissione != null && !idMissione.trim().equals(""))
			booleanModelSearch.getListaSearch().add(new ExactSearch(SearchConstants.MISSIONE_FIELD_ID, idMissione));
		return self();
	}

	public MissioneSearchBuilder withOggetto(String oggetto) {
		this.oggetto = oggetto;
		if (oggetto != null && !oggetto.trim().equals(""))

			booleanModelSearch.getListaSearch().add(new ExactSearch(SearchConstants.MISSIONE_FIELD_OGGETTO, oggetto,
					EnumBooleanType.MUST, Operator.OR));
		return self();
	}

	public MissioneSearchBuilder withMultiMatch(String multiMatchValue) {
		this.multiMatchValue = multiMatchValue;
		if (multiMatchValue != null && !multiMatchValue.trim().equals(""))

			booleanModelSearch.getListaSearch().add(new MultiMatchSearch(fieldMultiMatch, multiMatchValue));
		return self();
	}

	public MissioneSearchBuilder withFieldExist(String fieldExist) {
		this.fieldExist = fieldExist;
		if (fieldExist != null && !fieldExist.trim().equals(""))

			booleanModelSearch.getListaSearch().add(new ExistFieldSearch(fieldExist));
		return self();
	}

	public MissioneSearchBuilder withFieldNotExist(String fieldNotExist) {
		this.fieldNotExist = fieldNotExist;
		if (fieldNotExist != null && !fieldNotExist.trim().equals(""))

			booleanModelSearch.getListaSearch().add(new ExistFieldSearch(fieldNotExist, EnumBooleanType.MUST_NOT));
		return self();
	}

	public MissioneSearchBuilder withMultiMatchField(String fieldMultiMatch) {
		this.fieldMultiMatch = fieldMultiMatch;
		return self();
	}

	public MissioneSearchBuilder withSortField(String fieldSort) {
		this.fieldSort = fieldSort;
		return self();
	}

	public MissioneSearchBuilder withSize(int size) {
		this.size = size;
		return self();
	}

	public MissioneSearchBuilder withFrom(int from) {
		this.from = from;
		return self();
	}

	public BoolQueryBuilder buildQuery() {
		return booleanModelSearch.buildQuery();
	}

	/**
	 * @return the booleanModelSearch
	 */
	public BooleanModelSearch getBooleanModelSearch() {
		return booleanModelSearch;
	}

	/**
	 * @param booleanModelSearch
	 */
	public void setBooleanModelSearch(BooleanModelSearch booleanModelSearch) {
		this.booleanModelSearch = booleanModelSearch;
	}

	/**
	 * @return the fromDataInserimento
	 */
	public DateTime getFromDataInserimento() {
		return fromDataInserimento;
	}

	/**
	 * @param fromDataInserimento
	 */
	public void setFromDataInserimento(DateTime fromDataInserimento) {
		this.fromDataInserimento = fromDataInserimento;
	}

	/**
	 * @return the toDataInserimento
	 */
	public DateTime getToDataInserimento() {
		return toDataInserimento;
	}

	/**
	 * @param toDataInserimento
	 */
	public void setToDataInserimento(DateTime toDataInserimento) {
		this.toDataInserimento = toDataInserimento;
	}

	/**
	 * @return the idUser
	 */
	public String getIdUser() {
		return idUser;
	}

	/**
	 * @param idUser
	 */
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	/**
	 * @return the stato
	 */
	public String getStato() {
		return stato;
	}

	/**
	 * @param stato
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}

	/**
	 * @return the numeroOrdineRimborso
	 */
	public Long getNumeroOrdineRimborso() {
		return numeroOrdineRimborso;
	}

	/**
	 * @param numeroOrdineRimborso
	 */
	public void setNumeroOrdineRimborso(Long numeroOrdineRimborso) {
		this.numeroOrdineRimborso = numeroOrdineRimborso;
	}

	/**
	 * @return the fromDataRimbroso
	 */
	public DateTime getFromDataRimbroso() {
		return fromDataRimbroso;
	}

	/**
	 * @param fromDataRimbroso
	 */
	public void setFromDataRimbroso(DateTime fromDataRimbroso) {
		this.fromDataRimbroso = fromDataRimbroso;
	}

	/**
	 * @return the toDataRimbroso
	 */
	public DateTime getToDataRimbroso() {
		return toDataRimbroso;
	}

	/**
	 * @param toDataRimbroso
	 */
	public void setToDataRimbroso(DateTime toDataRimbroso) {
		this.toDataRimbroso = toDataRimbroso;
	}

	/**
	 * @return the idMissione
	 */
	public String getIdMissione() {
		return idMissione;
	}

	/**
	 * @param idMissione
	 */
	public void setIdMissione(String idMissione) {
		this.idMissione = idMissione;
	}

	/**
	 * @return the oggetto
	 */
	public String getOggetto() {
		return oggetto;
	}

	/**
	 * @param oggetto
	 */
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	/**
	 * @return the multiMatchValue
	 */
	public String getMultiMatchValue() {
		return multiMatchValue;
	}

	/**
	 * @param multiMatchValue
	 */
	public void setMultiMatchValue(String multiMatchValue) {
		this.multiMatchValue = multiMatchValue;
	}

	/**
	 * @return the fieldExist
	 */
	public String getFieldExist() {
		return fieldExist;
	}

	/**
	 * @param fieldExist
	 */
	public void setFieldExist(String fieldExist) {
		this.fieldExist = fieldExist;
	}

	/**
	 * @return the fieldNotExist
	 */
	public String getFieldNotExist() {
		return fieldNotExist;
	}

	/**
	 * @param fieldNotExist
	 */
	public void setFieldNotExist(String fieldNotExist) {
		this.fieldNotExist = fieldNotExist;
	}



	/**
	 * @return the fieldMultiMatch
	 */
	public String getFieldMultiMatch() {
		return fieldMultiMatch;
	}

	/**
	 * @param fieldMultiMatch
	 */
	public void setFieldMultiMatch(String fieldMultiMatch) {
		this.fieldMultiMatch = fieldMultiMatch;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the from
	 */
	public int getFrom() {
		return from;
	}

	/**
	 * @param from
	 */
	public void setFrom(int from) {
		this.from = from;
	}

	/**
	 * @return the fieldSort
	 */
	public String getFieldSort() {
		return fieldSort;
	}

	/**
	 * @param fieldSort
	 */
	public void setFieldSort(String fieldSort) {
		this.fieldSort = fieldSort;
	}

	/**
	 * @return the sortOrder
	 */
	public SortOrder getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder
	 */
	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * 
	 * @return
	 */
	private MissioneSearchBuilder self() {
		return this;
	}

}
package it.cnr.missioni.dashboard.component.table.admin;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

import it.cnr.missioni.dashboard.component.table.ITable;
import it.cnr.missioni.model.configuration.TipologiaSpesa;
import it.cnr.missioni.rest.api.response.tipologiaSpesa.TipologiaSpesaStore;

/**
 * @author Salvia Vito
 */
public final class ElencoTipologiaSpesaTable extends ITable.AbstractTable {




	/**
	 * 
	 */
	private static final long serialVersionUID = 5848805564266198608L;

	public ElencoTipologiaSpesaTable() {
		super();
		buildTable();
		

		
	}



	/**
	 * Aggiorna la tabella con la nuova lista derivante dalla query su ES
	 */

	public <T> void aggiornaTable(T tipologiaSpesaStore) {
		this.removeAllItems();

		if (((TipologiaSpesaStore)tipologiaSpesaStore).getTotale() > 0) {

			setVisible(true);
			setContainerDataSource(
					new BeanItemContainer<TipologiaSpesa>(TipologiaSpesa.class, ((TipologiaSpesaStore)tipologiaSpesaStore).getTipologiaSpesa()));

			setVisibleColumns("value","tipo","checkMassimale");
			setColumnHeaders("Descrizione","Tipo","Massimale");
			Object[] properties = { "value" };
			boolean[] ordering = { true };
			sort(properties, ordering);
			setId("id");

		}

	}



	/**
	 * 
	 */
	@Override
	public void addGeneratedColumn() {
		addGeneratedColumn("checkMassimale", new ColumnGenerator() {

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {

				TipologiaSpesa t = (TipologiaSpesa) itemId;
				if (t.isCheckMassimale()) {
					Label l = new Label();
					l.setContentMode(ContentMode.HTML);
					l.setValue(FontAwesome.CHECK.getHtml());
					return l;
				}
				return null;
			}
		});		
	}


}

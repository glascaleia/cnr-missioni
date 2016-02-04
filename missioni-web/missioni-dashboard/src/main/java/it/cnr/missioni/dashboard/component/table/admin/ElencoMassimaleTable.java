package it.cnr.missioni.dashboard.component.table.admin;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;

import it.cnr.missioni.dashboard.component.table.ITable;
import it.cnr.missioni.dashboard.event.DashboardEvent.TableMassimaleUpdatedEvent;
import it.cnr.missioni.model.configuration.Massimale;
import it.cnr.missioni.rest.api.response.massimale.MassimaleStore;

/**
 * @author Salvia Vito
 */
public final class ElencoMassimaleTable extends ITable.AbstractTable {




	/**
	 * 
	 */
	private static final long serialVersionUID = -623292542160514570L;

	public ElencoMassimaleTable() {
		super();
		buildTable();
	}



	/**
	 * Aggiorna la tabella con la nuova lista derivante dalla query su ES
	 */

	public <T> void aggiornaTable(T massimaleStore) {
		this.removeAllItems();

		if (massimaleStore != null) {

			setVisible(true);
			setContainerDataSource(
					new BeanItemContainer<Massimale>(Massimale.class, ((MassimaleStore)massimaleStore).getMassimale()));

			setVisibleColumns("value","descrizione","livello","areaGeografica");
			setColumnHeaders("Importo","Descrizione","Livello","Area Geografica");
			Object[] properties = { "value" };
			boolean[] ordering = { true };
			sort(properties, ordering);
			setId("id");

		}

	}

	/**
	 * 
	 * Aggiorna la table massimale a seguito di un inserimento o modifica
	 * 
	 */
	@Subscribe
	public void aggiornaTableNazione(final TableMassimaleUpdatedEvent tableMassimaleUpdatedEvent) {
		aggiornaTable(tableMassimaleUpdatedEvent.getMassimaleStore());
	}

}
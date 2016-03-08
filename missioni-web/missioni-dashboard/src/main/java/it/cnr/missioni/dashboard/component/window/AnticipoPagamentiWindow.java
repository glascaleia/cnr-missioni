package it.cnr.missioni.dashboard.component.window;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import it.cnr.missioni.dashboard.action.AnticipoPagamentoAction;
import it.cnr.missioni.dashboard.action.UpdateUserAction;
import it.cnr.missioni.dashboard.component.form.anticipoPagamenti.DatiAnticipoPagamentoForm;
import it.cnr.missioni.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import it.cnr.missioni.dashboard.event.DashboardEventBus;
import it.cnr.missioni.dashboard.utility.Utility;
import it.cnr.missioni.model.missione.DatiAnticipoPagamenti;
import it.cnr.missioni.model.missione.Missione;

public class AnticipoPagamentiWindow extends IWindow.AbstractWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5199469615215392155L;
	public static final String ID = "anticipoPagamentiwindow";

	private DatiAnticipoPagamenti anticipoPagamenti;
	private DatiAnticipoPagamentoForm datiAnticipoPagamentoForm;
	private Missione missione;

	private AnticipoPagamentiWindow(final Missione missione,boolean isAdmin,boolean enabled, boolean modifica) {
		super(isAdmin,enabled,modifica);
		this.anticipoPagamenti = missione.getDatiAnticipoPagamenti();
		this.missione = missione;
		setId(ID);
		build();
		buildTab();
		content.addComponent(buildFooter());
		this.setWidth("40%");
	}

	private void buildTab() {
		this.datiAnticipoPagamentoForm = new DatiAnticipoPagamentoForm(anticipoPagamenti,missione.getDatiMissioneEstera().getTrattamentoMissioneEsteraEnum(), isAdmin, enabled, modifica);
		detailsWrapper.addComponent(buildTab("Anagrafica", FontAwesome.USER, this.datiAnticipoPagamentoForm));

	}

	protected Component buildFooter() {

		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -4078143920654711489L;

			@Override
			public void buttonClick(ClickEvent event) {

				try {
					DatiAnticipoPagamenti datiAnticipoPagamenti = datiAnticipoPagamentoForm.validate();
					missione.setDatiAnticipoPagamenti(datiAnticipoPagamenti);
					DashboardEventBus.post(new AnticipoPagamentoAction(missione));

					close();

				} catch (InvalidValueException | CommitException e) {
					Utility.getNotification(Utility.getMessage("error_message"), Utility.getMessage("commit_failed"),
							Type.ERROR_MESSAGE);
				}

			}
		});
		ok.focus();
		footer.addComponent(ok);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
		return footer;
	}

	public static void open(final Missione missione,final boolean isAdmin,final boolean enabled,final boolean modifica) {
		DashboardEventBus.post(new CloseOpenWindowsEvent());
		Window w = new AnticipoPagamentiWindow(missione, isAdmin,enabled,modifica);
		UI.getCurrent().addWindow(w);
		w.focus();
	}

}
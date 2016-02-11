package it.cnr.missioni.dashboard.view.admin;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;

import it.cnr.missioni.dashboard.component.window.DettagliMissioneWindow;
import it.cnr.missioni.dashboard.component.window.DettagliRimborsoWindow;
import it.cnr.missioni.dashboard.component.window.WizardSetupWindow;
import it.cnr.missioni.dashboard.component.window.admin.MissioneWindowAdmin;
import it.cnr.missioni.dashboard.component.wizard.missione.WizardMissione;
import it.cnr.missioni.dashboard.component.wizard.rimborso.WizardRimborso;
import it.cnr.missioni.dashboard.utility.AdvancedFileDownloader;
import it.cnr.missioni.dashboard.utility.AdvancedFileDownloader.AdvancedDownloaderListener;
import it.cnr.missioni.dashboard.utility.AdvancedFileDownloader.DownloaderEvent;
import it.cnr.missioni.dashboard.view.GestioneMissioneView;
import it.cnr.missioni.el.model.search.builder.MissioneSearchBuilder;
import it.cnr.missioni.model.missione.Missione;
import it.cnr.missioni.model.rimborso.Rimborso;

/**
 * @author Salvia Vito
 */
public class GestioneMissioneAdminView extends GestioneMissioneView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8249728987254454501L;

	public GestioneMissioneAdminView() {
		super();
	}

	protected void inizialize() {
		this.missioneSearchBuilder = MissioneSearchBuilder.getMissioneSearchBuilder();
	}

	protected GridLayout addActionButtons() {
		GridLayout layout = new GridLayout(4, 1);
		layout.setSpacing(true);
		
		buttonDettagli = buildButton("Dettagli", "Visualizza i dettagli della Missione", FontAwesome.EDIT);

		buttonDettagli.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				MissioneWindowAdmin.open(selectedMissione);

			}

		});
		buttonRimborso = buildButton("Rimborso", "Visualizza i dettagli del Rimborso", FontAwesome.EURO);
		buttonRimborso.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Rimborso rimborso = null;
				// se è già associato il rimborso
				if (selectedMissione.getRimborso() != null) {
					rimborso = selectedMissione.getRimborso();
					DettagliRimborsoWindow.open(selectedMissione);

				} else {
					rimborso = new Rimborso();
					selectedMissione.setRimborso(rimborso);
					WizardSetupWindow.getWizardSetup().withTipo(new WizardRimborso()).withMissione(selectedMissione)
							.build();
				}

			}

		});

		buttonPDF = buildButton("PDF MISSIONE", "Download del PDF", FontAwesome.FILE_PDF_O);

		final AdvancedFileDownloader downloaderForLink = new AdvancedFileDownloader();
		downloaderForLink.addAdvancedDownloaderListener(new AdvancedDownloaderListener() {
			@Override
			public void beforeDownload(DownloaderEvent downloadEvent) {

				downloaderForLink.setFileDownloadResource(getResource());

			}

		});

		downloaderForLink.extend(buttonPDF);

		buttonVeicoloMissionePDF = buildButton("PDF VEICOLO", "Download del PDF", FontAwesome.FILE_PDF_O);

		final AdvancedFileDownloader veicoloDownloaderForLink = new AdvancedFileDownloader();
		veicoloDownloaderForLink.addAdvancedDownloaderListener(new AdvancedDownloaderListener() {
			@Override
			public void beforeDownload(DownloaderEvent downloadEvent) {

				veicoloDownloaderForLink.setFileDownloadResource(getResourceVeicolo());

			}

		});

		veicoloDownloaderForLink.extend(buttonVeicoloMissionePDF);

		layout.addComponents(buttonDettagli, buttonRimborso, buttonPDF, buttonVeicoloMissionePDF);

		enableDisableButtons(false);

		return layout;

	}
	
	protected void enableDisableButtons(boolean enabled) {
		this.buttonDettagli.setEnabled(enabled);
		this.buttonPDF.setEnabled(enabled);
		this.buttonRimborso.setEnabled(enabled);

		if (selectedMissione != null && selectedMissione.isMezzoProprio())
			buttonVeicoloMissionePDF.setEnabled(true);
		if (selectedMissione == null || !selectedMissione.isMezzoProprio())
			buttonVeicoloMissionePDF.setEnabled(false);

	}

}
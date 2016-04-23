package it.cnr.missioni.dashboard.component.form.missione;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import it.cnr.missioni.dashboard.DashboardUI;
import it.cnr.missioni.dashboard.client.ClientConnector;
import it.cnr.missioni.dashboard.component.form.IForm;
import it.cnr.missioni.dashboard.utility.Utility;
import it.cnr.missioni.el.model.search.builder.IUserSearchBuilder;
import it.cnr.missioni.model.missione.Missione;
import it.cnr.missioni.model.prenotazione.VeicoloCNR;
import it.cnr.missioni.model.user.User;
import it.cnr.missioni.model.user.Veicolo;
import it.cnr.missioni.rest.api.response.user.UserStore;

/**
 * @author Salvia Vito
 */
public interface IVeicoloMissioneForm extends IForm<Missione, IVeicoloMissioneForm> {
    class VeicoloMissioneForm extends IForm.FormAbstract<Missione, IVeicoloMissioneForm>
            implements IVeicoloMissioneForm {

        /**
         *
         */
        private static final long serialVersionUID = -6978603374633816039L;
        private static final String VEICOLO_CNR = "Autovettura di servizio";
        private static final String VEICOLO_PROPRIO = "Veicolo Proprio";
        private static final String NESSUNO = "Nessuno";
        private static final String NOLEGGIO = "Noleggio";
        boolean veicoloPrincipaleSetted = false;
        private TextArea motivazioneField;
        private TextArea altreDisposizioniField;
        private OptionGroup optionGroupMezzo;
        private Label labelVeicoloProprio;
        private Veicolo v = null;
        private User user;

        private VeicoloMissioneForm() {
        }

        public static IVeicoloMissioneForm getVeicoloMissioneForm() {
            return new VeicoloMissioneForm();
        }

        public IVeicoloMissioneForm build() {
            setFieldGroup(new BeanFieldGroup<Missione>(Missione.class));
            buildFieldGroup();
            buildTab();
            return self();
        }

        public void buildTab() {

            // se non è Admin prendo come user quello loggato al sistema
            if (!isAdmin) {
                user = DashboardUI.getCurrentUser();
            } else {
                try {
                    IUserSearchBuilder userSearchBuilder = IUserSearchBuilder.UserSearchBuilder.getUserSearchBuilder()
                            .withId(bean.getIdUser());
                    UserStore userStore = ClientConnector.getUser(userSearchBuilder);
                    user = userStore.getUsers().get(0);

                } catch (Exception e) {
                    Utility.getNotification(Utility.getMessage("error_message"), Utility.getMessage("request_error"),
                            Type.ERROR_MESSAGE);
                }
            }
            v = user.getVeicoloPrincipale();
            optionGroupMezzo = new OptionGroup("Veicolo");
            optionGroupMezzo.addItems(VEICOLO_CNR, VEICOLO_PROPRIO, NOLEGGIO, NESSUNO);
            if(bean.getTipoVeicolo().equals("Veicolo CNR"))
            	bean.setTipoVeicolo(VEICOLO_CNR);
            optionGroupMezzo.select(bean.getTipoVeicolo());
            optionGroupMezzo.setReadOnly(!enabled);

            motivazioneField = (TextArea) getFieldGroup().buildAndBind("Motivazione", "motivazioni", TextArea.class);
            motivazioneField.setReadOnly(
                    (bean.getTipoVeicolo() == VEICOLO_PROPRIO || bean.getTipoVeicolo() == NOLEGGIO) ? false : true);

            altreDisposizioniField = (TextArea) getFieldGroup().buildAndBind("Altre Disposizioni", "altreDisposizioni",
                    TextArea.class);
            labelVeicoloProprio = new Label();
            // Visualizza il veicolo principale dell'user
            if (bean.isMezzoProprio()) {
                labelVeicoloProprio.setValue("Veicolo: " + v.getTipo() + " Targa: " + v.getTarga());
                labelVeicoloProprio.setVisible(true);
            } else {
                labelVeicoloProprio.setVisible(false);
            }
            addComponent(optionGroupMezzo);
            addComponent(labelVeicoloProprio);
            addComponent(motivazioneField);
            addComponent(altreDisposizioniField);
            addListener();
            addValidator();
        }

        public Missione validate() throws CommitException, InvalidValueException {
            getFieldGroup().getFields().stream().forEach(f -> {
                ((AbstractField<?>) f).setValidationVisible(true);
            });
            getFieldGroup().commit();
            BeanItem<Missione> beanItem = (BeanItem<Missione>) getFieldGroup().getItemDataSource();
            bean = beanItem.getBean();
            bean.setTipoVeicolo(optionGroupMezzo.getValue().toString());
            bean.setMezzoProprio(optionGroupMezzo.getValue().equals(VEICOLO_PROPRIO) ? true : false);
            return bean;
        }

        /**
         *
         */
        @Override
        public void addValidator() {
            optionGroupMezzo.addValidator(new Validator() {

                /**
                 *
                 */
                private static final long serialVersionUID = -4519173795761278326L;

                // Verifica se è presenta almeno un veicolo proprio per l'utente
                @Override
                public void validate(Object value) throws InvalidValueException {

                    if (value.equals(VEICOLO_PROPRIO)) {
                        user.getMappaVeicolo().values().forEach(veicolo -> {
                            if (veicolo.isVeicoloPrincipale())
                                veicoloPrincipaleSetted = true;
                        });

                        // se non è presnte un veicolo settato come principale
                        if (!veicoloPrincipaleSetted)
                            throw new InvalidValueException(Utility.getMessage("no_veicolo"));
                    }
                }
            });

            // Se veicolo è proprio motivazione è obbligatoria
            motivazioneField.addValidator(new Validator() {

                /**
                 *
                 */
                private static final long serialVersionUID = -1375538750658054368L;

                @Override
                public void validate(Object value) throws InvalidValueException {

                    if (motivazioneField.getValue() == null && (optionGroupMezzo.getValue().equals(VEICOLO_PROPRIO)
                            || optionGroupMezzo.getValue().equals(NOLEGGIO)))
                        throw new InvalidValueException(Utility.getMessage("altre_disposizioni_error"));
                }
            });

        }

        /**
         *
         */
        @Override
        public void addListener() {
            optionGroupMezzo.addValueChangeListener(new ValueChangeListener() {

                /**
                 *
                 */
                private static final long serialVersionUID = -1526024942878895542L;

                @Override
                public void valueChange(ValueChangeEvent event) {
                    chooseTipoVeicolo(optionGroupMezzo.getValue().toString());
                }
            });
        }

        private void chooseTipoVeicolo(String tipo) {
            switch (tipo) {
                case VEICOLO_CNR:
                case NESSUNO:
                    motivazioneField.setReadOnly(false);
                    motivazioneField.setValue(null);
                    motivazioneField.setReadOnly(true);
                    motivazioneField.setValidationVisible(false);
                    labelVeicoloProprio.setVisible(false);
                    break;
                case VEICOLO_PROPRIO:
                    motivazioneField.setReadOnly(false);
                    labelVeicoloProprio.setVisible(true);
                    labelVeicoloProprio.setValue(
                            v != null ? "Veicolo: " + v.getTipo() + " Targa: " + v.getTarga() : "Nessun veicolo associato");
                    break;
                case NOLEGGIO:
                    motivazioneField.setReadOnly(false);
                    labelVeicoloProprio.setVisible(false);
                    break;
                default:
                    break;
            }
        }

        /**
         * @return
         */
        @Override
        protected IVeicoloMissioneForm self() {
            return this;
        }

    }

}

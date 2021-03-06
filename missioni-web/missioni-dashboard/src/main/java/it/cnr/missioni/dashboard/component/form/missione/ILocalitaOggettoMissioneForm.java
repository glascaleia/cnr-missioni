package it.cnr.missioni.dashboard.component.form.missione;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import it.cnr.missioni.dashboard.client.ClientConnector;
import it.cnr.missioni.dashboard.component.form.IForm;
import it.cnr.missioni.dashboard.utility.Utility;
import it.cnr.missioni.el.model.search.builder.INazioneSearchBuilder;
import it.cnr.missioni.model.missione.Missione;
import it.cnr.missioni.model.missione.StatoEnum;
import it.cnr.missioni.rest.api.response.geocoder.GeocoderStore;
import it.cnr.missioni.rest.api.response.nazione.NazioneStore;
import org.elasticsearch.common.geo.GeoPoint;

/**
 * @author Salvia Vito
 */
public interface ILocalitaOggettoMissioneForm extends IForm<Missione, ILocalitaOggettoMissioneForm> {

    /**
     * @param visible
     */
    void setVisibleListaNazione(boolean visible);

    class LocalitaOggettoMissioneForm extends IForm.FormAbstract<Missione, ILocalitaOggettoMissioneForm>
            implements ILocalitaOggettoMissioneForm {

        /**
         *
         */
        private static final long serialVersionUID = -8610795278738351308L;
        private TextField localitaField;
        private TextField oggettoField;
        private ComboBox listaLocalitaField;
        private GeocoderStore geocoderStore;
        private TextField distanzaField;
        private ComboBox listaNazioneField;
        private ComboBox listaStatoField;
        private TextArea altreLocalita;
        private CheckBox obbligoGiornaliero;

        private LocalitaOggettoMissioneForm() {
        }

        public static ILocalitaOggettoMissioneForm getDatiPeriodoMissioneForm() {
            return new LocalitaOggettoMissioneForm();
        }

        public ILocalitaOggettoMissioneForm build() {
            setFieldGroup(new BeanFieldGroup<Missione>(Missione.class));
            buildFieldGroup();
            buildTab();
            return self();
        }

        public void setVisibleListaNazione(boolean visible) {
            listaNazioneField.setVisible(visible);
        }

        public void buildTab() {
            localitaField = new TextField("Localita");
            listaLocalitaField = new ComboBox("Seleziona località");
            listaLocalitaField.setValidationVisible(false);
            if (modifica) {
                listaLocalitaField.addItem(bean.getGeoPoint().getLat() + "/" + bean.getGeoPoint().getLon());
                listaLocalitaField.setItemCaption(bean.getGeoPoint().getLat() + "/" + bean.getGeoPoint().getLon(),
                        bean.getLocalita());
                listaLocalitaField.select(bean.getGeoPoint().getLat() + "/" + bean.getGeoPoint().getLon());
                if (!isAdmin)
                    listaLocalitaField.setReadOnly(true);
            }
            oggettoField = (TextField) getFieldGroup().buildAndBind("Oggetto", "oggetto");
            distanzaField = (TextField) getFieldGroup().buildAndBind("Distanza", "distanza");
            distanzaField.setEnabled(false);
            altreLocalita = (TextArea) getFieldGroup().buildAndBind("Altre localita", "altreLocalita", TextArea.class);
            obbligoGiornaliero = (CheckBox) getFieldGroup().buildAndBind("Obbligo Giornaliero", "obbligoGiornaliero", CheckBox.class);

            // if(bean.isMissioneEstera())
            // buildFields();
            HorizontalLayout layoutLocalita = new HorizontalLayout();
            layoutLocalita.addComponent(localitaField);
            if (isAdmin) {
                listaStatoField = (ComboBox) getFieldGroup().buildAndBind("Stato", "stato", ComboBox.class);
                addComponent(listaStatoField);
            }
            addComponent(localitaField);
            addComponent(listaLocalitaField);
            addComponent(obbligoGiornaliero);
            addComponent(oggettoField);
            addComponent(distanzaField);
            addComponent(altreLocalita);
            buildFields();
            addComponent(listaNazioneField);
            addListener();
            addValidator();
        }

        private void buildFields() {
            listaNazioneField = new ComboBox("Nazione");
            localitaField.setReadOnly(!enabled);
            listaNazioneField.setImmediate(true);
            listaNazioneField.setValidationVisible(false);
            getFieldGroup().bind(listaNazioneField, "idNazione");
            try {
                NazioneStore nazioneStore = ClientConnector
                        .getNazione(INazioneSearchBuilder.NazioneSearchBuilder.getNazioneSearchBuilder().withAll(true));
                nazioneStore.getNazione().forEach(c -> {
                    listaNazioneField.addItem(c.getId());
                    listaNazioneField.setItemCaption(c.getId(), c.getValue());
                });
            } catch (Exception e) {
                Utility.getNotification(Utility.getMessage("error_message"), Utility.getMessage("request_error"),
                        Type.ERROR_MESSAGE);
            }
        }

        public void addListener() {

            listaLocalitaField.addBlurListener(new BlurListener() {

                /**
                 *
                 */
                private static final long serialVersionUID = 7311640376669034824L;

                @Override
                public void blur(BlurEvent event) {
                    try {
                        listaLocalitaField.validate();
                    } catch (Exception e) {
                        listaLocalitaField.setValidationVisible(true);
                    }
                }
            });

            listaNazioneField.addBlurListener(new BlurListener() {

                /**
                 *
                 */
                private static final long serialVersionUID = 7922449070519357247L;

                @Override
                public void blur(BlurEvent event) {
                    try {
                        listaNazioneField.validate();
                    } catch (Exception e) {
                        listaNazioneField.setValidationVisible(true);
                    }
                }
            });

            listaLocalitaField.addBlurListener(new BlurListener() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1427910449983002609L;

                @Override
                public void blur(BlurEvent event) {
                    try {
                        String localita = listaLocalitaField.getItemCaption(listaLocalitaField.getValue());
                        if (localita != null) {
                            Double distance = ClientConnector.getNewDistanceForMissione(
                                    "Tito Scalo Potenza", localita);
                            if (distance == -1)
                                Utility.getNotification(Utility.getMessage("error_message"),
                                        Utility.getMessage("no_distance"), Type.ERROR_MESSAGE);
                            else
                                distanzaField.setValue(String.valueOf(distance.intValue()));
                        } else {
                            distanzaField.setValue(null);
                        }
                    } catch (Exception e) {
                        Utility.getNotification(Utility.getMessage("error_message"),
                                Utility.getMessage("request_error"), Type.ERROR_MESSAGE);
                    }
                }
            });

            localitaField.addValueChangeListener(new ValueChangeListener() {

                /**
                 *
                 */
                private static final long serialVersionUID = -3920108318148178176L;

                @Override
                public void valueChange(ValueChangeEvent event) {

                    listaLocalitaField.removeAllItems();

                    if (!localitaField.getValue().trim().equals("")) {
                        try {
                            geocoderStore = ClientConnector
                                    .getGeocoderStoreForMissioneLocation(localitaField.getValue());
                            if (geocoderStore.getGeocoderResponses() != null) {
                                geocoderStore.getGeocoderResponses().forEach(c -> {
                                    listaLocalitaField.addItem(c.getLat() + "/" + c.getLon());
                                    listaLocalitaField.setItemCaption(c.getLat() + "/" + c.getLon(),
                                            c.getFormattedAddress());
                                });
                            } else {
                                Utility.getNotification(Utility.getMessage("error_message"),
                                        Utility.getMessage("localita_error"), Type.ERROR_MESSAGE);
                            }
                        } catch (Exception e) {
                            Utility.getNotification(Utility.getMessage("error_message"),
                                    Utility.getMessage("request_error"), Type.ERROR_MESSAGE);
                        }
                    }
                }
            });
        }

        public Missione validate() throws CommitException, InvalidValueException {
            getFieldGroup().getFields().stream().forEach(f -> {
                ((AbstractField<?>) f).setValidationVisible(true);
            });
            listaLocalitaField.setValidationVisible(true);
            listaLocalitaField.validate();
            getFieldGroup().commit();
            // oggettoField.validate();
            listaNazioneField.validate();
            // bean.setOggetto(oggettoField.getValue());
            bean.setLocalita(listaLocalitaField.getItemCaption(listaLocalitaField.getValue()));
            // bean.setDistanza(distanzaField.getValue());
            // bean.setAltreLocalita(altreLocalita.getValue());
            // Missione estera
            if (bean.isMissioneEstera()) {
                bean.setIdNazione(listaNazioneField.getValue().toString());
                bean.setShortDescriptionNazione(listaNazioneField.getItemCaption(bean.getIdNazione()));
            } else {
                bean.setIdNazione(null);
                bean.setShortDescriptionNazione(null);
            }
            if (isAdmin)
                bean.setStato(StatoEnum.getStatoE(listaStatoField.getValue().toString()));

            String[] latLng = ((String) listaLocalitaField.getValue()).split("/");
            GeoPoint geoPoint = new GeoPoint(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
            bean.setGeoPoint(geoPoint);
            bean.setLocalita(listaLocalitaField.getItemCaption(listaLocalitaField.getValue()));
            return bean;
        }

        /**
         *
         */
        @Override
        public void addValidator() {
            listaNazioneField.addValidator(new Validator() {

                /**
                 *
                 */
                private static final long serialVersionUID = 2212244745005231496L;

                @Override
                public void validate(Object value) throws InvalidValueException {

                    if (value == null && bean.isMissioneEstera())
                        throw new InvalidValueException(Utility.getMessage("nazione_error"));
                }

            });

            listaLocalitaField.addValidator(new Validator() {

                /**
                 *
                 */
                private static final long serialVersionUID = 621159309875209551L;

                @Override
                public void validate(Object value) throws InvalidValueException {

                    if (value == null)
                        throw new InvalidValueException(Utility.getMessage("field_required"));
                }

            });

        }

        /**
         * @return
         */
        @Override
        protected ILocalitaOggettoMissioneForm self() {
            return this;
        }

    }

}

package it.cnr.missioni.notification.message.factory.dev;

import it.cnr.missioni.notification.bridge.implementor.MissioniMailImplementor;
import it.cnr.missioni.notification.message.AddMissioneMessage;
import it.cnr.missioni.notification.message.AddRimborsoMessage;
import it.cnr.missioni.notification.message.UpdateMissioneMessage;
import it.cnr.missioni.notification.message.factory.NotificationMessageFactory;
import it.cnr.missioni.notification.support.itext.PDFBuilder;
import org.geosdi.geoplatform.configurator.bootstrap.Develop;
import org.springframework.stereotype.Component;

/**
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
@Develop
@Component(value = "notificationMessageDevFactory")
public class NotificationMessageDevFactory implements NotificationMessageFactory {

    /**
     * @param userName
     * @param userSurname
     * @param userEmail
     * @param cnrMissioniEmail
     * @param pdfBuilder
     * @return {@link String AddMissioneMessage}
     */
    @Override
    public AddMissioneMessage buildAddMissioneMessage(String userName, String userSurname, String userEmail,
            String cnrMissioniEmail, PDFBuilder pdfBuilder) {
        return new AddMissioneMessage(userName, userSurname, userEmail, cnrMissioniEmail, pdfBuilder) {

            /**
             * @return {@link MissioniMailImplementor.NotificationMessageType}
             */
            @Override
            public MissioniMailImplementor.NotificationMessageType getNotificationMessageType() {
                return MissioniMailImplementor.NotificationMessageType.AGGIUNGI_MISSIONE_MAIL_DEV;
            }
        };
    }

    /**
     * @param userName
     * @param userSurname
     * @param userEmail
     * @param cnrMissioniEmail
     * @return {@link  UpdateMissioneMessage}
     */
    @Override
    public UpdateMissioneMessage buildUpdateMissioneMessage(String userName, String userSurname, String stato,String userEmail, String missioneID, PDFBuilder pdfBuilder) {
        return new UpdateMissioneMessage(userName, userSurname,stato, userEmail,
                missioneID, pdfBuilder) {

            /**
             * @return {@link MissioniMailImplementor.NotificationMessageType}
             */
            @Override
            public MissioniMailImplementor.NotificationMessageType getNotificationMessageType() {
                return MissioniMailImplementor.NotificationMessageType.MODIFICA_MISSIONE_MAIL_DEV;
            }
        };
    }

    /**
     * @param userName
     * @param userSurname
     * @param userEmail
     * @param cnrMissioniEmail
     * @param missioneID
     * @param pdfBuilder
     * @return {@link AddRimborsoMessage}
     */
    @Override
    public AddRimborsoMessage buildAddRimborsoMessage(String userName, String userSurname,
            String userEmail, String cnrMissioniEmail, String missioneID, PDFBuilder pdfBuilder) {
        return new AddRimborsoMessage(userName, userSurname, userEmail, cnrMissioniEmail, missioneID, pdfBuilder) {

            @Override
            public MissioniMailImplementor.NotificationMessageType getNotificationMessageType() {
                return MissioniMailImplementor.NotificationMessageType.RICHIESTA_RIMBORSO_MISSIONE_MAIL_DEV;
            }
        };
    }
}

package it.cnr.missioni.notification.bridge.implementor.dev;

import it.cnr.missioni.notification.task.IMissioniMailNotificationTask;
import org.apache.velocity.app.VelocityEngine;
import org.geosdi.geoplatform.support.mail.configuration.detail.GPMailDetail;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
public class AddMissioneMailDev extends MissioniMailDev {

    /**
     * @param message
     * @param velocityEngine
     * @param gpMailDetail
     * @return {@link String}
     * @throws Exception
     */
    @Override
    public String prepareMessage(IMissioniMailNotificationTask.IMissioneNotificationMessage message,
            VelocityEngine velocityEngine, GPMailDetail gpMailDetail) throws Exception {
        String userName = (String) message.getMessageParameters().get("userName");
        String userSurname = (String) message.getMessageParameters().get("userSurname");

        Map model = new HashMap();
        model.put("userName", userName);
        model.put("userSurname", userSurname);
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                "template/addMissioneMailNotification.html.vm", "UTF-8", model);
    }

    /**
     * @return {@link ImplementorKey}
     */
    @Override
    public ImplementorKey getKey() {
        return NotificationMessageType.AGGIUNGI_MISSIONE_MAIL_DEV;
    }

    /**
     * <p>
     * Specify if {@link ImplementorKey} is valid or not
     * </p>
     *
     * @return {@link Boolean}
     */
    @Override
    public Boolean isImplementorValid() {
        return Boolean.TRUE;
    }
}
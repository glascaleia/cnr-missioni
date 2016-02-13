package it.cnr.missioni.notification.bridge.implementor.prod;

import it.cnr.missioni.notification.bridge.implementor.Implementor.ImplementorKey;
import it.cnr.missioni.notification.bridge.implementor.MissioniMailImplementor.NotificationMessageType;
import it.cnr.missioni.notification.message.preparator.IMissioniMessagePreparator;
import it.cnr.missioni.notification.support.itext.PDFBuilder;
import it.cnr.missioni.notification.task.IMissioniMailNotificationTask;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.geosdi.geoplatform.support.mail.configuration.detail.GPMailDetail;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
public class UpdateRimborsoMissioneMailProd extends MissioniMailProd {

    /**
     * @param message
     * @param velocityEngine
     * @param gpMailDetail
     * @return {@link MimeMessagePreparator}
     * @throws Exception
     */
    @Override
    public IMissioniMessagePreparator prepareMessage(IMissioniMailNotificationTask.IMissioneNotificationMessage message,
            VelocityEngine velocityEngine, GPMailDetail gpMailDetail) throws Exception {
        IMissioniMessagePreparator missioniMessagePreparator = super.createMissioniMessagePreparator();
        missioniMessagePreparator.setMimeMessagePreparator(new MimeMessagePreparator() {

            String userName = (String) message.getMessageParameters().get("userName");
            String userSurname = (String) message.getMessageParameters().get("userSurname");
            String userEmail = (String) message.getMessageParameters().get("userEmail");
            String rimborsoID = (String) message.getMessageParameters().get("rimborsoID");
            String stato = (String) message.getMessageParameters().get("stato");
            PDFBuilder pdfBuilder = (PDFBuilder) message.getMessageParameters().get("rimborsoPDFBuilder");

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = createMimeMessageHelper(mimeMessage, gpMailDetail, Boolean.TRUE);
                message.setTo(new String[]{userEmail});
                Map model = new HashMap();
                model.put("userName", userName);
                model.put("userSurname", userSurname);
                model.put("rimborsoID", rimborsoID);
                model.put("stato", stato);

                String messageText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                        "template/updateRimborsoMissioneMailNotification.html.vm", "UTF-8", model);
                message.setText(messageText, Boolean.TRUE);

                Path tempFilePath = Files.createTempFile("Rimborso-Missione - ".concat(userName), ".pdf");
                File file = tempFilePath.toFile();

                pdfBuilder.withFile(file);
                pdfBuilder.build();
                message.addAttachment(file.getName(), file);
                missioniMessagePreparator.addAttachment(file);

            }
        });
        return missioniMessagePreparator;
    }

    /**
     * @return {@link ImplementorKey}
     */
    @Override
    public ImplementorKey getKey() {
        return NotificationMessageType.MODIFICA_RIMBORSO_MISSIONE_MAIL_PROD;
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
package uk.nhs.digital.ps;

import org.onehippo.cms7.event.HippoEvent;
import org.onehippo.cms7.event.HippoEventConstants;
import org.onehippo.cms7.services.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;

public class AuditEventListener {
    private static Logger log = LoggerFactory.getLogger(AttachmentTextEraserDerivedDataFunction.class);

    @Subscribe
    public void handleEvent(HippoEvent event) {
        if (HippoEventConstants.CATEGORY_WORKFLOW.equals(event.category())) {

            Instant timestamp = Instant.ofEpochMilli(event.timestamp());

            String action = null;
            switch (event.action()) {
                case "obtainEditableInstance":
                    action = "startEdit";
                    break;
                case "commitEditableInstance":
                    action = "saveDocument";
                    break;
                default:
                    action = event.action();
            }

            Map<String, Object> values = event.getValues();

            log.info(
                "AUDIT_EVENT"
                + " time=" + timestamp
                + ", user=" + event.user()
                + ", action=" + action
                + ", doctype=" + values.get("documentType")
                + ", document=" + values.get("documentPath")
            );


//            for (Map.Entry<String, Object> entry : values.entrySet()) {
//                String key = entry.getKey();
//                Object value = entry.getValue();
//                log.info(key + ": " + value);
//            }
        }
    }
}

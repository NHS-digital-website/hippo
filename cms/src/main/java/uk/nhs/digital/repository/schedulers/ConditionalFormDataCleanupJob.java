package uk.nhs.digital.repository.schedulers;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;

import org.apache.commons.lang.*;
import org.hippoecm.repository.util.*;
import org.onehippo.repository.scheduling.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;
import javax.jcr.*;
import javax.jcr.query.*;

/**
 * This class is almost a exact copy of org.onehippo.cms7.hst.toolkit.addon.formdata.FormDataCleanupJob
 * After processing the exluded paths in the removeOldFormData method, is checking
 * if the form data data behavior has been executed correctly
 */
public class ConditionalFormDataCleanupJob implements RepositoryJob {

    private static final Logger log = LoggerFactory.getLogger(ConditionalFormDataCleanupJob.class);

    private static final String CONFIG_MINUTES_TO_LIVE = "minutestolive";
    private static final String CONFIG_BATCH_SIZE = "batchsize";
    private static final String CONFIG_EXCLUDE_PATHS = "excludepaths";

    private static final String FORMDATA_QUERY = "SELECT * FROM hst:formdata ORDER BY hst:creationtime ASC";

    @Override
    public void execute(final RepositoryJobExecutionContext context) throws RepositoryException {
        log.warn("Running form data cleanup job");
        final Session session = context.createSystemSession();
        try {
            long minutesToLive = Long.parseLong(context.getAttribute(CONFIG_MINUTES_TO_LIVE));
            long batchSize;
            try {
                batchSize = Long.parseLong(context.getAttribute(CONFIG_BATCH_SIZE));
            } catch (NumberFormatException e) {
                log.warn("Incorrect batch size '" + context.getAttribute(CONFIG_BATCH_SIZE) + "'. Setting default to 100");
                batchSize = 100;
            }
            String[] excludePaths = context.getAttribute(CONFIG_EXCLUDE_PATHS).split("\\|");
            removeOldFormData(minutesToLive, batchSize, excludePaths, session);
        } finally {
            session.logout();
        }
    }

    private void removeOldFormData(long minutesToLive,
                                   final long batchSize,
                                   final String[] excludePaths,
                                   final Session session) throws RepositoryException {
        final QueryManager queryManager = session.getWorkspace().getQueryManager();
        final Query query = queryManager.createQuery(FORMDATA_QUERY, Query.SQL);
        final NodeIterator nodes = query.execute().getNodes();
        final long tooOldTimeStamp = System.currentTimeMillis() - minutesToLive * 60 * 1000L;
        int count = 0;
        outer:
        while (nodes.hasNext()) {
            try {
                final Node node = nodes.nextNode();
                if (node.getProperty("hst:creationtime").getDate().getTimeInMillis() > tooOldTimeStamp) {
                    break outer;
                }
                for (String path : excludePaths) {
                    if (!"".equals(path) && node.getPath().startsWith(path)) {
                        continue outer;
                    }
                }
                //in case the form behavior hasn't been successfully completed, don't clean the log
                String hstPayload =
                    JcrUtils.getStringProperty(node, "hst:payload", StringUtils.EMPTY);
                if (StringUtils.isNotEmpty(hstPayload)) {
                    try {
                        //create ObjectMapper instance needed to obtain FormField
                        ObjectMapper objectMapper = new ObjectMapper();
                        //deserializing the json object according to org.hippoecm.hst.component.support.forms.FormMap
                        HashMap<String, FormField> emp = objectMapper.readValue(hstPayload, new TypeReference<HashMap<String, FormField>>() {
                        });
                        //fetching the eforms_process_done
                        FormField formField = emp.get("eforms_process_done");
                        if (formField == null) {
                            continue outer;
                        }
                        List<String> valueList = formField.getValueList();
                        if (valueList.isEmpty() || valueList.stream().allMatch(t -> t.toLowerCase().equals("false"))) {
                            //in case the valueList is empty or the one of the values is false, something went wrong with the one of the form data behavior
                            log.error("Something went wrong while deleting form data {}", node.getPath());
                            continue outer;
                        }
                    } catch (IOException exception) {
                        log.error("Something went wrong while fetching form data", exception);
                    }
                }

                log.warn("Form data cleanup item: {}", node.getPath());
                remove(node, 2);
                if (count++ % batchSize == 0) {
                    session.save();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                        log.debug("ignored ", ignored);
                    }
                }
            } catch (RepositoryException e) {
                log.error("Error while cleaning up form data", e);
            }
        }
        if (session.hasPendingChanges()) {
            session.save();
        }
        if (count > 0) {
            log.info("Done cleaning " + count + " items");
        } else {
            log.info("No timed out items");
        }
    }

    private void remove(final Node node, int ancestorsToRemove) throws RepositoryException {
        final Node parent = node.getParent();
        node.remove();
        if (ancestorsToRemove > 0 && parent != null && parent.getName().length() == 1 && parent.isNodeType("hst:formdatacontainer") && parent.getNodes().getSize() == 0) {
            remove(parent, ancestorsToRemove - 1);
        }
    }

}


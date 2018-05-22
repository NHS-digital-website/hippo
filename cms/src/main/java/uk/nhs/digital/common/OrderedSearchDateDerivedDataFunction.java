package uk.nhs.digital.common;

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

import org.apache.jackrabbit.value.LongValue;
import org.hippoecm.repository.ext.DerivedDataFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Map;
import javax.jcr.Value;

/**
 * Derived data function that turns the nominal publication date into a long that when
 * sorted, will have all published documents first in descending order followed by
 * upcoming documents in ascending date order
 */
public class OrderedSearchDateDerivedDataFunction extends DerivedDataFunction {

    private static final long serialVersionUID = 1;
    private static final Logger log = LoggerFactory.getLogger(OrderedSearchDateDerivedDataFunction.class);

    @Override
    public Map<String, Value[]> compute(Map<String, Value[]> parameters) {
        try {
            Value[] dateValues = parameters.get("date");
            if (isEmpty(dateValues)) {
                return emptyMap();
            }

            Value[] publiclyAccessibleValues = parameters.get("publiclyAccessible");

            Calendar date = dateValues[0].getDate();
            boolean publiclyAccessible = isEmpty(publiclyAccessibleValues) || publiclyAccessibleValues[0].getBoolean();

            long orderedSearchDate = date.getTimeInMillis();
            if (!publiclyAccessible) {
                orderedSearchDate *= -1;
            }

            parameters.put("orderedSearchDate", new Value[]{new LongValue(orderedSearchDate)});

            return parameters;
        } catch (Exception ex) {
            log.error("Failed to compute Ordered Search Date", ex);

            return emptyMap();
        }
    }
}

package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import uk.nhs.digital.website.beans.EffectiveDate;
import uk.nhs.digital.website.beans.Roadmap;
import uk.nhs.digital.website.beans.RoadmapItem;

import java.util.*;
import java.util.stream.Collectors;

public class RoadmapLinkedBeansComponent extends ContentRewriterComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        request.setAttribute("orderBy", getSortByOrDefault(getPublicRequestParameter(request, "order-by")));
        request.setAttribute("selectedTypes", Arrays.asList(getPublicRequestParameters(request, "type")));

        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));

        Object bean = request.getAttribute(REQUEST_ATTR_DOCUMENT);
        if (bean instanceof HippoBean) {
            Roadmap roadmapDoc = (Roadmap) bean;
            List<RoadmapItem> items = roadmapDoc.getItem();
            items = items.stream()
                .sorted(Comparator.comparing(RoadmapItem::getEffectiveDate, Comparator.comparing(EffectiveDate::getStartDate)))
                .collect(Collectors.toList());

            HashMap<String, List<RoadmapItem>> categorisedItems = new HashMap<>();
            for (RoadmapItem item : items) {
                String category = item.getCategoryLink();

                List<RoadmapItem> categoryItems = categorisedItems.get(category);
                if (categoryItems == null) {
                    categoryItems = new ArrayList<>();
                }
                categoryItems.add(item);
                categorisedItems.put(category, categoryItems);
            }

            request.setAttribute("roadmapItems", categorisedItems);

            String granularity = roadmapDoc.getGranularity();

            List<Calendar> roadmapDates = new ArrayList<>();

            RoadmapItem firstItem = items.get(0);

            Calendar startDate = Calendar.getInstance();
            startDate.setTime(firstItem.getEffectiveDate().getStartDate().getTime());

            int increment = 1;
            if (granularity.equals("Quarterly")) {
                increment = 3;
                int quarterStartMonth = (startDate.get(Calendar.MONTH) / 3) * 3;
                startDate.set(Calendar.MONTH, quarterStartMonth);
            } else if (granularity.equals("Yearly")) {
                increment = 12;
                startDate.set(Calendar.DAY_OF_YEAR, 1);
            }

            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(startDate.getTime());
            currentDate.set(Calendar.DAY_OF_MONTH, 1);

            Optional<RoadmapItem> lastItemOptional =
                items.stream()
                .sorted(Comparator.comparing(RoadmapItem::getEffectiveDate, Comparator.comparing(EffectiveDate::getEndDate)))
                .reduce((a, b) -> b);

            if (lastItemOptional.isPresent()) {
                RoadmapItem lastItem = lastItemOptional.get();

                Calendar endDate = lastItem.getEffectiveDate().getEndDate();
                while (currentDate.before(endDate) || currentDate.equals(endDate)) {
                    roadmapDates.add(currentDate);
                    Calendar nextDate = Calendar.getInstance();
                    nextDate.setTime(currentDate.getTime());
                    nextDate.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) + increment);
                    currentDate = nextDate;
                }

                request.setAttribute("roadmapItemsOptions", roadmapItemsOptions(items, granularity));
            }

            request.setAttribute("roadmapDates", roadmapDates);
        }
    }

    private String getSortByOrDefault(String order) {
        if (order != null && order.equalsIgnoreCase("start-date")) {
            return "startDate";
        }
        return "endDate";
    }

    private Map<String, Object> roadmapItemsOptions(List<RoadmapItem> roadmapItems, String granularity) {
        Map<String, Object> roadmapOptions = new HashMap<>();

        roadmapItems.forEach(item -> {
            Map<String, Object> itemOptions = new HashMap<>();

            Calendar startDate = item.getEffectiveDate().getStartDate();
            Calendar endDate = item.getEffectiveDate().getEndDate();

            double periodOffset = 0;
            double periodPartialLength = 1;
            int periods = 1;
            int verticalPosition = 1;

            if (granularity.equals("Yearly")) {
                Calendar mycal = new GregorianCalendar(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), 1);
                int daysInYear = mycal.getActualMaximum(Calendar.DAY_OF_YEAR);

                periodOffset = startDate.get(Calendar.DAY_OF_YEAR) / (double) daysInYear;

                mycal = new GregorianCalendar(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), 1);
                daysInYear = mycal.getActualMaximum(Calendar.DAY_OF_YEAR);

                long endDays = daysInYear - endDate.get(Calendar.DAY_OF_YEAR);

                periodPartialLength = 1 - (endDays / (double) daysInYear) - periodOffset;

                periods = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR) + 1;
            } else if (granularity.equals("Quarterly")) {
                Calendar quarterStartDate = Calendar.getInstance();
                quarterStartDate.setTime(startDate.getTime());
                int quarterStartMonth = startDate.get(Calendar.MONTH) / 3 * 3;
                quarterStartDate.set(Calendar.MONTH, quarterStartMonth);
                quarterStartDate.set(Calendar.DAY_OF_MONTH, 1);

                long startDateDifference = startDate.getTime().getTime() - quarterStartDate.getTime().getTime();
                int startDays = (int) (startDateDifference / (1000 * 60 * 60 * 24));

                int daysInStartQuarter = getDaysInQuarter(quarterStartDate);

                periodOffset = startDays / (double) daysInStartQuarter;

                Calendar quarterEndDate = Calendar.getInstance();
                quarterEndDate.setTime(endDate.getTime());
                int quarterEndMonth = (endDate.get(Calendar.MONTH) / 3) * 3 + 3;
                quarterEndDate.set(Calendar.MONTH, quarterEndMonth);
                quarterEndDate.set(Calendar.DAY_OF_MONTH, 0);

                int daysInEndQuarter = getDaysInQuarter(quarterEndDate);

                long endDateDifference = quarterEndDate.getTime().getTime() - endDate.getTime().getTime();
                int endDays = (int) (endDateDifference / (1000 * 60 * 60 * 24));

                periodPartialLength = 1 - (endDays / (double) daysInEndQuarter) - periodOffset;

                periods = endDate.get(Calendar.YEAR) * 12 - startDate.get(Calendar.YEAR) * 12;
                periods += endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
                double quarters = Math.ceil((periods + 1) / (double) 3);
                periods = (int) quarters;
            } else {
                Calendar mycal = new GregorianCalendar(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), 1);
                int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1;
                periodOffset = (startDate.get(Calendar.DAY_OF_MONTH) - 1) / (double) daysInMonth;

                mycal = new GregorianCalendar(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), 1);
                daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

                long endDays = daysInMonth - endDate.get(Calendar.DAY_OF_MONTH);

                periodPartialLength = 1 - (endDays / (double) daysInMonth) - periodOffset;

                periods = endDate.get(Calendar.YEAR) * 12 - startDate.get(Calendar.YEAR) * 12;
                periods += endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH) + 1;
            }

            Set<String> roadmapItemUuids = roadmapOptions.keySet();
            for (String roadmapItemUuid : roadmapItemUuids) {
                Optional<RoadmapItem> otherRoadmapItemOptional = roadmapItems.stream().filter(i -> i.getCanonicalUUID().equals(roadmapItemUuid)).findAny();
                if (otherRoadmapItemOptional.isPresent()) {
                    RoadmapItem otherRoadmapItem = otherRoadmapItemOptional.get();

                    Calendar otherItemStartDate = otherRoadmapItem.getEffectiveDate().getStartDate();
                    Calendar otherItemEndDate = otherRoadmapItem.getEffectiveDate().getEndDate();

                    if (item.getCategoryLink() != null && item.getCategoryLink().equals(otherRoadmapItem.getCategoryLink())) {
                        if (item.getEffectiveDate().getStartDate().after(otherItemStartDate) && item.getEffectiveDate().getStartDate().before(otherItemEndDate)) {
                            verticalPosition++;
                        } else if (item.getEffectiveDate().getEndDate().before(otherItemEndDate) && item.getEffectiveDate().getEndDate().after(otherItemStartDate)) {
                            verticalPosition++;
                        }
                    }
                }
            }

            itemOptions.put("periodOffset", periodOffset);
            itemOptions.put("periodPartialLength", periodPartialLength);
            itemOptions.put("periods", periods);
            itemOptions.put("verticalPosition", verticalPosition);
            roadmapOptions.put(item.getCanonicalUUID(), itemOptions);
        });

        return roadmapOptions;
    }

    private int getDaysInQuarter(Calendar date) {
        Calendar quarterDate = Calendar.getInstance();
        quarterDate.setTime(date.getTime());

        int quarterStartMonth = date.get(Calendar.MONTH) / 3;
        quarterDate.set(Calendar.MONTH, quarterStartMonth);

        // Month 1 total days
        Calendar mycal = new GregorianCalendar(quarterDate.get(Calendar.YEAR), quarterDate.get(Calendar.MONTH), 1);
        int daysInQuarter = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Month 2 total days
        quarterDate.set(Calendar.MONTH, quarterStartMonth + 1);
        mycal = new GregorianCalendar(quarterDate.get(Calendar.YEAR), quarterDate.get(Calendar.MONTH), 1);
        daysInQuarter += mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Month 3 total days
        quarterDate.set(Calendar.MONTH, quarterStartMonth + 2);
        mycal = new GregorianCalendar(quarterDate.get(Calendar.YEAR), quarterDate.get(Calendar.MONTH), 1);
        daysInQuarter += mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

        return daysInQuarter;
    }
}

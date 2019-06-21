package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



@HippoEssentialsGenerated(internalName = "website:effectivedate")
@Node(jcrType = "website:effectivedate")
public class EffectiveDate extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:status")
    public String getStatus() {
        return getProperty("website:status");
    }

    @HippoEssentialsGenerated(internalName = "website:startdate")
    public Calendar getStartDate() {
        return getProperty("website:startdate");
    }

    @HippoEssentialsGenerated(internalName = "website:enddate")
    public Calendar getEndDate() {
        return getProperty("website:enddate");
    }

    @HippoEssentialsGenerated(internalName = "website:datescale")
    public String getDateScale() {
        return getProperty("website:datescale");
    }

    public List<String> getMethodNames(String date1, String date2) {

        List months = new ArrayList();
        DateFormat formater = new SimpleDateFormat("MMM-yyyy");
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();

        try {
            beginCalendar.setTime(formater.parse(date1));
            finishCalendar.setTime(formater.parse(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat formaterYd = new SimpleDateFormat("MMMM");
        while (beginCalendar.before(finishCalendar) || beginCalendar.equals(finishCalendar)) {
            String date = formaterYd.format(beginCalendar.getTime());
            months.add(date);
            beginCalendar.add(Calendar.MONTH, 1);
        }
        return months;
    }

}

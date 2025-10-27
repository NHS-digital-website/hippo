package uk.nhs.digital.freemarker.highcharts;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
public class ChartData {

    private String data;
    private String filterData;
    private String suppressedSeries;

    public String getData() {
        return this.data;
    }

    public String getFilterData() {
        filterData = data;
        try {
            if (StringUtils.isNotBlank(suppressedSeries)) {
                filterData = removeSeries();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return filterData;
    }

    public String removeSeries() throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.getDecoder().decode(getData());
        String decodedString = new String(decodedBytes);
        String[] split = decodedString.split("\n");

        Map<Integer, List> map = new HashMap<>();
        String suppressedString = getSuppressedSeries();
        AtomicInteger i = new AtomicInteger();
        Arrays.stream(split).forEach(line -> {
                Pattern.compile(",").splitAsStream(line).forEachOrdered(s -> map.computeIfAbsent(i.get(), ArrayList::new).add(s));
                i.getAndIncrement();
            }
        );

        Arrays.stream(suppressedString.split(",")).forEach(s -> {
            if (map.get(0).indexOf(s) > -1) {
                int index = map.get(0).indexOf(s);
                map.keySet().stream().forEach(ss -> {
                    map.get(ss).remove(index);
                });
            }
        });
        String str = map.entrySet().stream()
            .map(entry -> String.join(",", entry.getValue()))
            .collect(Collectors.joining("\n")).toString();
        String base64encodedString = Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
        return base64encodedString;
    }

    public String getSuppressedSeries() {
        return suppressedSeries;
    }

    public void setSuppressedSeries(String suppressedSeries) {
        this.suppressedSeries = suppressedSeries;
    }
}

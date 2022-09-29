package uk.nhs.digital.apispecs.util;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;

public class CustomToStringStyle extends MultilineRecursiveToStringStyle {

    private CustomToStringStyle() {
        setUseShortClassName(true);
        setUseIdentityHashCode(false);
    }

    public static final CustomToStringStyle INSTANCE = new CustomToStringStyle();

}

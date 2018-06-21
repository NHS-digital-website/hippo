/*
 * Copyright 2011-2018 Bloomreach (http://www.bloomreach.com)
 */

package com.onehippo.cms7.eforms.hst.model;

import com.onehippo.cms7.eforms.hst.beans.DateFieldBean;
import com.onehippo.cms7.eforms.hst.daterules.DateRule;
import com.onehippo.cms7.eforms.hst.util.DateRuleUtil;
import com.onehippo.cms7.eforms.hst.util.EformsConstants;
import com.onehippo.cms7.eforms.hst.validation.rules.DateFormatRule;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * This class represents a fork of the existing one since the initial value needs to
 * be fully available to the frontend. The current project implementation is using
 * the timezone selection drop down. While storing dates (like the default initial
 * value form date field in form document), the date picked doesn't contain the hours:
 * by default is set to midnight, but since dates are stored in JCR according to GMT,
 * then the actual date is referring to the day before during summer time. For this
 * reason the date received by the front end refers to day before.
 *
 * This fork is only adding a new method, getInitialValue, that just return that
 * value as it is stored
 *
 */
public class DateField extends AbstractField {

    private static Logger log = LoggerFactory.getLogger(DateField.class);

    private String dateFormat;
    private Calendar initialValue;

    /**
     * {@inheritDoc}
     */
    protected DateField(final Builder builder) {
        super(builder);
        this.dateFormat = builder.dateFormat;
        this.initialValue = builder.initialValue;
    }

    public abstract static class Builder<T extends Builder<T>> extends AbstractField.Builder<T> {

        private String dateFormat;
        private Calendar initialValue;

        public Builder(final Form form, final String name) {
            super(form, name);
        }

        public T dateFormat(final String dateFormat) {
            this.dateFormat = dateFormat;
            return self();
        }

        public T initialValue(final Calendar initialValue) {
            this.initialValue = initialValue;
            return self();
        }

        public DateField build() {
            return new DateField(this);
        }
    }

    private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
        public ConcreteBuilder(final Form form, final String name) {
            super(form, name);
        }

        @Override
        protected ConcreteBuilder self() {
            return this;
        }
    }

    public static Builder<?> builder(final Form form, final String name) {
        return new ConcreteBuilder(form, name);
    }

    public DateField(final DateFieldBean bean, final Form form) {
        super(bean, form);

        dateFormat = StringUtils.defaultIfEmpty(bean.getDateformat(), EformsConstants.DEFAULT_DATE_FORMAT);

        if (bean.isInitialValueSet()) {
            initialValue = bean.getInitialValue();
        } else if (bean.isInitialValueDayOffsetMode()) {
            initialValue = Calendar.getInstance();
            initialValue.add(Calendar.DATE, ((Long) bean.getInitialValueDayOffset()).intValue());
        } else if (bean.isInitialValueRuleMode()) {
            final DateRule dateRule = DateRuleUtil.getDateRule(bean.getInitialValueRule());
            if (dateRule != null) {
                initialValue = dateRule.getDate();
            } else {
                log.warn("Failed to set initial value of date field '{}' using rule '{}'", bean.getPath(),
                    bean.getInitialValueRule());
            }
        }
    }

    @Deprecated
    public DateField(final Form form, final String name, final String label, final String dateFormat) {
        super(form);
        setName(name);
        setLabel(label);
        this.dateFormat = dateFormat;
    }

    @Override
    public void createValidationRules() {
        super.createValidationRules();

        if (StringUtils.isNotBlank(dateFormat)) {
            final DateFormatRule rule = new DateFormatRule();
            rule.setRuleData(dateFormat);
            getRules().add(rule);
        }
    }

    public String getDateFormat() {
        return dateFormat;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DateField");
        sb.append(" {dateFormat=").append(getDateFormat());
        sb.append('}');

        return sb.toString();
    }

    @Override
    public String getValue() {
        String value = super.getValue();
        if (StringUtils.isBlank(value) && initialValue != null && dateFormat != null) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            value = format.format(initialValue.getTime());
        }
        return value;
    }

    public Date getInitialValue() {
        //returning the real value store in JCR, according to the GMT
        return initialValue.getTime();
    }

}

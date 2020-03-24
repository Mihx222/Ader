package com.ader.backend.helpers;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BeanHelper {

    /**
     * Check for null properties of a bean using BeanWrapper and add their names to an array
     * It also checks for empty collections if the ignoreEmptyCollections flag is set
     *
     * @param source                 bean name
     * @param ignoreEmptyCollections check for empty collections
     * @return String[]
     */
    public static String[] getNullPropertyNames(Object source, boolean ignoreEmptyCollections) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            } else if (ignoreEmptyCollections && srcValue instanceof Collection) {
                if (((Collection) srcValue).isEmpty()) {
                    emptyNames.add(pd.getName());
                }
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}

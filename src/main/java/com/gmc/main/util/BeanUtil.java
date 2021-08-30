package com.gmc.main.util;

import java.beans.PropertyDescriptor;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanUtil {

	public static String[] getNullPropertyNames(Object source) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(source);
		PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
		Set<String> nullProperties = new LinkedHashSet<>();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Object propertyValue = beanWrapper.getPropertyValue(propertyDescriptor.getName());
			if (propertyValue == null) {
				nullProperties.add(propertyDescriptor.getName());
			}
		}
		String[] result = new String[nullProperties.size()];
		return nullProperties.toArray(result);
	}
}

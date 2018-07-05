package org.loy.mq.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class AppProperties extends PropertyPlaceholderConfigurer{
	private static Map<String, String> ctxPropertiesMap;
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,Properties props) throws BeansException {
		ctxPropertiesMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			ctxPropertiesMap.put(keyStr, value);
		}
		super.processProperties(beanFactoryToProcess, props);
	}
	
	public static String getProperties(String name) {
		return ctxPropertiesMap.get(name);
	}
}

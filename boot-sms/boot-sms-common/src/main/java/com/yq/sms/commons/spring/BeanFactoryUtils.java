package com.yq.sms.commons.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * spring bean util 需要注册才能使用
 *
 */
@Configuration
public class BeanFactoryUtils implements ApplicationContextAware {

	private static ApplicationContext context;

	private static ConfigurableApplicationContext configurableContext;

	private static DefaultListableBeanFactory beanDefinitionRegistry;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
		configurableContext = (ConfigurableApplicationContext) context;
		beanDefinitionRegistry = (DefaultListableBeanFactory) configurableContext.getBeanFactory();
	}

	/**
	 * 注册bean
	 * @param beanId 所注册bean的id
	 * @param className bean的className，
	 *                     三种获取方式：1、直接书写，如：com.mvc.entity.User
	 *                                   2、User.class.getName
	 *                                   3.user.getClass().getName()
	 */
	public static void registerBean(String beanId,String className) {
		// get the BeanDefinitionBuilder
		BeanDefinitionBuilder beanDefinitionBuilder =
				BeanDefinitionBuilder.genericBeanDefinition(className);
		// get the BeanDefinition
		BeanDefinition beanDefinition=beanDefinitionBuilder.getBeanDefinition();
		// register the bean
		beanDefinitionRegistry.registerBeanDefinition(beanId,beanDefinition);
	}

	public static void registerBean(String beanId,Object obj){
		beanDefinitionRegistry.registerSingleton(beanId,obj);
	}

	/**
	 * 移除bean
	 * @param beanId bean的id
	 */
	public static void unregisterBean(String beanId){
		beanDefinitionRegistry.removeBeanDefinition(beanId);
	}

	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	public static <T> T getBean(Class<T> clazz) {
		Map<?, T> beanMap = context.getBeansOfType(clazz);
		if (beanMap == null || beanMap.size() == 0) {
			return null;
		}
		return beanMap.values().iterator().next();
	}

}

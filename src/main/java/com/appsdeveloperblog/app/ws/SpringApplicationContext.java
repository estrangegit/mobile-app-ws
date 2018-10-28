package com.appsdeveloperblog.app.ws;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {

  private static ApplicationContext CONTEXT;

  @Override
  public void setApplicationContext(final ApplicationContext context) throws BeansException {
    CONTEXT = context;
  }

  public static Object getBean(final String beanName) {
    return CONTEXT.getBean(beanName);
  }
}

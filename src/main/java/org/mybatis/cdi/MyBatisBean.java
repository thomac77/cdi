/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mybatis.cdi;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionManager;

/**
 * Internal CDI metadata for a mapper bean.
 *
 * @author Frank D. Martinez [mnesarco]
 */
public class MyBatisBean implements Bean, Serializable {

  protected final Class type;

  protected final Set<Annotation> qualifiers;

  protected final BeanManager beanManager;

  protected final String sqlSessionFactoryName;
  
  public MyBatisBean(Class type, Set<Annotation> qualifiers, String sqlSessionFactoryName, BeanManager beanManager) {  
    this.type = type;
    this.sqlSessionFactoryName = sqlSessionFactoryName;
    this.beanManager = beanManager;    
    if (qualifiers == null || qualifiers.isEmpty()) {
      this.qualifiers = new HashSet<Annotation>();
      this.qualifiers.add(new CDIUtils.SerializableDefaultAnnotationLiteral());
      this.qualifiers.add(new CDIUtils.SerializableAnyAnnotationLiteral());
    }
    else {
      this.qualifiers = qualifiers;
    }    
  }

  public Set getTypes() {
    Set<Type> types = new HashSet<Type>();
    types.add(type);
    return types;
  }

  public Set getQualifiers() {
    return qualifiers;
  }

  public Class getScope() {
    return Dependent.class;
  }

  public String getName() {
    return null;
  }

  public Set getStereotypes() {
    return Collections.emptySet();
  }

  public Class getBeanClass() {
    return type;
  }

  public boolean isAlternative() {
    return false;
  }

  public boolean isNullable() {
    return false;
  }

  public Set getInjectionPoints() {
    return Collections.emptySet();
  }

  public Object create(CreationalContext creationalContext) {
    if (SqlSession.class.equals(type)) {
      return findSqlSessionManager(creationalContext);
    }
    else {
      return Proxy.newProxyInstance(
        SqlSessionFactory.class.getClassLoader(), 
        new Class[] {type}, 
        new SerializableMapperProxy(this, creationalContext));
    }
  }

  public void destroy(Object instance, CreationalContext creationalContext) {
    creationalContext.release();
  }

  private SqlSessionManager findSqlSessionManager(CreationalContext creationalContext) {
    SqlSessionFactory factory = CDIUtils.findSqlSessionFactory(sqlSessionFactoryName, qualifiers, beanManager, creationalContext);
    return CDIUtils.getRegistry(beanManager, creationalContext).getManager(factory);
  }

}

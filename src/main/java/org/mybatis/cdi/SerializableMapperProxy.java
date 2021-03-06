/*
 * Copyright 2014 the original author or authors.
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import javax.enterprise.context.spi.CreationalContext;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author Frank D. Martinez [mnesarco]
 */
public class SerializableMapperProxy implements InvocationHandler, Serializable {

  private static final long serialVersionUID = 1L;

  private transient Object mapper;

  private final MyBatisBean bean;

  private final CreationalContext creationalContext;

  public SerializableMapperProxy(MyBatisBean bean, CreationalContext creationalContext) {
    this.bean = bean;
    this.creationalContext = creationalContext;
    this.mapper = getMapper();
  }

  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    return method.invoke(mapper, args);
  }

  private Object getMapper() {
    SqlSessionFactory factory = CDIUtils.findSqlSessionFactory(bean.sqlSessionFactoryName, bean.qualifiers, bean.beanManager, creationalContext);
    return CDIUtils.getRegistry(bean.beanManager, creationalContext).getManager(factory).getMapper(bean.type);
  }

  private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
    is.defaultReadObject();
    mapper = getMapper();
  }

  private void writeObject(ObjectOutputStream os) throws IOException {
    os.defaultWriteObject();
  }

}

/*
 *    Copyright 2013 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.cdi;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class WeldJUnit4Runner extends BlockJUnit4ClassRunner {

  private final Class<?> klass;
  private final Weld weld;
  private final WeldContainer container;

  public WeldJUnit4Runner(final Class<Object> klass) throws InitializationError {
    super(klass);
    this.klass = klass;
    this.weld = new Weld();
    this.container = weld.initialize();
  }

  @Override
  protected Object createTest() throws Exception {
    final Object test = container.instance().select(klass).get();
    return test;
  }

}
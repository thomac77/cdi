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

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4Runner.class)
public class TestingIoC {

  @Inject
  private FooService fooService;

  @Test
  public void shouldGetAUser() {
    Assert.assertEquals("User1", fooService.doSomeBusinessStuff(1).getName());
    Assert.assertEquals("User2", fooService.doSomeBusinessStuff2(2).getName());
    Assert.assertEquals("User3", fooService.doSomeBusinessStuff3(3).getName());
  }


}
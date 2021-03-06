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

import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

/**
 * Interceptor for JTA transactions. MyBatis should be configured to use the
 * {@code MANAGED} transaction manager.
 *
 * @author Eduardo Macarrón
 */
@Transactional
@Interceptor
public class JtaTransactionInterceptor extends LocalTransactionInterceptor {

  @Inject
  private transient UserTransaction userTransaction;

  @Override
  protected boolean isTransactionActive() throws Exception {
    return userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION;
  }
  
  @Override
  protected void beginJta() throws Exception {
    userTransaction.begin();
  }
  
  @Override
  protected void endJta(boolean isExternaTransaction, boolean needsRollback) throws Exception {
    if (isExternaTransaction) {
      if (needsRollback) {
        userTransaction.setRollbackOnly();
      }
    } else {
      if (needsRollback) {
        userTransaction.rollback();
      } else {
        userTransaction.commit();
      }
    }
  }

}

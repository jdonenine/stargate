/*
 * Copyright The Stargate Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.stargate.web.impl;

import io.stargate.auth.AuthenticationService;
import io.stargate.auth.AuthorizationService;
import io.stargate.core.metrics.api.Metrics;
import io.stargate.db.datastore.DataStoreFactory;

public class WebImpl {

  private AuthenticationService authenticationService;
  private AuthorizationService authorizationService;
  private Metrics metrics;
  private DataStoreFactory dataStoreFactory;

  public AuthenticationService getAuthenticationService() {
    return authenticationService;
  }

  public void setAuthenticationService(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  public void setAuthorizationService(AuthorizationService authorizationService) {
    this.authorizationService = authorizationService;
  }

  public Metrics getMetrics() {
    return metrics;
  }

  public void setMetrics(Metrics metrics) {
    this.metrics = metrics;
  }

  public AuthorizationService getAuthorizationService() {
    return authorizationService;
  }

  public DataStoreFactory getDataStoreFactory() {
    return dataStoreFactory;
  }

  public void setDataStoreFactory(DataStoreFactory dataStoreFactory) {
    this.dataStoreFactory = dataStoreFactory;
  }

  public void start() throws Exception {
    Server server =
        new Server(
            this.authenticationService, this.authorizationService, this.metrics, dataStoreFactory);
    server.run("server", "config.yaml");
  }
}

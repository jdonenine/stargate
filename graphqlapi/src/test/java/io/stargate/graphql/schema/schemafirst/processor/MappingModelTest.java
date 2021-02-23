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
package io.stargate.graphql.schema.schemafirst.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.stargate.db.schema.ImmutableKeyspace;
import org.junit.jupiter.api.Test;

class MappingModelTest {

  @Test
  public void shouldBuildAMappingModelWithOnePrimaryKey() {
    TypeDefinitionRegistry typeDefinitionRegistry =
        new SchemaParser()
            .parse(
                "type User { id: ID! name: String username: String } "
                    + "type Query { getUser(id: ID!): User }");

    // when
    MappingModel mappingModel =
        MappingModel.build(
            typeDefinitionRegistry,
            new ProcessingContext(
                typeDefinitionRegistry, ImmutableKeyspace.builder().name("ks_1").build(), true));

    // then
    QueryMappingModel operationMappingModel =
        (QueryMappingModel) mappingModel.getOperations().get(0);
    assertThat(operationMappingModel.getCoordinates().getFieldName()).isEqualTo("getUser");
    assertThat(operationMappingModel.getCoordinates().getTypeName()).isEqualTo("Query");
    assertThat(operationMappingModel.getInputNames().get(0)).isEqualTo("id");

    EntityMappingModel entityMappingModel = mappingModel.getEntities().get("User");
    FieldMappingModel primaryKey = entityMappingModel.getPrimaryKey().get(0);
    assertThat(primaryKey.getCqlName()).isEqualTo("id");
    assertThat(entityMappingModel.getPrimaryKey().get(0).isPartitionKey()).isTrue();
  }
}

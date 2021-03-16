/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.spi.connector;

/**
 * 用于创建ConnectorPageSink
 * ConnectorPageSink可以向第三方数据存储引擎写入数据
 */
public interface ConnectorPageSinkProvider
{
    /**
     * create table xxx as select * from table_a limit 11;
     * @param transactionHandle
     * @param session
     * @param outputTableHandle
     * @return
     */
    ConnectorPageSink createPageSink(ConnectorTransactionHandle transactionHandle, ConnectorSession session, ConnectorOutputTableHandle outputTableHandle);

    /**
     * insert table xxx as select * from table_a limit 11;
     * @param transactionHandle
     * @param session
     * @param insertTableHandle
     * @return
     */
    ConnectorPageSink createPageSink(ConnectorTransactionHandle transactionHandle, ConnectorSession session, ConnectorInsertTableHandle insertTableHandle);
}

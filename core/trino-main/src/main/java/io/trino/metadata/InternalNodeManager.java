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
package io.trino.metadata;

import io.trino.connector.CatalogName;

import java.util.Set;
import java.util.function.Consumer;

/**
 * node节点管理器
 * 用于为执行stage分配节点时使用
 */
public interface InternalNodeManager
{
    /**
     * 获取指定状态的节点列表
     * @param state
     * @return
     */
    Set<InternalNode> getNodes(NodeState state);

    /**
     * 根据catalog获取节点列表
     * @param catalogName
     * @return
     */
    Set<InternalNode> getActiveConnectorNodes(CatalogName catalogName);

    InternalNode getCurrentNode();

    Set<InternalNode> getCoordinators();

    AllNodes getAllNodes();

    /**
     * 刷新节点的信息
     */
    void refreshNodes();

    void addNodeChangeListener(Consumer<AllNodes> listener);

    void removeNodeChangeListener(Consumer<AllNodes> listener);
}

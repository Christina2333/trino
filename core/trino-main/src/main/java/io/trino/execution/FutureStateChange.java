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
package io.trino.execution;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static java.util.Objects.requireNonNull;

/**
 * xx变更时异步执行的任务
 * 用于执行完监听器操作之类，把对应的监听器移除
 * @param <T>
 */
@ThreadSafe
public class FutureStateChange<T>
{
    // Use a separate future for each listener so canceled listeners can be removed
    @GuardedBy("listeners")
    private final Set<SettableFuture<T>> listeners = new HashSet<>();

    public ListenableFuture<T> createNewListener()
    {
        SettableFuture<T> listener = SettableFuture.create();
        synchronized (listeners) {
            listeners.add(listener);
        }

        // remove the listener when the future completes
        listener.addListener(
                () -> {
                    synchronized (listeners) {
                        listeners.remove(listener);
                    }
                },
                directExecutor());

        return listener;
    }

    public void complete(T newState)
    {
        // 启动状态变更的任务
        fireStateChange(newState, directExecutor());
    }

    public void complete(T newState, Executor executor)
    {
        fireStateChange(newState, executor);
    }

    private void fireStateChange(T newState, Executor executor)
    {
        requireNonNull(executor, "executor is null");
        Set<SettableFuture<T>> futures;
        // 移除全部监听器
        synchronized (listeners) {
            futures = ImmutableSet.copyOf(listeners);
            listeners.clear();
        }

        // 设置异步任务的新状态
        for (SettableFuture<T> future : futures) {
            executor.execute(() -> future.set(newState));
        }
    }
}

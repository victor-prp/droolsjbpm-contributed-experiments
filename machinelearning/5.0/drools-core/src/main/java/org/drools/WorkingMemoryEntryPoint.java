/*
 * Copyright 2007 JBoss Inc
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
 *
 * Created on Dec 14, 2007
 */
package org.drools;

import java.io.Serializable;

/**
 * An interface for instances that allow handling of entry-point-scoped
 * facts
 *  
 * @author etirelli
 */
public interface WorkingMemoryEntryPoint {
    /**
     * Assert a fact.
     * 
     * @param object
     *            The fact object.
     * 
     * @return The new fact-handle associated with the object.
     * 
     * @throws FactException
     *             If a RuntimeException error occurs.
     */
    FactHandle insert(Object object) throws FactException;
    
    /**
     * Insert a fact registering JavaBean <code>PropertyChangeListeners</code>
     * on the Object to automatically trigger <code>update</code> calls
     * if <code>dynamic</code> is <code>true</code>.
     * 
     * @param object
     *            The fact object.
     * @param dynamic
     *            true if Drools should add JavaBean
     *            <code>PropertyChangeListeners</code> to the object.
     * 
     * @return The new fact-handle associated with the object.
     * 
     * @throws FactException
     *             If a RuntimeException error occurs.
     */
    FactHandle insert(Object object,
                            boolean dynamic) throws FactException;
    
    /**
     * Retract a fact.
     * 
     * @param handle
     *            The fact-handle associated with the fact to retract.
     * 
     * @throws FactException
     *             If a RuntimeException error occurs.
     */
    void retract(FactHandle handle) throws FactException;

    /**
     * Inform the WorkingMemory that a Fact has been modified and that it
     * should now update the network.
     * 
     * @param handle
     *            The fact-handle associated with the fact to modify.
     * @param object
     *            The new value of the fact.
     * 
     * @throws FactException
     *             If a RuntimeException error occurs.
     */
    void update(FactHandle handle,
                      Object object) throws FactException;
    
    /**
     * 
     * @param factHandle
     */
    public void modifyRetract(final FactHandle factHandle);
    
    /**
     * 
     * @param factHandle
     * @param object
     */
    public void modifyInsert(final FactHandle factHandle,
                             final Object object); 
    
    public WorkingMemoryEntryPoint getWorkingMemoryEntryPoint(String name);    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.database;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ResourceDAO<TId, TResource> extends Closeable
{
    TResource find (TId id) throws IOException;
    List<TResource> getAll() throws IOException;
    void save(TResource resource) throws IOException;
    boolean delete(TResource resource) throws IOException;
    void update(TResource resource) throws IOException;
    int count() throws IOException;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.database;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface ResourceDAO<T> {
    T find (UUID id) throws SQLException;
    List<T> getAll() throws SQLException;
}

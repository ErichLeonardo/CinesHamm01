package org.Hamm.Model.DAO;

import java.sql.SQLException;
import java.util.List;
public interface DAO<T> extends AutoCloseable {
    /**
     * Retrieves all entities of type T from the database.
     *
     * @return a list of all entities
     * @throws SQLException if any SQL error occurs
     */
    List<T> findAll() throws SQLException;
    /**
     * Retrieves an entity of type T from the database based on its ID.
     *
     * @param id the ID of the entity to retrieve
     * @return the retrieved entity
     * @throws SQLException if any SQL error occurs
     */
    T findById(int id) throws SQLException;
    /**
     * Inserts an entity of type T into the database.
     *
     * @param entity the entity to insert
     * @return the inserted entity
     * @throws SQLException if any SQL error occurs
     */
    T insert(T entity) throws SQLException;
    /**
     * Deletes an entity of type T from the database.
     *
     * @param entity the entity to delete
     * @throws SQLException if any SQL error occurs
     */
    void delete(T entity) throws SQLException;
    /**
     * Updates an entity of type T in the database.
     *
     * @param entity the entity to update
     * @return the updated entity
     * @throws SQLException if any SQL error occurs
     */
    T update(T entity) throws SQLException;
}

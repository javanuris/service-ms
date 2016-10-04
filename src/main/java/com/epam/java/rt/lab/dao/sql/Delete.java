package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;

/**
 * category-ms
 */
public class Delete extends Sql {

    private static final String DELETE = "DELETE FROM ";
    private Class entityClass;
    private String table;
    private Where where;

    Delete(Class entityClass) throws DaoException {
        if (entityClass == null)
            throw new DaoException("exception.dao.sql.delete.empty-class");
        this.entityClass = entityClass;
        this.table = getProperty(entityClass.getName());
    }

    public Delete where(Where.Predicate predicate) throws DaoException {
        if (predicate == null)
            throw new DaoException("exception.dao.sql.delete.empty-predicate");
        this.where = new Where(null, predicate);
        this.where.linkWildValue(getWildValueList());
        return this;
    }

    @Override
    public String create() throws DaoException {
        try {
            return this.where.appendClause(
                    new StringBuilder().append(DELETE).append(table)
            ).toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.sql.delete.combine", e.getCause());
        }
    }

}

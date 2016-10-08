package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;

/**
 * {@code Delete} class defines sql statement,
 * which calls DELETE sql expression
 *
 * @see com.epam.java.rt.lab.dao.sql.Where.Predicate
 */
public class Delete extends Sql {

    private static final String DELETE = "DELETE FROM ";

    /** Entity {@code Class} */
    private Class entityClass;
    /** {@code String} representation of table name */
    private String table;
    /** {@code Where} object, which defines where clause */
    private Where where;

    /**
     * Initiates new @{code Delete} object with defined
     * {@code Class} of entity
     *
     * @param entityClass       entity {@code Class}
     * @throws DaoException
     */
    Delete(Class entityClass) throws DaoException {
        if (entityClass == null)
            throw new DaoException("exception.dao.sql.delete.empty-class");
        this.entityClass = entityClass;
        this.table = getProperty(entityClass.getName());
    }

    /**
     * Returns {@code Delete} object, on which this method called
     * with setting its where clause
     *
     * @param predicate         {@code Predicate} object, which defines
     *                          where predicate for sql statement
     * @return                  {@code Count} object, on which this method called
     * @throws DaoException
     *
     * @see com.epam.java.rt.lab.dao.sql.Where.Predicate
     */
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

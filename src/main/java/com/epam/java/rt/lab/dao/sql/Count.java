package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;

import java.util.List;

/**
 * {@code Count} class defines sql statement,
 * which calls COUNT sql-function
 *
 * @see com.epam.java.rt.lab.dao.sql.Where.Predicate
 */
public class Count extends Sql {

    private static final String SELECT_COUNT = "SELECT COUNT(*) as count ";

    /** {@code From} object, which defines from clause */
    private Select.From from;
    /** {@code Join} object, which defines join clause */
    private Select.Join join;
    /** {@code Where} object, which defines where clause */
    private Where where;

    /**
     * Initiates new {@code Count} object with defined
     * {@code List} of column names, which should be queried
     * in sql statement
     *
     * @param columnList        {@code List} object, which contains columns that
     *                          should be queried in sql statement
     * @throws DaoException
     */
    Count(List<Column> columnList) throws DaoException {
        if (columnList == null || columnList.size() == 0)
            throw new DaoException("exception.dao.sql.Select.empty-column-list");
        this.join = new Select.Join();
        this.from = new Select.From(columnList, join);
    }

    /**
     * Returns {@code Count} object, on which this method called
     * with setting its where clause
     *
     * @param predicate     {@code Predicate} object, which defines
     *                      where predicate for sql statement
     * @return              {@code Count} object, on which this method called
     * @throws DaoException
     *
     * @see com.epam.java.rt.lab.dao.sql.Where.Predicate
     */
    public Count where(Where.Predicate predicate) throws DaoException {
        this.where = new Where(join, predicate);
        this.where.linkWildValue(getWildValueList());
        return this;
    }

    @Override
    public String create() throws DaoException {
        try {
            StringBuilder result = new StringBuilder();
            result = this.from.appendClause(result.append(SELECT_COUNT));
            if (this.join != null) {
                this.join.appendClause(result);
                if (this.where == null)
                    this.where = new Where(this.join, null);
            }
            if (this.where != null) this.where.appendClause(result);
//            System.out.println(result.toString());
            return result.toString();
        } catch (Exception e) {
            throw new DaoException("exception.dao.sql.Select.combine", e.getCause());
        }
    }

}

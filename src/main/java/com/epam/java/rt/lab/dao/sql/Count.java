package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;

import java.util.List;

/**
 * category-ms
 */
public class Count extends Sql {

    private static final String SELECT_COUNT = "SELECT COUNT(*) as count ";

    private Select_.From from;
    private Select_.Join join;
    private Where where;

    Count(List<Column> columnList) throws DaoException {
        if (columnList == null || columnList.size() == 0)
            throw new DaoException("exception.dao.sql.Select.empty-column-list");
        this.join = new Select_.Join();
        this.from = new Select_.From(columnList, join);
    }

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

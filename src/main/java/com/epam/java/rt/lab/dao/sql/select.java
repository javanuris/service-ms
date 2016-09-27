package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.util.StringArray;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class Select extends Sql {

    private static final String SELECT = "SELECT ";

    private List<Column> columnList;
    private From from;
    private Where where;

    Select(List<Column> columnList) throws DaoException {
        if (columnList == null || columnList.size() == 0)
            throw new DaoException("exception.dao.sql.select.empty-column-list");
        this.columnList = columnList;
        this.from = new From(columnList);
    }

    public Select where(Where.Predicate predicate) throws DaoException {
        this.where = new Where(from.getJoinPredicate(), predicate);
        return this;
    }

    public Select orderBy() {
        return this;
    }

    public Select limit() {
        return this;
    }

    @Override
    public String toString() {
        return SELECT.concat(StringArray.combine(this.columnList, SIGN_COMMA)).concat(from.toString());
    }

    /**
     *
     */
    private static class From {

        private static final String FROM = " FROM ";
        private static final String JOIN = " JOIN ";

        private String from;
        private List<String> joinList;
        private Where.Predicate joinPredicate;

        private From(List<Column> columnList) {
            this.from = columnList.get(0).getTableName();
            this.joinList = new ArrayList<>();
            for (Column column : columnList) {
                if (!column.getTableName().equals(this.from) &&
                        !this.joinList.contains(column.getTableName()))
                    this.joinList.add(column.getTableName());
            }
        }

        private void fillJoinPredicateList() throws DaoException {
            if (this.joinList.size() == 0) return;
            List<Where.Predicate> joinPredicateList = new ArrayList<>();
            for (String join : this.joinList) {
                String predicate = getProperty(this.from.concat(join));
                if (predicate == null) predicate = getProperty(join.concat(this.from));
                if (predicate == null)
                    throw new DaoException("exception.dao.sql.select.join.predicate");
                String[] split = StringArray.splitSpaceLessNames(predicate, SIGN_EQUAL);
                joinPredicateList.add(new Where.Predicate(
                        Column.of(split[0]),
                        Where.Predicate.PredicateOperator.EQUAL,
                        Column.of(split[1])
                ));
            }
            this.joinPredicate = Where.Predicate.get(joinPredicateList);
        }

        public Where.Predicate getJoinPredicate() {
            return this.joinPredicate;
        }

        @Override
        public String toString() {
            return FROM.concat(this.from).concat(this.joinList.size() == 0 ? "" :
                    JOIN.concat(StringArray.combine(this.joinList, SIGN_COMMA)));
        }

    }
}

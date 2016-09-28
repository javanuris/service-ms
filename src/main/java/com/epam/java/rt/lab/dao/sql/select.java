package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.util.StringArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * service-ms
 */
public class Select extends Sql implements Iterable<Column> {

    private static final String SELECT = "SELECT ";

    private List<Column> columnList;
    private From from;
    private Join join;
    private Where where;
    private OrderBy orderBy;
    private Limit limit;

    Select(List<Column> columnList) throws DaoException {
        if (columnList == null || columnList.size() == 0)
            throw new DaoException("exception.dao.sql.select.empty-column-list");
        this.columnList = columnList;
        this.join = new Join();
        this.from = new From(columnList, join);
    }

    public Select where(Where.Predicate predicate) throws DaoException {
        this.where = new Where(join, predicate);
        this.where.linkWildValue(getWildValueList());
        return this;
    }

    public Select orderBy(OrderBy.Criteria[] criteriaArray) {
        this.orderBy = new OrderBy(criteriaArray);
        return this;
    }

    public Select limit(Integer offset, Integer count) {
        this.limit = new Limit(offset, count);
        return this;
    }

    @Override
    public String create() throws DaoException {
        try {
            StringBuilder result = new StringBuilder();
            result = this.from.appendClause(StringArray.combine(result.append(SELECT), this.columnList, COMMA_DELIMITER));
            if (this.join != null) {
                this.join.appendClause(result);
                if (this.where == null)
                    this.where = new Where(this.join, null);
            }
            if (this.where != null) this.where.appendClause(result);
            if (this.orderBy != null) this.orderBy.appendClause(result);
            if (this.limit != null) this.limit.appendClause(result);
//            System.out.println(result.toString());
            return result.toString();
        } catch (Exception e) {
            throw new DaoException("exception.dao.sql.select.combine", e.getCause());
        }
    }

    @Override
    public Iterator<Column> iterator() {
        return new Iterator<Column>() {
            private int index = 0;

            public boolean hasNext(){
                return index < columnList.size();
            }

            public Column next(){
                return columnList.get(index++);
            }

            public void remove(){
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * The usage is only in select statement
     */
    static class From implements Clause {

        private static final String FROM = " FROM ";

        private String from;

        From(List<Column> columnList, Join join) {
            this.from = columnList.get(0).getTableName();
            join.setFrom(this.from);
            for (Column column : columnList) join.addJoin(column.getTableName());
        }

        @Override
        public StringBuilder appendClause(StringBuilder result) {
            return result.append(FROM).append(this.from );
        }

    }

    /**
     *
     */
    static class Join implements Clause {

        private static final String JOIN = " JOIN ";

        private String from;
        private List<String> joinList;

        Join() {
            this.joinList = new ArrayList<>();
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        void addJoin(String join) {
            if (!from.equals(join) && !this.joinList.contains(join))
                this.joinList.add(join);
        }

        Where.Predicate getPredicate() throws DaoException {
            List<Where.Predicate> predicateList = new ArrayList<>();
            getPredicate(predicateList, this.from, 0);
            for (int i = 0; i < this.joinList.size() - 1; i++)
                getPredicate(predicateList, this.joinList.get(i), i + 1);
            return Where.Predicate.get(predicateList);
        }

        private void getPredicate(List<Where.Predicate> predicateList, String tableName, int startIndex) throws DaoException {
            for (int i = startIndex; i < this.joinList.size(); i++) {
                String joinTableName = this.joinList.get(i);
                String joinExpression = getProperty(tableName.concat(JOIN_AMPERSAND).concat(joinTableName));
                if (joinExpression == null) joinExpression = getProperty(joinTableName.concat(JOIN_AMPERSAND).concat(tableName));
                if (joinExpression != null) {
                    String[] split = StringArray.splitSpaceLessNames(joinExpression, JOIN_AMPERSAND);
                    predicateList.add(new Where.Predicate(
                            Column.of(split[0]),
                            Where.Predicate.PredicateOperator.EQUAL,
                            Column.of(split[1])
                    ));
                }
            }
        }

        @Override
        public StringBuilder appendClause(StringBuilder result) throws DaoException {
            try {
                return joinList.size() == 0 ? result :
                        result.append(JOIN).append(StringArray.combine(this.joinList, COMMA_DELIMITER));
            } catch (Exception e) {
                throw new DaoException("exception.dao.sql.join.combine", e.getCause());
            }
        }

    }

    /**
     * The usage is only in select statement
     */
    static class Limit implements Clause {

        private static final String LIMIT = " LIMIT ";

        private Integer offset;
        private Integer count;

        Limit(Integer offset, Integer count) {
            this.offset = offset;
            this.count = count;
        }

        @Override
        public StringBuilder appendClause(StringBuilder result) throws DaoException {
            return offset == null || count == null || offset < 0 || count <= 0 ? result :
                    result.append(LIMIT)
                            .append(String.valueOf(this.offset))
                            .append(Sql.COMMA_DELIMITER)
                            .append(String.valueOf(this.count));
        }

    }

}

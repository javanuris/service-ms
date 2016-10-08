package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.util.StringArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * {@code Select} class defines sql statement,
 * which calls SELECT sql expression
 *
 * @see Column
 * @see OrderBy
 * @see Limit
 * @see com.epam.java.rt.lab.dao.sql.OrderBy.Criteria
 * @see com.epam.java.rt.lab.dao.sql.Where.Predicate
 */
public class Select extends Sql implements Iterable<Column> {

    private static final String SELECT = "SELECT ";

    /** {@code List} of {@code Column} objects */
    private List<Column> columnList;
    /** {@code From} object */
    private From from;
    /** {@code Join} object */
    private Join join;
    /** {@code Where} object */
    private Where where;
    /** {@code OrderBy} object */
    private OrderBy orderBy;
    /** {@code Limit} object */
    private Limit limit;

    /**
     * Initiates new {@code Select} object with defined
     * {@code List} of {@code Column} objects
     *
     * @param columnList        {@code List} of {@code Column} objects
     * @throws DaoException
     */
    Select(List<Column> columnList) throws DaoException {
        if (columnList == null || columnList.size() == 0)
            throw new DaoException("exception.dao.sql.Select.empty-column-list");
        this.columnList = columnList;
        this.join = new Join();
        this.from = new From(columnList, join);
    }

    /**
     * Returns {@code Select} object, on which this method called
     * with setting its where clause
     *
     * @param predicate     {@code Predicate} object
     * @return              {@code Select} object, on which this method called
     * @throws DaoException
     *
     * @see com.epam.java.rt.lab.dao.sql.Where.Predicate
     */
    public Select where(Where.Predicate predicate) throws DaoException {
        this.where = new Where(join, predicate);
        this.where.linkWildValue(getWildValueList());
        return this;
    }

    /**
     * Returns {@code Select} object, on which this method called
     * with defined its order by clause
     *
     * @param criteriaArray {@code Array} of {@code Criteria} objects
     * @return              {@code Select} object, on which this method called
     *
     * @see com.epam.java.rt.lab.dao.sql.OrderBy.Criteria
     */
    public Select orderBy(OrderBy.Criteria[] criteriaArray) {
        this.orderBy = new OrderBy(criteriaArray);
        return this;
    }

    /**
     * Returns {@code Select} object, on which this method called
     * with defined its limit clause
     *
     * @param offset    {@code Long} limit offset value
     * @param count     {@code Long} limit count value
     * @return          {@code Select} object, on which this method called
     */
    public Select limit(Long offset, Long count) {
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
            throw new DaoException("exception.dao.sql.Select.combine", e.getCause());
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
     * {@code From} class defines from clause of sql statement
     *
     * @see Column
     */
    public static class From implements Clause {

        private static final String FROM = " FROM ";

        /** {@code From} object */
        private String from;

        /**
         * Initiates new {@code From} object with defined
         * {@code From} object
         *
         * @param columnList    {@code List} of {@Column} objects
         * @param join          {@code Join} object to pre initiate join clause
         *                      according to selected columns
         */
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
     * {@cdoe Join} class defines join clause of sql statement
     *
     * @see com.epam.java.rt.lab.dao.sql.Where.Predicate
     */
    public static class Join implements Clause {

        private static final String JOIN = " JOIN ";

        /** {@code String} representation of base table name */
        private String from;
        /** {@code List} of {@code Stirng} objects which defines join table names */
        private List<String> joinList;

        /**
         * Initiates new {@code Join} object
         */
        Join() {
            this.joinList = new ArrayList<>();
        }

        /**
         * Returns {@code From} object
         *
         * @return  {@code From} object
         */
        public String getFrom() {
            return from;
        }

        /**
         * Set {@code From} object
         *
         * @param from  {@code From} object
         */
        public void setFrom(String from) {
            this.from = from;
        }

        /**
         * Adds join table name to join clause
         *
         * @param join  {@code String} representation of join table name
         */
        void addJoin(String join) {
            if (!from.equals(join) && !this.joinList.contains(join))
                this.joinList.add(join);
        }

        /**
         * Returns {@code Predicate} object for where clause
         *
         * @return      {@code Predicate} object
         * @throws DaoException
         *
         * @see com.epam.java.rt.lab.dao.sql.Where.Predicate
         */
        Where.Predicate getPredicate() throws DaoException {
            List<Where.Predicate> predicateList = new ArrayList<>();
            getPredicate(predicateList, this.from, 0);
            for (int i = 0; i < this.joinList.size() - 1; i++)
                getPredicate(predicateList, this.joinList.get(i), i + 1);
            return Where.Predicate.get(predicateList);
        }

        /**
         * Fills {@code List} of {@code Predicate} objects for where clause
         * from table name, which should be selected or joined.
         *
         * @param predicateList     {@code List} of {@code Predicate} objects
         * @param tableName         {@code String} representation of table name
         * @param startIndex        {@code int} starting index in {@code List} of
         *                          {@code Predicate} objects, to which should be
         *                          found referenced columns
         * @throws DaoException
         *
         * @see com.epam.java.rt.lab.dao.sql.Where.Predicate
         */
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
     * {@code Limit} class defines limit clause
     * for sql statement
     */
    static class Limit implements Clause {

        private static final String LIMIT = " LIMIT ";

        /** {@code Long} representation of limit offset value */
        private Long offset;
        /** {@code Long} representation of limit count value */
        private Long count;

        /**
         * Initiates new {@code Limit} object with defined
         * {@code Long} limit offset value and
         * {@code Long} limit count value
         *
         * @param offset        {@code Long} limit offset value
         * @param count         {@code Long} limit count value
         */
        Limit(Long offset, Long count) {
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

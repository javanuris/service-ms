package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.StringCombiner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.*;

/**
 * {@code Select} class defines sql statement,
 * which calls SELECT sql expression
 *
 * @see Column
 * @see OrderBy
 * @see Limit
 * @see com.epam.java.rt.lab.dao.sql.OrderBy.Criteria
 * @see Predicate
 */
public class Select extends Sql implements Iterable<Column> {

    private static final String SELECT = "SELECT ";

    /**
     * {@code List} of {@code Column} objects
     */
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
     * @throws AppException
     */
    Select(List<Column> columnList) throws AppException {
        if (columnList == null || columnList.size() == 0) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
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
     * @throws AppException
     *
     * @see Predicate
     */
    public Select where(Predicate predicate) throws AppException {
//        if (predicate == null) throw new AppException(NULL_NOT_ALLOWED);
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
     * @throws AppException
     *
     * @see com.epam.java.rt.lab.dao.sql.OrderBy.Criteria
     */
    public Select orderBy(OrderBy.Criteria[] criteriaArray)
            throws AppException {
//        if (criteriaArray == null) throw new AppException(NULL_NOT_ALLOWED);
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
    public String create() throws AppException {
        StringBuilder result = new StringBuilder();
        result.append(SELECT);
        result = StringCombiner.combine(result, this.columnList,
                COMMA_WITH_SPACE);
        result = this.from.appendClause(result);
        if (this.join != null) {
            this.join.appendClause(result);
            if (this.where == null)
                this.where = new Where(this.join, null);
        }
        if (this.where != null) this.where.appendClause(result);
        if (this.orderBy != null) this.orderBy.appendClause(result);
        if (this.limit != null) this.limit.appendClause(result);
        return result.toString();
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
     * {@code From} class defines getDate clause of sql statement
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
         * @param columnList    {@code List} of {@code Column} objects
         * @param join          {@code Join} object to pre initiate join clause
         *                      according to selected columns
         * @throws AppException
         */
        From(List<Column> columnList, Join join) throws AppException {
            this.from = columnList.get(0).getTableName();
            join.setFrom(this.from);
            for (Column column : columnList) {
                join.addJoin(column.getTableName());
            }
        }

        @Override
        public StringBuilder appendClause(StringBuilder result) {
            return result.append(FROM).append(this.from);
        }

    }

    /**
     * {@code Join} class defines join clause of sql statement
     *
     * @see Predicate
     */
    public static class Join implements Clause {

        private static final String JOIN = " JOIN ";

        /**
         * {@code String} representation of base table name
         */
        private String from;
        /**
         * {@code List} of {@code Stirng} objects which defines join table names
         */
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
         * @throws AppException
         */
        void addJoin(String join) throws AppException {
            if (join == null) throw new AppException(NULL_NOT_ALLOWED);
            if (!from.equals(join) && !this.joinList.contains(join))
                this.joinList.add(join);
        }

        /**
         * Returns {@code Predicate} object for where clause
         *
         * @return      {@code Predicate} object
         * @throws AppException
         *
         * @see Predicate
         */
        Predicate getPredicate() throws AppException {
            List<Predicate> predicateList = new ArrayList<>();
            getPredicate(predicateList, this.from, 0);
            for (int i = 0; i < this.joinList.size() - 1; i++) {
                getPredicate(predicateList, this.joinList.get(i), i + 1);
            }
            return Predicate.get(predicateList);
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
         * @throws AppException
         *
         * @see Predicate
         */
        private void getPredicate(List<Predicate> predicateList,
                                  String tableName, int startIndex)
                throws AppException {
            if (predicateList == null || tableName == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            for (int i = startIndex; i < this.joinList.size(); i++) {
                String joinTableName = this.joinList.get(i);
                String joinExpression =
                        getProperty(tableName + AMPERSAND + joinTableName);
                if (joinExpression == null) {
                    joinExpression =
                            getProperty(joinTableName + AMPERSAND + tableName);
                }
                if (joinExpression != null) {
                    String[] split = StringCombiner.
                            splitSpaceLessNames(joinExpression, AMPERSAND);
                    Predicate predicate = new Predicate(Column.from(split[0]),
                            Predicate.PredicateOperator.EQUAL,
                            Column.from(split[1]));
                    predicateList.add(predicate);
                }
            }
        }

        @Override
        public StringBuilder appendClause(StringBuilder result)
                throws AppException {
            return ((joinList.size() == 0) ? result
                    : result.append(JOIN).
                    append(StringCombiner.combine(this.joinList, COMMA)));
        }

    }

    /**
     * {@code Limit} class defines limit clause
     * for sql statement
     */
    static class Limit implements Clause {

        private static final String LIMIT = " LIMIT ";

        /**
         * {@code Long} representation of limit offset value
         */
        private Long offset;
        /**
         * {@code Long} representation of limit count value
         */
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
        public StringBuilder appendClause(StringBuilder result)
                throws AppException {
            if (result == null) throw new AppException(NULL_NOT_ALLOWED);
            return ((offset == null || count == null
                    || offset < 0 || count <= 0) ? result
                    : result.append(LIMIT).append(String.valueOf(this.offset)).
                    append(COMMA_WITH_SPACE).append(String.valueOf(this.count)));
        }

    }

}

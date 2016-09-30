package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.EntityProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class Where implements Clause {

    private static final String WHERE = " WHERE ";

    private Select.Join join;
    private Predicate predicate;

    Where(Select.Join join, Predicate predicate) throws DaoException {
        this.join = join;
        Predicate joinPredicate = null;
        if (join == null && predicate == null) return;
        if (predicate != null) addJoinFromPredicate(predicate);
        if (join != null) joinPredicate = join.getPredicate(); // checked join from where clause predicate's columns
        if (joinPredicate != null && predicate != null) {
            this.predicate = Predicate.get(
                    joinPredicate,
                    Predicate.PredicateOperator.AND,
                    predicate
            );
        } else if (predicate != null) {
            this.predicate = predicate;
        } else {
            this.predicate = joinPredicate;
        }
    }

    private void addJoinFromPredicate(Predicate predicate) {
        if (predicate.predicate != null) addJoinFromPredicate(predicate.predicate);
        if (predicate.comparePredicate != null) addJoinFromPredicate(predicate.comparePredicate);
        if (predicate.column != null && this.join != null)
            this.join.addJoin(predicate.column.getTableName());
        if (predicate.compareColumn != null && this.join != null)
            this.join.addJoin(predicate.compareColumn.getTableName());
    }

    void linkWildValue(List<WildValue> wildValueList) {
        if (this.predicate != null) linkWildValue(wildValueList, this.predicate);
    }

    private void linkWildValue(List<WildValue> wildValueList, Predicate predicate) {
        if (predicate.predicate != null) linkWildValue(wildValueList, predicate.predicate);
        if (predicate.comparePredicate != null) linkWildValue(wildValueList, predicate.comparePredicate);
        if (predicate.wildValueArray != null)
            for (WildValue wildValue : predicate.wildValueArray) wildValue.link(wildValueList);
    }

    @Override
    public StringBuilder appendClause(StringBuilder result) throws DaoException {
        return this.predicate == null ? result :
                this.predicate.appendClause(result.append(WHERE));
    }

    public static class Predicate implements Clause {

        private static final String EQUAL = " = ";
        private static final String NOT_EQUAL = " <> ";
        private static final String MORE = " > ";
        private static final String MORE_OR_EQUAL = " >= ";
        private static final String LESS = " < ";
        private static final String LESS_OR_EQUAL = " <= ";
        private static final String IN = " IN ";
        private static final String OR = " OR ";
        private static final String AND = " AND ";
        private static final String BETWEEN = " BETWEEN ";
        private static final String LIKE = " LIKE ";
        private static final String IS_NULL = " IS NULL";
        private static final String IS_NOT_NULL = " IS NOT NULL";
        private static final String PAR_OPEN = "(";
        private static final String PAR_CLOSE = ")";

        public enum PredicateOperator {
            EQUAL,
            NOT_EQUAL,
            MORE,
            MORE_OR_EQUAL,
            LESS,
            LESS_OR_EQUAL,
            IN,
            OR,
            AND,
            BETWEEN,
            LIKE,
            IS_NULL,
            IS_NOT_NULL
        }

        private Column column;
        private Predicate predicate;
        private PredicateOperator operator;
        WildValue[] wildValueArray;
        private Column compareColumn;
        private Predicate comparePredicate;

        Predicate(Column column, PredicateOperator operator, Column compareColumn) {
            this.column = column;
            this.operator = operator;
            this.compareColumn = compareColumn;
        }

        private Predicate(Predicate predicate, PredicateOperator operator, Predicate comparePredicate) {
            this.predicate = predicate;
            this.operator = operator;
            this.comparePredicate = comparePredicate;
        }

        private Predicate(Column column, PredicateOperator operator, WildValue[] wildValueArray) {
            this.column = column;
            this.operator = operator;
            this.wildValueArray = wildValueArray;
        }

        Predicate(Column column, PredicateOperator operator) {
            this.column = column;
            this.operator = operator;
        }

        // static gets

        public static Predicate get(EntityProperty entityProperty, PredicateOperator operator, EntityProperty joinEntityProperty)
                throws DaoException {
            if (operator != PredicateOperator.EQUAL)
                throw new DaoException("exception.dao.sql.where.join-predicate");
            return new Predicate(Sql.getColumn(entityProperty), operator, Sql.getColumn(joinEntityProperty));
        }

        public static Predicate get(Predicate predicate, PredicateOperator operator, Predicate comparePredicate)
                throws DaoException {
            if (operator != PredicateOperator.AND && operator != PredicateOperator.OR)
                throw new DaoException("exception.dao.sql.where.both-predicates");
            return new Predicate(predicate, operator, comparePredicate);
        }

        public static <T> Predicate get(EntityProperty entityProperty, PredicateOperator operator, T... valArray)
                throws DaoException {
            if (operator == PredicateOperator.IS_NULL || operator == PredicateOperator.IS_NOT_NULL ||
                    operator == PredicateOperator.AND || operator == PredicateOperator.OR ||
                    (operator == PredicateOperator.BETWEEN && valArray.length != 2) ||
                    valArray.length == 0)
                throw new DaoException("exception.dao.sql.where.value");
            List<WildValue> wildValueList = new ArrayList<>();
            for (T val : valArray) wildValueList.add(new WildValue(val));
            WildValue[] wildValueArray = {};
            return new Predicate(
                    Sql.getColumn(entityProperty),
                    operator,
                    wildValueList.toArray(wildValueArray)
            );
        }

        public static Predicate isNull(EntityProperty entityProperty) throws DaoException {
            return new Predicate(Sql.getColumn(entityProperty), PredicateOperator.IS_NULL);
        }

        public static Predicate isNotNull(EntityProperty entityProperty) throws DaoException {
            return new Predicate(Sql.getColumn(entityProperty), PredicateOperator.IS_NOT_NULL);
        }

        // join predicate list to predicate

        public static Predicate get(List<Predicate> joinPredicateList)
                throws DaoException {
            if (joinPredicateList == null || joinPredicateList.size() == 0) return null;
            int i = joinPredicateList.size() - 1;
            Predicate lastPredicate = joinPredicateList.get(i);
            while (i > 0) {
                i--;
                lastPredicate = Predicate.get(
                        joinPredicateList.get(i),
                        PredicateOperator.AND,
                        lastPredicate
                );
            }
            return lastPredicate;
        }

        // combines

        private String getWildcard() throws DaoException {
            if (this.operator == PredicateOperator.IN) {
                boolean first = true;
                String result = "";
                for (int i = 0; i < this.wildValueArray.length; i++) {
                    if (first) {
                        first = false;
                        result = result.concat(PAR_OPEN);
                    } else {
                        result = result.concat(Sql.SIGN_COMMA);
                    }
                    result = result.concat(this.wildValueArray[i].makeWildcard());
                }
                return result.concat(PAR_CLOSE);
            } else if (this.operator == PredicateOperator.BETWEEN) {
                return this.wildValueArray[0].makeWildcard().concat(AND).concat(this.wildValueArray[1].makeWildcard());
            } else {
                return this.wildValueArray[0].makeWildcard();
            }
        }

        private StringBuilder left(StringBuilder result) throws DaoException {
            return this.column != null ? this.column.appendClause(result) :
                    this.predicate != null ? this.predicate.appendClause(result) : result.append(getWildcard());
        }

        private StringBuilder right(StringBuilder result) throws DaoException {
            return this.compareColumn != null ? this.compareColumn.appendClause(result) :
                    this.comparePredicate != null ? this.comparePredicate.appendClause(result) : result.append(getWildcard());
        }

        @Override
        public StringBuilder appendClause(StringBuilder result) throws DaoException {
            switch (this.operator) {
                case EQUAL:
                    return right(left(result).append(EQUAL));
                case NOT_EQUAL:
                    return right(left(result).append(NOT_EQUAL));
                case MORE:
                    return right(left(result).append(MORE));
                case MORE_OR_EQUAL:
                    return right(left(result).append(MORE_OR_EQUAL));
                case LESS:
                    return right(left(result).append(LESS));
                case LESS_OR_EQUAL:
                    return right(left(result).append(LESS_OR_EQUAL));
                case IN:
                    return left(result).append(IN).append(getWildcard());
                case OR:
                    return right(left(result).append(OR));
                case AND:
                    return right(left(result).append(AND));
                case BETWEEN:
                    return left(result).append(BETWEEN).append(getWildcard());
                case LIKE:
                    return right(left(result).append(LIKE));
                case IS_NULL:
                    return left(result).append(IS_NULL);
                case IS_NOT_NULL:
                    return left(result).append(IS_NOT_NULL);

            }
            return result;
        }
    }

}

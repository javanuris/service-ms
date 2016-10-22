package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.exception.AppException;

import java.util.ArrayList;
import java.util.List;

import static com.epam.java.rt.lab.dao.sql.SqlExceptionCode.*;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.*;

/**
 * {@code Where} class defines where clause of sql statement
 */
public class Where implements Clause {

    private static final String WHERE = " WHERE ";

    /** {@code Join} object */
    private Select.Join join;
    /** {@code Predicate} object */
    private Predicate predicate;

    /**
     * Initiates new {@code Where} object with defined
     * {@code Join} object and {@code Predicate} object
     *
     * @param join          {@code Join} object
     * @param predicate     {@code Predicate} object
     * @throws AppException
     *
     * @see com.epam.java.rt.lab.dao.sql.Select.Join
     */
    Where(Select.Join join, Predicate predicate) throws AppException {
        this.join = join;
        Predicate joinPredicate = null;
        if (join == null && predicate == null) return;
        if (predicate != null) addJoinFromPredicate(predicate);
        // checked join from where clause predicate's columns
        if (join != null) joinPredicate = join.getPredicate();
        if (joinPredicate != null && predicate != null) {
            this.predicate = Predicate.get(joinPredicate,
                    Predicate.PredicateOperator.AND, predicate);
        } else if (predicate != null) {
            this.predicate = predicate;
        } else {
            this.predicate = joinPredicate;
        }
    }

    /**
     * Adds {@code Join} table to join clause from {@code Predicate}
     *
     * @param predicate     {@code Predicate} object
     * @throws AppException
     */
    private void addJoinFromPredicate(Predicate predicate)
            throws AppException {
        if (predicate == null) throw new AppException(NULL_NOT_ALLOWED);
        if (predicate.predicate != null) {
            addJoinFromPredicate(predicate.predicate);
        }
        if (predicate.comparePredicate != null) {
            addJoinFromPredicate(predicate.comparePredicate);
        }
        if (predicate.column != null && this.join != null) {
            this.join.addJoin(predicate.column.getTableName());
        }
        if (predicate.compareColumn != null && this.join != null) {
            this.join.addJoin(predicate.compareColumn.getTableName());
        }
    }

    /**
     * Links to base single {@code List} of {@code WildValue} objects
     *
     * @param wildValueList     {@code List} of {@code WildValue} objects
     * @throws AppException
     */
    void linkWildValue(List<WildValue> wildValueList) throws AppException {
        if (wildValueList == null) throw new AppException(NULL_NOT_ALLOWED);
        if (this.predicate != null) {
            linkWildValue(wildValueList, this.predicate);
        }
    }

    /**
     * Recursive method which links to base single {@code List} of
     * {@code WildValue} objects from {@code Predicate} object
     *
     * @param wildValueList     {@code List} of {@code WildValue} objects
     * @param predicate         {@code Predicate} object
     * @throws AppException
     */
    private void linkWildValue(List<WildValue> wildValueList,
                               Predicate predicate) throws AppException {
        if (wildValueList == null || predicate == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        if (predicate.predicate != null) {
            linkWildValue(wildValueList, predicate.predicate);
        }
        if (predicate.comparePredicate != null) {
            linkWildValue(wildValueList, predicate.comparePredicate);
        }
        if (predicate.wildValueArray != null) {
            for (WildValue wildValue : predicate.wildValueArray) {
                wildValue.link(wildValueList);
            }
        }
    }

    @Override
    public StringBuilder appendClause(StringBuilder result)
            throws AppException {
        return ((this.predicate == null) ? result
                : this.predicate.appendClause(result.append(WHERE)));
    }

    /**
     * {@code Predicate} class defines where predicate
     */
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

        /**
         * {@code Predicate} operator enumeration
         */
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

        Predicate(Column column, PredicateOperator operator,
                  Column compareColumn) {
            this.column = column;
            this.operator = operator;
            this.compareColumn = compareColumn;
        }

        private Predicate(Predicate predicate, PredicateOperator operator,
                          Predicate comparePredicate) {
            this.predicate = predicate;
            this.operator = operator;
            this.comparePredicate = comparePredicate;
        }

        private Predicate(Column column, PredicateOperator operator,
                          WildValue[] wildValueArray) {
            this.column = column;
            this.operator = operator;
            this.wildValueArray = wildValueArray;
        }

        Predicate(Column column, PredicateOperator operator) {
            this.column = column;
            this.operator = operator;
        }

        public static Predicate get(EntityProperty entityProperty,
                                    PredicateOperator operator,
                                    EntityProperty joinEntityProperty)
                throws AppException {
            if (entityProperty == null || joinEntityProperty == null
                    || operator == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            if (!PredicateOperator.EQUAL.equals(operator)) {
                throw new AppException(PREDICATE_ONLY_EQUAL_ALLOWED);
            }
            return new Predicate(Sql.getColumn(entityProperty),
                    operator, Sql.getColumn(joinEntityProperty));
        }

        public static Predicate get(Predicate predicate,
                                    PredicateOperator operator,
                                    Predicate comparePredicate)
                throws AppException {
            if (predicate == null || comparePredicate == null
                    || operator == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            if (!PredicateOperator.AND.equals(operator)
                    && !PredicateOperator.OR.equals(operator)) {
                throw new AppException(PREDICATE_ONLY_AND_OR_ALLOWED);
            }
            return new Predicate(predicate, operator, comparePredicate);
        }

        public static <T> Predicate get(EntityProperty entityProperty,
                                        PredicateOperator operator,
                                        T... valArray)
                throws AppException {
            if (entityProperty == null || operator == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            if (PredicateOperator.IS_NULL.equals(operator)
                    || PredicateOperator.IS_NOT_NULL.equals(operator)
                    || PredicateOperator.AND.equals(operator)
                    || PredicateOperator.OR.equals(operator)
                    || (PredicateOperator.BETWEEN.equals(operator)
                    && valArray.length != 2) || valArray.length == 0) {
                throw new AppException(PREDICATE_SPECIAL_ALLOWED);
            }
            List<WildValue> wildValueList = new ArrayList<>();
            for (T val : valArray) wildValueList.add(new WildValue<T>(val));
            WildValue[] wildValueArray = {};
            return new Predicate(Sql.getColumn(entityProperty), operator,
                    wildValueList.toArray(wildValueArray));
        }

        static Predicate isNull(EntityProperty entityProperty)
                throws AppException {
            if (entityProperty == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            return new Predicate(Sql.getColumn(entityProperty),
                    PredicateOperator.IS_NULL);
        }

        static Predicate isNotNull(EntityProperty entityProperty)
                throws AppException {
            if (entityProperty == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            return new Predicate(Sql.getColumn(entityProperty),
                    PredicateOperator.IS_NOT_NULL);
        }

        public static Predicate get(List<Predicate> joinPredicateList)
                throws AppException {
            if (joinPredicateList == null || joinPredicateList.size() == 0) {
                return null;
                //throw new AppException(NULL_NOT_ALLOWED);
            }
            int i = joinPredicateList.size() - 1;
            Predicate lastPredicate = joinPredicateList.get(i);
            while (i > 0) {
                i--;
                lastPredicate = Predicate.get(joinPredicateList.get(i),
                        PredicateOperator.AND, lastPredicate);
            }
            return lastPredicate;
        }

        private String getWildcard() throws AppException {
            if (PredicateOperator.IN.equals(this.operator)) {
                boolean first = true;
                StringBuilder result = new StringBuilder();
                for (WildValue aWildValueArray : this.wildValueArray) {
                    if (first) {
                        first = false;
                        result.append(LEFT_PARENTHESIS);
                    } else {
                        result.append(COMMA);
                    }
                    result.append(aWildValueArray.makeWildcard());
                }
                return result.append(RIGHT_PARENTHESIS).toString();
            } else if (PredicateOperator.BETWEEN.equals(this.operator)) {
                return this.wildValueArray[0].makeWildcard()
                        + AND + this.wildValueArray[1].makeWildcard();
            } else {
                return this.wildValueArray[0].makeWildcard();
            }
        }

        private StringBuilder left(StringBuilder result) throws AppException {
            if (result == null) throw new AppException(NULL_NOT_ALLOWED);
            return (this.column != null)
                    ? this.column.appendClause(result)
                    : ((this.predicate != null)
                    ? this.predicate.appendClause(result)
                    : result.append(getWildcard()));
        }

        private StringBuilder right(StringBuilder result) throws AppException {
            if (result == null) throw new AppException(NULL_NOT_ALLOWED);
            return (this.compareColumn != null)
                    ? this.compareColumn.appendClause(result)
                    : ((this.comparePredicate != null)
                    ? this.comparePredicate.appendClause(result)
                    : result.append(getWildcard()));
        }

        @Override
        public StringBuilder appendClause(StringBuilder result)
                throws AppException {
            if (result == null) throw new AppException(NULL_NOT_ALLOWED);
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

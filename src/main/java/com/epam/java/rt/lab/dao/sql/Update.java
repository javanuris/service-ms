package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.util.StringArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * service-ms
 */
public class Update extends Sql {

    private static final String UPDATE = "UPDATE ";

    private Class entityClass;
    private String table;
    private Set set;
    private Where where;

    Update(Class entityClass) throws DaoException {
        if (entityClass == null)
            throw new DaoException("exception.dao.sql.update.empty-class");
        this.entityClass = entityClass;
        this.table = getProperty(entityClass.getName());
    }

    public Update set(SetValue[] setValueArray) throws DaoException {
        for (SetValue setValue : setValueArray)
            if (!entityClass.equals(setValue.entityProperty.getEntityClass()))
                throw new DaoException("exception.dao.sql.update.entity-class-property");
        this.set = new Set(setValueArray);
        this.set.linkWildValue(getWildValueList());
        return this;
    }

    public Update where(Where.Predicate predicate) throws DaoException {
        if (predicate == null)
            throw new DaoException("exception.dao.sql.update.empty-predicate");
        this.where = new Where(null, predicate);
        this.where.linkWildValue(getWildValueList());
        return this;
    }

    @Override
    public String create() throws DaoException {
        try {
            return this.where.appendClause(
                    this.set.appendClause(
                            new StringBuilder().append(UPDATE).append(table)
                    )
            ).toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.sql.update.combine", e.getCause());
        }
    }

    /**
     * The usage is only in update statement
     */
    static class Set implements Clause {

        private static final String SET = " SET ";

        private SetValue[] setValueArray;

        Set(SetValue[] setValueArray) {
            this.setValueArray = setValueArray;
        }

        private void linkWildValue(List<WildValue> wildValueList) {
            if (this.setValueArray != null) {
                for (SetValue setValue : this.setValueArray)
                    setValue.wildValue.link(wildValueList);
            }
        }

        @Override
        public StringBuilder appendClause(StringBuilder result) throws DaoException {
            try {
                return this.setValueArray == null || this.setValueArray.length == 0 ? result :
                        StringArray.combine(result.append(SET), new ArrayList<>(Arrays.asList(this.setValueArray)), Sql.COMMA_DELIMITER);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.sql.set-values.combine", e.getCause());
            }
        }

    }

    public static class SetValue implements Clause {

        private static final String EQUAL = " = ";

        private EntityProperty entityProperty;
        private WildValue wildValue;

        public <T> SetValue(EntityProperty entityProperty, T value) throws DaoException {
            if (entityProperty == null)
                throw new DaoException("exception.dao.update.empty-entity-property");
            this.entityProperty = entityProperty;
            this.wildValue = new WildValue(value);
        }

        @Override
        public StringBuilder appendClause(StringBuilder result) throws DaoException {
            return result.append(getColumn(this.entityProperty).getColumnName()).append(EQUAL).append(this.wildValue.makeWildcard());
        }
    }

}

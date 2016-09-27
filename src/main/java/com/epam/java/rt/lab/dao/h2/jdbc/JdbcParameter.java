package com.epam.java.rt.lab.dao.h2.jdbc;

/**
 * service-ms
 */
public class JdbcParameter {

//    private Map<JdbcParameterType, Object> parameterMap;
//    private JdbcDao_ jdbcDao;
//
//    private JdbcParameter(Parameter_ parameter, JdbcDao_ jdbcDao) throws DaoException {
//        this.jdbcDao = jdbcDao;
//        use(parameter);
//        extend();
//    }
//
//    static JdbcParameter of(Parameter_ parameter, JdbcDao_ jdbcDao) throws DaoException {
//        if (parameter == null || jdbcDao == null)
//            throw new DaoException("exception.dao.jdbc.jdbc-parameter.of.parameter");
//        return new JdbcParameter(parameter, jdbcDao);
//    }
//
//    public JdbcParameter put(JdbcParameterType type, Object... valueArray) {
//        this.parameterMap.put(type, valueArray.length == 1 ? valueArray[0] : valueArray);
//        return this;
//    }
//
//    public Object get(JdbcParameterType type) {
//        return this.parameterMap.get(type);
//    }
//
//    /*
//        ======================
//        PREPARE JDBC PARAMETER
//    */
//    private void use(Parameter_ parameter)
//            throws DaoException {
//        this.parameterMap = new HashMap<>();
//        Object resultPropertyArrayParameter = parameter.get(ParameterType.RESULT_PROPERTY_ARRAY);
//        if (resultPropertyArrayParameter != null) {
//            EntityProperty[] resultPropertyArray =
//                    (EntityProperty[]) CastManager.getArray(resultPropertyArrayParameter, EntityProperty.class);
//            if (resultPropertyArray == null)
//                throw new DaoException("exception.dao.jdbc.parameter.use.result-property-array");
//            put(
//                    JdbcParameterType.SELECT_COLUMN_LIST,
//                    getColumnList(resultPropertyArray)
//            );
//        } else {
//            put(
//                    JdbcParameterType.SELECT_COLUMN_LIST,
//                    this.jdbcDao.getParameterAllSelectColumnList()
//            );
//        }
//        Object setFieldArrayParameter = parameter.get(ParameterType.SET_FIELD_ARRAY);
//        Parameter_.Field[] setFieldArray =
//                (Parameter_.Field[]) CastManager.getArray(setFieldArrayParameter, Parameter_.Field.class);
//        if (setFieldArrayParameter != null && setFieldArray == null)
//            throw new DaoException("exception.dao.jdbc.parameter.use.set-field-array");
//        put(
//                JdbcParameterType.SET_FIELD_LIST,
//                getFieldList(setFieldArray)
//        );
//        Object whereFieldArrayParameter = parameter.get(ParameterType.WHERE_FIELD_ARRAY);
//        Parameter_.Field[] whereFieldArray =
//                (Parameter_.Field[]) CastManager.getArray(whereFieldArrayParameter, Parameter_.Field.class);
//        if (whereFieldArrayParameter != null && whereFieldArray == null)
//            throw new DaoException("exception.dao.jdbc.parameter.use.where-field-array");
//        put(
//                JdbcParameterType.WHERE_FIELD_LIST,
//                getFieldList(whereFieldArray)
//        );
//        put(
//                JdbcParameterType.LIMIT_OFFSET,
//                parameter.get(ParameterType.LIMIT_OFFSET)
//        );
//        put(
//                JdbcParameterType.LIMIT_COUNT,
//                parameter.get(ParameterType.LIMIT_COUNT)
//        );
//        put(
//                JdbcParameterType.ORDER_TYPE,
//                parameter.get(ParameterType.ORDER_TYPE)
//        );
//        Object orderPropertyArrayParameter = parameter.get(ParameterType.ORDER_PROPERTY_ARRAY);
//        EntityProperty[] orderPropertyArray =
//                (EntityProperty[]) CastManager.getArray(orderPropertyArrayParameter, EntityProperty.class);
//        if (resultPropertyArrayParameter != null && orderPropertyArray == null)
//            throw new DaoException("exception.dao.jdbc.parameter.use.order-property-array");
//        put(
//                JdbcParameterType.ORDER_COLUMN_LIST,
//                getColumnList(orderPropertyArray)
//        );
//        put(
//                JdbcParameterType.CUSTOM,
//                parameter.get(ParameterType.CUSTOM)
//        );
//    }
//
//    private List<Column> getColumnList(EntityProperty[] propertyArray) {
//        List<Column> columnList = new ArrayList<>();
//        for (EntityProperty property : propertyArray)
//            columnList.add(this.jdbcDao.getColumn(property));
//        return columnList;
//    }
//
//    private List<Field> getFieldList(Parameter_.Field[] fieldArray) {
//        List<Field> fieldList = new ArrayList<>();
//        for (Parameter_.Field field : fieldArray) {
//            fieldList.add(new Field(
//                    field,
//                    this.jdbcDao.getColumn(field.getEntityProperty()),
//                    this.jdbcDao.getColumn(field.getCompareEntityProperty())
//            ));
//        }
//        return fieldList;
//    }
//
//    private void extend() throws DaoException {
//        List<String> joinTableNameList = new ArrayList<>();
//        List<JdbcParameter.Field> joinWhereFieldList = new ArrayList<>();
//        // prepare join section and where section according to join from Select section
//        List<Column> selectColumnList =
//                CastManager.getList(get(JdbcParameterType.SELECT_COLUMN_LIST), Column.class);
//        detectJoin(joinTableNameList, joinWhereFieldList, selectColumnList);
//        // another prepare join section from filter section
//        List<Field> whereFieldList =
//                CastManager.getList(get(JdbcParameterType.WHERE_FIELD_LIST), Field.class);
//        detectJoin(joinTableNameList, joinWhereFieldList, whereFieldList);
//        // complete join section
//        put(
//                JdbcParameterType.JOIN_TABLES,
//                StringArray.combine(joinTableNameList, StringArray.COMMA_DELIMITER)
//        );
//        // complete where section
//        put(
//                JdbcParameterType.WHERE_FIELD_LIST,
//                whereFieldList.addAll(joinWhereFieldList)
//        );
//    }
//
//    private void detectJoin(List<String> joinTableNameList, List<JdbcParameter.Field> joinWhereFieldList,
//                            List<?> columnOrFieldList) throws DaoException {
//        if (columnOrFieldList.getClass().isAssignableFrom(Column.class)) {
//            for (Column column: CastManager.getList(columnOrFieldList, Column.class))
//                addJoin(joinTableNameList, joinWhereFieldList, column);
//        } else if (columnOrFieldList.getClass().isAssignableFrom(Field.class)) {
//            for (Field field : CastManager.getList(columnOrFieldList, Field.class))
//                addJoin(joinTableNameList, joinWhereFieldList, field.column);
//        }
//    }
//
//    private void addJoin(List<String> joinTableNameList, List<JdbcParameter.Field> joinWhereFieldList,
//                         Column column) throws DaoException {
//        if (!column.tableName.equals(this.jdbcDao.getParameterDefaultFrom()) &&
//                !joinTableNameList.contains(column.getTableName())) {
//            joinTableNameList.add(column.getTableName());
//            joinWhereFieldList.add(this.jdbcDao.getParameterJoinWhereField(column.getTableName()));
//        }
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder result = new StringBuilder();
//        for (Map.Entry<JdbcParameterType, Object> parameter : this.parameterMap.entrySet())
//            result.append("\n").append(parameter.getKey()).append("=").append(parameter.getValue());
//        return result.toString();
//    }
//
//    public static class Column {
//
//        private static final String TABLE_COLUMN_DELIMITER = ".";
//
//        private String tableName;
//        private String columnName;
//
//        public Column(String tableName, String columnName) {
//            this.tableName = tableName;
//            this.columnName = columnName;
//        }
//
//        public String getTableName() {
//            return tableName;
//        }
//
//        public String getColumnName() {
//            return columnName;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            Column column = (Column) obj;
//            return column != null && this.tableName.equals(column.tableName) &&
//                    this.columnName.equals(column.columnName);
//        }
//
//        @Override
//        public String toString() {
//            return this.tableName.concat(TABLE_COLUMN_DELIMITER).concat(this.columnName);
//        }
//    }
//
//    public static class Field {
//        private Parameter_.Field parameterField;
//        private Column column;
//        private Column compareColumn;
//
//        public Field(Parameter_.Field parameterField, Column column, Column compareColumn) {
//            this.parameterField = parameterField;
//            this.column = column;
//            this.compareColumn = compareColumn;
//        }
//
//        public <T> T getValue() {
//            return parameterField == null ? null : (T) parameterField.getValue();
//        }
//
//        public Column getColumn() {
//            return column;
//        }
//
//        public Column getCompareColumn() {
//            return compareColumn;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            Field field = (Field) obj;
//            return field != null && field.column.equals(this.column) &&
//                    field.parameterField.equals(this.parameterField) &&
//                    field.compareColumn.equals(this.compareColumn);
//        }
//
//        @Override
//        public String toString() {
//            return this.column.toString().concat(
//                    this.parameterField.getValue() == null && this.compareColumn == null ?
//                            " is null" : this.compareColumn == null ?
//                            " = ?" : " = ".concat(this.compareColumn.toString())
//            );
//        }
//    }

}

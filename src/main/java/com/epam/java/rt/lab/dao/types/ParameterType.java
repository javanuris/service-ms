package com.epam.java.rt.lab.dao.types;

/**
 * service-ms
 */
public enum ParameterType {
    RESULT_PROPERTY_ARRAY,  // Property array
    SET_FIELD_ARRAY,        // Property array
    WHERE_FIELD_ARRAY,      // Property array
    LIMIT_OFFSET,           // Long value of offset
    LIMIT_COUNT,            // Long value of count
    ORDER_TYPE,             // Enum OrderType, could be ASC or DESC
    ORDER_PROPERTY_ARRAY,   // Property array
    CUSTOM                  // Extra type used to store mapped values/objects
}

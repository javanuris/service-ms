package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * service-ms
 */
public class SelectUnknownEntityTest {

    public enum RolePermissionProperty implements EntityProperty {
        ID,
        ROLE_ID,
        PERMISSION_ID;

        @Override
        public Class getEntityClass() {
            return SelectUnknownEntityTest.RolePermissionProperty.class;
        }
    }


    @Test
    public void selectUnknownEntity() {
        try {
            Select select = Sql.select(Permission.Property.ID, Permission.Property.URI, RolePermissionProperty.ROLE_ID);
            select.where(
                    Where.Predicate.get(
                            RolePermissionProperty.ROLE_ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            2
                    )
            );
            System.out.println(select.create());
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

}
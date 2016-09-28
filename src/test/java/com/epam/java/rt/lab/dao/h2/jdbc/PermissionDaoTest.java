package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.dao.sql.SelectUnknownEntityTest;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.service.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * service-ms
 */
public class PermissionDaoTest {

    private static final String PERMISSION_READ_A = "SELECT \"Permission\".id, \"Permission\".uri, \"RolePermission\".role_id FROM \"Permission\" JOIN \"RolePermission\", \"Role\" WHERE \"RolePermission\".permission_id = \"Permission\".id AND \"Role\".id = \"RolePermission\".role_id AND \"Role\".id = ?";
    private static final String PERMISSION_READ_B = "SELECT \"Permission\".id, \"Permission\".uri, \"RolePermission\".role_id FROM \"Permission\" JOIN \"RolePermission\" WHERE \"RolePermission\".permission_id = \"Permission\".id ORDER BY \"Role\".name ASC";
    private static final String PERMISSION_READ_C = "SELECT \"Permission\".id, \"Permission\".uri, \"RolePermission\".role_id FROM \"Permission\" JOIN \"RolePermission\" WHERE \"RolePermission\".permission_id = \"Permission\".id LIMIT 0, 10";

    private DaoFactory daoFactory;
    private DaoParameter daoParameter;
    private Dao dao;

    @Before
    public void setUp() throws Exception {
        try {
            this.daoFactory = AbstractDaoFactory.createDaoFactory();
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.base.dao", e.getCause());
        }
    }

    @After
    public void tearDown() throws Exception {
        this.daoParameter = null;
        this.daoFactory.close();
        this.daoFactory = null;
    }

    @Test
    public void getSqlCreate() throws Exception {

    }

    @Test
    public void getSqlRead() throws Exception {
        dao = daoFactory.createDao("Permission");
        this.daoParameter = new DaoParameter()
                .setWherePredicate(
                        Where.Predicate.get(
                                Role.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                1
                        )
                );
        assertEquals("getSqlRead(WHERE) failed", PERMISSION_READ_A, ((PermissionDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter()
                .setOrderByCriteriaArray(
                        OrderBy.Criteria.asc(Role.Property.NAME)
                );
        assertEquals("getSqlRead(ORDER) failed", PERMISSION_READ_B, ((PermissionDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter().setLimit(0, 10);
        assertEquals("getSqlRead(LIMIT) failed", PERMISSION_READ_C, ((PermissionDao) dao).getSqlRead(this.daoParameter).create());
    }

    @Test
    public void getSqlUpdate() throws Exception {

    }

    @Test
    public void getSqlDelete() throws Exception {

    }

    @Test
    public void getEntity() throws Exception {
        dao = daoFactory.createDao("Permission");
        this.daoParameter = new DaoParameter()
                .setWherePredicate(
                        Where.Predicate.get(
                                Permission.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                1L
                        )
                );
        Sql sql = ((PermissionDao) dao).getSqlRead(this.daoParameter);
        PreparedStatement statement = ((PermissionDao) dao).getConnection().prepareStatement(sql.create());
        statement.setLong(1, (Long) sql.getWildValueList().get(0).getVal());
        ResultSet resultSet = statement.executeQuery();
        List<Permission> permissionList = ((PermissionDao) dao).getEntity(resultSet, sql);
        Permission permission = permissionList.get(0);
        assertNotNull("getEntity() failed", permission);
    }

}
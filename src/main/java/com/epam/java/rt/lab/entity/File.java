package com.epam.java.rt.lab.entity;

import java.io.InputStream;
import java.sql.Timestamp;


public interface File {

    String getType();

    InputStream getFile();

    Timestamp getModified();

}

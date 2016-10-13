package com.epam.java.rt.lab.util.file;

import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.StringArray;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public final class UploadManager {

    private static final String FILE_PATH_PREFIX = "filePath=";
    private static final String CHARSET_NAME_UTF_8 = "UTF-8";
    private static final String CHARSET_NAME_LATIN_1 = "ISO-8859-1";
    private static final String CONTENT_TYPE_PROPERTY_FILE = "content-type.properties";
    private static Map<String, List<String>> contentTypeListMap = new HashMap<>();

    private UploadManager() {
    }

    public static void initContentTypeListMap() throws UploadException {
        ClassLoader classLoader = UploadManager.class.getClassLoader();
        InputStream inputStream = classLoader.
                getResourceAsStream(CONTENT_TYPE_PROPERTY_FILE);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            Enumeration names = properties.propertyNames();
            while (names.hasMoreElements()) {
                String key = (String) names.nextElement();
                String property = properties.getProperty(key);
                String[] typeArray = StringArray.
                        splitSpaceLessNames(property, PropertyManager.COMMA);
                List<String> typeList = new ArrayList<>(Arrays.asList(typeArray));
                contentTypeListMap.put(key, typeList);
            }
        } catch (IOException e) {
            throw new UploadException(e.getCause());
        }
    }

    public static String uploadFileAndGetAbsolutePath(
            String sessionId, String uploadType, Part filePart)
            throws UploadException {
        List<String> contentTypeList = contentTypeListMap.get(uploadType);
        if (contentTypeList == null) return null;
        String contentType = filePart.getContentType();
        if (!contentTypeList.contains(contentType)) return null;
        String fileName = filePart.getSubmittedFileName();
        contentType = contentType.replaceAll(PropertyManager.SLASH,
                                             PropertyManager.UNDERSCORE);
        String prefix = PropertyManager.POINT + uploadType
                + PropertyManager.POINT + contentType
                + PropertyManager.POINT;
        String postfix = PropertyManager.POINT + sessionId;
        try {
            File outputFile = File.createTempFile(fileName + prefix, postfix);
            InputStream inputStream = filePart.getInputStream();
            Files.copy(inputStream, outputFile.toPath(),
                       StandardCopyOption.REPLACE_EXISTING);
            inputStream.close();
            Charset charsetUtf8 = Charset.forName(CHARSET_NAME_UTF_8);
            Charset charsetLatin1 = Charset.forName(CHARSET_NAME_LATIN_1);
            byte[] outputFilePath = outputFile.getAbsolutePath().
                                    getBytes(charsetUtf8);
            return FILE_PATH_PREFIX + new String(outputFilePath, charsetLatin1);
        } catch (IOException e) {
            throw new UploadException(e.getCause());
        }
    }

}

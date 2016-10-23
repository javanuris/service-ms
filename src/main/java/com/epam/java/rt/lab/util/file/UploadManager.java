package com.epam.java.rt.lab.util.file;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.exception.AppExceptionCode;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.StringCombiner;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.util.file.FileExceptionCode.*;

public final class UploadManager {

    private static final String FILE_PATH_PREFIX = "filePath=";
    private static final String CHARSET_NAME_UTF_8 = "UTF-8";
    private static final String CHARSET_NAME_LATIN_1 = "ISO-8859-1";
    private static final String CONTENT_TYPE_PROPERTY_FILE =
            "content-type.properties";
    private static Map<String, List<String>> contentTypeListMap =
            new HashMap<>();

    private UploadManager() {
    }

    public static void initContentTypeListMap() throws AppException {
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
                String[] typeArray = StringCombiner.
                        splitSpaceLessNames(property, PropertyManager.COMMA);
                List<String> typeList = new ArrayList<>(Arrays.asList(typeArray));
                contentTypeListMap.put(key, typeList);
            }
        } catch (IOException e) {
            String[] detailArray = {CONTENT_TYPE_PROPERTY_FILE};
            throw new AppException(AppExceptionCode.PROPERTY_READ_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

    public static String receiveFileAndGetAbsolutePath(
            String sessionId, String uploadType, Part filePart)
            throws AppException {
        if (sessionId == null || uploadType == null || filePart == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        List<String> contentTypeList = contentTypeListMap.get(uploadType);
        if (contentTypeList == null) {
            String[] detailArray = {uploadType};
            throw new AppException(UPLOAD_TYPE_ERROR, detailArray);
        }
        String contentType = filePart.getContentType();
        if (!contentTypeList.contains(contentType)) {
            String[] detailArray = {contentType};
            throw new AppException(CONTENT_TYPE_ERROR, detailArray);
        }
        String fileName = filePart.getSubmittedFileName();
        String prefix = getPrefixFromMetaInfo(fileName, uploadType, contentType);
        String suffix = POINT + sessionId;
        try {
            File outputFile = File.createTempFile(prefix, suffix);
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
            String[] detailArray = {prefix, suffix};
            throw new AppException(FILE_ACCESS_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

    private static String getPrefixFromMetaInfo(String fileName,
                                                String uploadType,
                                                String contentType) {
        contentType = contentType.replaceAll(SLASH, UNDERSCORE);
        return fileName + POINT + uploadType + POINT + contentType + POINT;
    }

    public static String[] getMetaInfoFromPrefix(String fileNamePrefix,
                                                 String uploadType)
            throws AppException {
        if (fileNamePrefix == null || uploadType == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        int prefixLastPoint = fileNamePrefix.lastIndexOf(ESCAPED_POINT);
        fileNamePrefix = fileNamePrefix.substring(0, prefixLastPoint);
        uploadType = ESCAPED_POINT + uploadType + ESCAPED_POINT;
        String[] result = fileNamePrefix.split(uploadType);
        if (result.length != 2) {
            String[] detailArray = {fileNamePrefix, uploadType};
            throw new AppException(META_INFO_ERROR, detailArray);
        }
        result[1] = result[1].replaceAll(UNDERSCORE, SLASH);
        return result;
    }

}
package com.epam.java.rt.lab.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;

/**
 * service-ms
 */
public class FileManager {
    private static final Logger logger = LoggerFactory.getLogger(FileManager.class);

    private FileManager() {
    }

    public static void deleteFilesWithExtension(String folderPath, String extension) {
        ExtensionFilter extensionFilter = new ExtensionFilter(folderPath, extension);
        File folder = new File(folderPath);
        String[] fileArray = folder.list(extensionFilter);
        if (fileArray.length > 0) {
            folderPath = folderPath.concat(File.separator);
            for (String fileName : fileArray) {
                if (!new File(folderPath.concat(fileName)).delete())
                    logger.debug("Session scope temporary file not deleted: {}", fileName);
            }
        }

    }

    private static class ExtensionFilter implements FilenameFilter {
        private String folderPath;
        private String extension;

        private ExtensionFilter(String folderPath, String extension) {
            this.folderPath = folderPath;
            this.extension = extension;
        }

        @Override
        public boolean accept(File dir, String name) {
            return dir.equals(folderPath) && name.endsWith(extension);
        }
    }

}

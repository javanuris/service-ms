package com.epam.java.rt.lab.util.file;

import com.epam.java.rt.lab.exception.AppException;

import java.io.File;
import java.io.FilenameFilter;

import static com.epam.java.rt.lab.util.file.FileExceptionCode.FILE_DELETE_ERROR;

public final class FileManager {

    private FileManager() {
    }

    public static void deleteFilesWithExtension(String folderPath,
                                                String extension)
            throws AppException {
        ExtensionFilter extensionFilter =
                new ExtensionFilter(folderPath, extension);
        File folder = new File(folderPath);
        String[] fileArray = folder.list(extensionFilter);
        if (fileArray.length > 0) {
            folderPath = folderPath + File.separator;
            for (String fileName : fileArray) {
                if (!new File(folderPath + fileName).delete()) {
                    String[] detailArray = {fileName};
                    throw new AppException(FILE_DELETE_ERROR,detailArray);
                }
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
            return dir.getName().equals(folderPath) && name.endsWith(extension);
        }
    }

}
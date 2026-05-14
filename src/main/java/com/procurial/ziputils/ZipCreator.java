package com.procurial.ziputils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.procurial.gui.GUIStarter;

public class ZipCreator {
    private static final int BUFFER_SIZE = 4096;

    public static void unzipTo(String fileLoc) throws IOException {
        File destination = new File(fileLoc);
        if (destination.isDirectory()) {
            unzip("/pc2-9.10.0-7065.zip", destination.getAbsolutePath());
        } else {
            System.out.println("Path provided is not a directory and cannot be unzipped to.");
        }
    }

    public static void unzipFileTo(String zip, String fileLoc) throws IOException {
        File destination = new File(fileLoc);
        if (destination.isDirectory()) {
            String destDirectory = destination.getAbsolutePath();
            File destDir = new File(destDirectory);
            if (!destDir.exists()) {
                destDir.mkdir();
            }
            System.out.println("P"+zip);
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zip));
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!filePath.contains("__MACOSX")) {
                    if (!entry.isDirectory()) {
                        File filePathFile = new File(filePath);
                        File parent = new File(filePathFile.getParent());
                        parent.mkdirs();
                        extractFile(zipIn, filePath);
                        if (filePathFile.getName().toLowerCase().startsWith("pc2") && !filePathFile.getName().contains(".")) {
                            filePathFile.setExecutable(true);
                        }
                    } else {
                        File dir = new File(filePath);
                        dir.mkdirs();
                    }
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } else {
            System.out.println("Path provided is not a directory and cannot be unzipped to.");
        }
    }

    private static void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        System.out.println("P"+GUIStarter.class.getResource(zipFilePath));
        ZipInputStream zipIn = new ZipInputStream(GUIStarter.class.getResourceAsStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!filePath.contains("__MACOSX")) {
                if (!entry.isDirectory()) {
                    File filePathFile = new File(filePath);
                    File parent = new File(filePathFile.getParent());
                    parent.mkdirs();
                    extractFile(zipIn, filePath);
                    if (filePathFile.getName().toLowerCase().startsWith("pc2") && !filePathFile.getName().contains(".")) {
                        filePathFile.setExecutable(true);
                    }
                } else {
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public static void zipDirectoryTo(String sourceDirPath, String zipFilePath) throws IOException {
        File sourceDir = new File(sourceDirPath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new IOException("Source path is not a valid directory: " + sourceDirPath);
        }

        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zos = new ZipOutputStream(fos)) {

            File[] files = sourceDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    zipFile(file, file.getName(), zos);
                }
            }
        }
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zos) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }

        if (fileToZip.isDirectory()) {
            String entryName = fileName.endsWith("/") ? fileName : fileName + "/";
            zos.putNextEntry(new ZipEntry(entryName));
            zos.closeEntry();

            File[] children = fileToZip.listFiles();
            if (children != null) {
                for (File childFile : children) {
                    zipFile(childFile, fileName + "/" + childFile.getName(), zos);
                }
            }
        } else {
            try (FileInputStream fis = new FileInputStream(fileToZip)) {
                ZipEntry zipEntry = new ZipEntry(fileName);
                zos.putNextEntry(zipEntry);

                byte[] bytes = new byte[BUFFER_SIZE];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zos.write(bytes, 0, length);
                }
                zos.closeEntry();
            }
        }
    }
}
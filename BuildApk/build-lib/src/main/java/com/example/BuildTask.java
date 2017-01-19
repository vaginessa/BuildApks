package com.example;

import com.meituan.android.walle.ChannelWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class BuildTask {

    private static String fileRootPath;

    //apk所在默认路径
    private static final String apk_path = "build-lib\\apks";
    //指定apk路径的文件路径
    private static final String path_path = "build-lib\\path";
    //渠道路径
    private static final String channel_path= "build-lib\\channel";



    public static void main(String[] args) {

        //当前的路径
        fileRootPath = new File("").getAbsolutePath();

        File apkFiles = new File(getApkPath());

        if (apkFiles.exists()) {
            File[] files = apkFiles.listFiles();
            if (files != null && files.length > 0) {
                File sourceFile = files[0];
                ArrayList<ApkEntity> apkEntities = readChannelFile();
                if (apkEntities != null) {
                    for (ApkEntity apkEntity : apkEntities) {

                        String copyFilePath = copyFile(sourceFile
                                , new File(apkFiles, sourceFile.getName().substring(0, sourceFile.getName().lastIndexOf("_") + 1) + apkEntity.channelName + ".apk")
                                , true);
                        try {
                            if (copyFilePath != null) {
                                ChannelWriter.put(new File(copyFilePath), apkEntity.channelName, apkEntity.hashMap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }

    private static String getApkPath() {

        File file = new File(fileRootPath, path_path);
        String absolutePath = new File(fileRootPath, apk_path).getAbsolutePath();

        if (!file.exists()) {
            return absolutePath;
        } else {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
                try {
                    String line = bufferedReader.readLine();
                    if (line != null && line.trim().length() > 0 && !line.trim().startsWith("#")) {
                        absolutePath = line;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return absolutePath;

    }

    private static ArrayList<ApkEntity> readChannelFile() {

        File file = new File(fileRootPath, channel_path);
        ArrayList<ApkEntity> apkEntities = new ArrayList<>();
        if (file.exists()) {

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
                try {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.trim().length() > 0 && !line.trim().startsWith("#")) {
                            String[] strings = line.split(",");
                            ApkEntity apkEntity = new ApkEntity();
                            HashMap<String, String> params = new HashMap<>();
                            for (String string : strings) {
                                if (string.contains("=")) {
                                    String[] split = string.split("=");
                                    params.put(split[0], split[1]);
                                } else {
                                    apkEntity.channelName = string;
                                }
                            }
                            apkEntity.hashMap = params;
                            apkEntities.add(apkEntity);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return apkEntities;

    }

    private static String copyFile(File fromFile, File toFile, Boolean rewrite) {
        if (!fromFile.exists()) {
            return null;
        }
        if (!fromFile.isFile()) {
            return null;
        }
        if (!fromFile.canRead()) {
            return null;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }
        //当文件不存时，canWrite一直返回的都是false
        try {

            FileInputStream fosfrom = new FileInputStream(fromFile);

            FileOutputStream fosto = new FileOutputStream(toFile);

            byte bt[] = new byte[1024];

            int c;

            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); //将内容写到新文件当中

            }

            fosfrom.close();

            fosto.close();


        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return toFile.length() > 0 ? toFile.getAbsolutePath() : null;

    }

    private static class ApkEntity {

        String channelName;

        HashMap<String, String> hashMap;

        private ApkEntity() {
        }

        public ApkEntity(String channelName, HashMap<String, String> hashMap) {
            this.channelName = channelName;
            this.hashMap = hashMap;
        }
    }


}

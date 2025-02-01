package com.example.kacper.inzynierka.questions;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileService {

    public final static String uploadDir = "src/main/resources/static/uploads/";


    public String getImagePath(MultipartFile file) throws IOException {

        if(file !=null) {

            String timestamp = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

            String filename = timestamp + "_" + file.getOriginalFilename();

            byte[] bytes = file.getBytes();
            String imagePath = uploadDir + filename;

            Files.write(Paths.get(imagePath), bytes);

            imagePath = imagePath.replace("\\", "/");

            imagePath = imagePath.substring(imagePath.indexOf("/uploads"));

            return imagePath;
        }

        return "";
    }

//    public String testFtp(MultipartFile file){
//        String FTP_ADDRESS = "127.0.0.1";
//        String LOGIN = "user_image";
//        String PSW = "";
//
//        FTPClient con = null;
//
//        try {
//            con = new FTPClient();
//            con.connect(FTP_ADDRESS);
//
//            if (con.login(LOGIN, PSW)) {
//                con.enterLocalPassiveMode(); // important!
//                con.setFileType(FTP.BINARY_FILE_TYPE);
//
//                boolean result = con.storeFile(file.getOriginalFilename(), file.getInputStream());
//
//                con.logout();
//
//                con.disconnect();
//
//            }
//        } catch (Exception e) {
//
//        }
//
//        return "test";
//    }

}

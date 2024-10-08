package com.spring.goodsShop.etc;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ImgHandler {
    public void writeFile(MultipartFile fName, String saveFileName, String urlPath) throws IOException  {
        byte[] data = fName.getBytes();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String realPath = request.getSession().getServletContext().getRealPath("/resources/static/img/"+urlPath+"/");

        FileOutputStream fos = new FileOutputStream(realPath + saveFileName);
        fos.write(data);
        fos.close();
    }
}

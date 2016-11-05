package services.servlets;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig

public class UploadImage extends  HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public  UploadImage() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Lecture des parametres */
        String path=  "";
		  InputStream inputStream = null; // input stream of the upload file
	        // obtains the upload file part in this multipart request
	        Part filePart = request.getPart("file-0");
	        if (filePart != null) {
	            // prints out some information for debugging
	            System.out.println(filePart.getName());
	            System.out.println(filePart.getSize());
	            System.out.println(filePart.getContentType());
	             
	            // obtains input stream of the upload file
	            inputStream = filePart.getInputStream();
	            BufferedImage image = ImageIO.read(inputStream);
	            File f = new File("/home/ladi/images/test.png");
	           ImageIO.write(image, "png", new File("/home/ladi/images/test.png"));
	        }
	        PrintWriter writer = response.getWriter();
			response.setContentType("plain/text");
			writer.println(path);
			
	}
}

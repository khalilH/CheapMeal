package services.servlets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig

public class UploadImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UploadImage() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Lecture des parametres */
		String path = "";
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
			File f = new File("/var/lib/tomcat8/webapps/images/toz.png");
			ImageIO.write(image, "png", f);
		}
		PrintWriter writer = response.getWriter();
		response.setContentType("plain/text");

		File f = new File("/var/lib/tomcat8/webapps/images/toz.png");
		String encodstring = encodeFileToBase64Binary(f);

		writer.println(encodstring);

	}

	private static String encodeFileToBase64Binary(File file) {
		String encodedfile = null;
		try {
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fileInputStreamReader.read(bytes);
			encodedfile =  Base64.getEncoder().encodeToString(bytes);
			fileInputStreamReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedfile;
	}
}

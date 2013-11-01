package com.cnpanoramio.webapp.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.appfuse.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.cnpanoramio.domain.Photo;
import com.cnpanoramio.domain.Point;
import com.cnpanoramio.service.PhotoManager;
import com.cnpanoramio.service.PhotoService;
import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;

/**
 * Controller class to upload Files.
 * <p/>
 * <p>
 * <a href="FileUploadFormController.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/fileupload*")
public class FileUploadController extends BaseFormController {
 
	PhotoManager photoService = null;
	
	@Autowired
	public void setPhotoService(PhotoManager photoService) {
		this.photoService = photoService;
	}

	public FileUploadController() {
		setCancelView("redirect:/mainMenu");
		setSuccessView("uploadDisplay");
	}

	@ModelAttribute
	@RequestMapping(method = RequestMethod.GET)
	public FileUpload showForm() {
		return new FileUpload();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(FileUpload fileUpload, BindingResult errors,
			HttpServletRequest request) throws Exception {

		if (request.getParameter("cancel") != null) {
			return getCancelView();
		}

		if (validator != null) { // validator is null during testing
			validator.validate(fileUpload, errors);

			if (errors.hasErrors()) {
				return "fileupload";
			}
		}

		// validate a file was entered
		if (fileUpload.getFile().length == 0) {
			Object[] args = new Object[] { getText("uploadForm.file",
					request.getLocale()) };
			errors.rejectValue("file", "errors.required", args, "File");

			return "fileupload";
		}

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
				.getFile("file");
		
		Photo photo = null;
		try {
		   photo = photoService.save(file.getOriginalFilename(), file.getInputStream());
		}catch(Exception e) {
			e.printStackTrace();
		}

		// place the data into the request for retrieval on next page
		request.setAttribute("friendlyName", fileUpload.getName());
		request.setAttribute("fileName", file.getOriginalFilename());
		request.setAttribute("contentType", file.getContentType());
		request.setAttribute("size", file.getSize() + " bytes");
		request.setAttribute("location", photo.getName()
				+ Constants.FILE_SEP + file.getOriginalFilename());

		String link = request.getContextPath() + "/resources" + "/"
				+ request.getRemoteUser() + "/";
		request.setAttribute("link", link + file.getOriginalFilename());

		return getSuccessView();
	}

}

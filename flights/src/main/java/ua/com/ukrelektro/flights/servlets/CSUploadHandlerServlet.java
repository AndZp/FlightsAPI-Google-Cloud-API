package ua.com.ukrelektro.flights.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;

public class CSUploadHandlerServlet extends HttpServlet {

	private final static Logger LOG = Logger.getLogger(CSUploadHandlerServlet.class.getName());
	private final static String HOST = "https://and-flights-api.appspot.com";

	/*
	 * Object to be returned as JSON in HTTP-response (and can be stored in data
	 * base)
	 */
	class UploadedFileData {
		FileInfo fileInfo;
		String BlobKey;
		String fileServeServletLink;
		String servingUrlFromgsObjectName;
		String servingUrlFromGsBlobKey;
	} // end of uploadedFileData

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		// Returns the FileInfo for any files that were uploaded, keyed by the
		// upload form "name" field.
		// This method should only be called from within a request served by the
		// destination of a createUploadUrl call.
		// https://cloud.google.com/appengine/docs/java/javadoc/com/google/appengine/api/blobstore/BlobstoreService#getFileInfos-HttpServletRequest-
		java.util.Map<java.lang.String, java.util.List<FileInfo>> fileInfoListsMap = BlobstoreServiceFactory.getBlobstoreService().getFileInfos(req);
		LOG.warning("[LOGGER]: " + new Gson().toJson(fileInfoListsMap));

		ArrayList<UploadedFileData> uploadedFilesDataList = new ArrayList<>();

		for (java.util.List<FileInfo> fileInfoList : fileInfoListsMap.values()) {

			for (FileInfo fileInfo : fileInfoList) {

				UploadedFileData uploadedFileData = new UploadedFileData();
				uploadedFileData.fileInfo = fileInfo;

				LOG.warning("uploadedFileData created:" + new Gson().toJson(uploadedFileData));
				BlobKey blobKey = BlobstoreServiceFactory.getBlobstoreService().createGsBlobKey(fileInfo.getGsObjectName());
				uploadedFileData.BlobKey = blobKey.getKeyString();
				uploadedFileData.fileServeServletLink = HOST + "/serve?blob-key=" + blobKey.getKeyString();

				// Use Images Java API to create serving URL
				// works only for images (PNG, JPEG, GIF, TIFF, BMP, ICO, WEBP)
				for (com.google.appengine.api.images.Image.Format type : com.google.appengine.api.images.Image.Format.values()) {
					LOG.warning("com.google.appengine.api.images.Image.Format type: " + type.toString());
					LOG.warning("fileInfo.getContentType(): " + fileInfo.getContentType());
					if (fileInfo.getContentType().toLowerCase().contains(type.toString().toLowerCase())) {

						uploadedFileData.servingUrlFromgsObjectName = ImagesServiceFactory.getImagesService()
								.getServingUrl(ServingUrlOptions.Builder.withGoogleStorageFileName(fileInfo.getGsObjectName()));
						uploadedFileData.servingUrlFromGsBlobKey = ImagesServiceFactory.getImagesService()
								.getServingUrl(ServingUrlOptions.Builder.withBlobKey(blobKey));
					}
				}

				uploadedFilesDataList.add(uploadedFileData);

			}

		}

		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter(); // get the stream to write the data
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		pw.println(gson.toJson(uploadedFilesDataList));
		LOG.warning("uploadedFilesDataMap" + new Gson().toJson(uploadedFilesDataList));
		pw.close(); // closing the stream

	} // doPost End

}
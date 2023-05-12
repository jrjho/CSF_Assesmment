package ibf2022.batch2.csf.backend.repositories;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;




@Repository
public class ImageRepository {

	@Autowired
    private AmazonS3 s3Client;

    @Value("${do.storage.bucketname}")
    private String bucketName;

	//TODO: Task 3
	// You are free to change the parameter and the return type
	// Do not change the method's name
	public ArrayList<String>  upload(MultipartFile file, String name, String title, String comments) throws IOException {
		ArrayList<String> UrlArray = new ArrayList<>();
        Path tempDirectory = Files.createTempDirectory(UUID.randomUUID().toString());
        try (InputStream zipInputStream = file.getInputStream();
                ZipArchiveInputStream zis = new ZipArchiveInputStream(zipInputStream)) {

            ArchiveEntry entry;
            int count = 0;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String fileName = entry.getName();
                    String fileExtension = getFileExtension(fileName);
                    Path filePath = tempDirectory.resolve(fileName);
                    byte[] entryData = IOUtils.toByteArray(zis);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(entryData);
                    

                    System.out.println("filename: " + fileName);
                    System.out.println("fileExtension: " + fileExtension);
                    System.out.println("fileExtension: " + entry.getSize());
                    System.out.println("filepath before: " + filePath);
                    Files.copy(zis, filePath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("filepath after: " + filePath);  


                    // String key = UUID.randomUUID().
                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentType(getContentType(fileExtension));
                    metadata.setContentLength(entry.getSize());

                    PutObjectRequest putRequest = new PutObjectRequest(
                            bucketName, fileName , inputStream, metadata);
                    putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
                    s3Client.putObject(putRequest);

                    //get url of images and store in UrlArray
					URL result = s3Client.getUrl(bucketName, fileName);
					UrlArray.add(result.toString());
					System.out.println("URL result: " + result);
					count+=1;
                }
            }
            System.out.println("count: " + count);
			System.out.println("URL Array: " + UrlArray);

        }
        return UrlArray;
    }

	private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    private String getContentType(String fileExtension) {
        // Customize the content types as per your requirement
        switch (fileExtension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            // Add more cases for other file extensions if needed
            default:
                return "image/jpeg";
        }
    }
}

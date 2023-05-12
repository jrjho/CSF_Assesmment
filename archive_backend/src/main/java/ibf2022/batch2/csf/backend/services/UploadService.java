package ibf2022.batch2.csf.backend.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.batch2.csf.backend.models.Archives;
import ibf2022.batch2.csf.backend.repositories.ArchiveRepository;
import ibf2022.batch2.csf.backend.repositories.ImageRepository;
import jakarta.json.JsonObject;

@Service
public class UploadService {

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private ArchiveRepository archiveRepo;

    public ArrayList<String> uploadtoS3(MultipartFile file, String name, String title, String comments) throws IOException {

        ArrayList<String> urlArray = imageRepo.upload(file, name, title, comments);
        return urlArray;
    }

    public JsonObject uploadtoMongo(String name, String title, String comments, ArrayList<String> urlArray){
        String bundleId = UUID.randomUUID().toString().substring(0, 8);
        Document toInsert = toDocument(bundleId, name, title, comments, urlArray);
        JsonObject bundleIdJson = archiveRepo.recordBundle(bundleId, toInsert);
        return bundleIdJson;
    }

    public List<Archives> getInfoByBundleId(String bundleId){
        List<Archives> results = archiveRepo.getBundleByBundleId(bundleId);
        return results;
    }

    public static Document toDocument(String bundleId, String name, String title, String comments, ArrayList<String> urlArray) {

        Document document = new Document();
        document.append("bundleId", bundleId);
        document.append("date", LocalDateTime.now().toString());
        document.append("name", name);
        document.append("title", title);
        document.append("comments", comments);
        document.append("urls", urlArray);

        System.out.printf("document: %s\n",document.toString());

        return document;
        
    }

}

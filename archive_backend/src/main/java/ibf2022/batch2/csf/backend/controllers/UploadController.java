package ibf2022.batch2.csf.backend.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.batch2.csf.backend.models.Archives;
import ibf2022.batch2.csf.backend.services.UploadService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping()
public class UploadController {

	@Autowired
	private UploadService uploadSvc;

	// TODO: Task 2, Task 3, Task 4
	@PostMapping(path="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upload(@RequestPart MultipartFile file, @RequestPart String name, @RequestPart String title, @RequestPart(required = false) String comments) throws IOException{

        try{
		System.out.println("name: " + name + " title: " + title + " comments: " + comments);
		System.out.println("filename in controller: " + file.getOriginalFilename() + " size: " + file.getSize());
		
		ArrayList<String> results = uploadSvc.uploadtoS3(file, name, title, comments);
        JsonObject bundleIdJson = uploadSvc.uploadtoMongo(name, title, comments, results);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(bundleIdJson.toString());

        }catch(Exception e){
            JsonObject err = Json.createObjectBuilder()
                    .add("error", e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(err.toString());
        }
       
    }

	// TODO: Task 5
	
    @GetMapping(path="/bundle/{bundleId}")
    public ResponseEntity<String> getInfoByBundleId(@PathVariable(required=true) String bundleId){
        try{
        System.out.println("bundleId in controller: " + bundleId);
        Document results = uploadSvc.getInfoByBundleId(bundleId);
        System.out.println("results at controller: " + results);
        
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(results.toJson().toString());

        }
        catch(Exception e){
            JsonObject err = Json.createObjectBuilder()
                    .add("error", e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(err.toString());
        }
        
    }

    public static JsonObject toJson(List<Archives> archive) {
        JsonObjectBuilder jsonObj = Json.createObjectBuilder();
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();


        archive.stream().forEach(
            v -> {
                JsonObjectBuilder j = Json.createObjectBuilder();
                j.add("title", v.getTitle())
                .add("name", v.getName())
                .add("comments", v.getComments())
                .add("bundleId", v.getBundleId())
                .add("date", v.getDate());
                // .add("urls", v.getUrls());
                // .add("reviewUrl", v.getReviewURL())
                // .add("image", v.getImage())
                // .add("commentCount", v.getCommentCount());
                jsonArray.add(j);
            }
        );

        jsonObj.add("jsonArray: ", jsonArray);

        return jsonObj.build();
    }


	// TODO: Task 6

}

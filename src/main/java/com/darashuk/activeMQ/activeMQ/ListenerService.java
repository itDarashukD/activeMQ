package com.darashuk.activeMQ.activeMQ;

import com.darashuk.activeMQ.entity.Song;
import com.darashuk.activeMQ.entity.Source;
import com.darashuk.activeMQ.entity.StorageTypes;
import com.darashuk.activeMQ.service.IWavToMp3ConversationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class ListenerService implements IListenerService {

    @Autowired
    private IWavToMp3ConversationService wavToMp3ConversationService;

    @Value("${HTTP_REQUEST_GET_SONG}")
    private String HTTP_REQUEST_GET_SONG;
    @Value("${HTTP_REQUEST_GET_FILE_IN_ARRAY}")
    private String HTTP_REQUEST_GET_FILE_IN_ARRAY;
    @Value("${HTTP_REQUEST_POST_SAVE_FILE}")
    private String HTTP_REQUEST_POST_SAVE_FILE;

    private byte[] arrayFromWavFile;
    private File mp3File;
    private final RestTemplate restTemplate = new RestTemplate();
    private String nameFromSource;
    private Long song_id;
    @Value("${temporaryDirectory}")
    private String temporaryDirectory;


    public void getFileBySourceFromListener(Source sourceFromListener) {
        StorageTypes storageTypes = sourceFromListener.getStorage_types();
        String fileType = sourceFromListener.getFileType();
        nameFromSource = sourceFromListener.getName();
        song_id = sourceFromListener.getSong_id();
        arrayFromWavFile = restTemplate.getForObject(HTTP_REQUEST_GET_FILE_IN_ARRAY
                , byte[].class
                , nameFromSource
                , fileType
                , storageTypes);
        writingArrayToFile();
    }

    public void writingArrayToFile() {
        try {
//           String tempDir = System.getProperty("java.io.tmpdir");  //this for host application
//            String temporaryDirectory = "./";                                 //this for host application, but don't tested
//            String temporaryDirectory = "/data/app_active_mq_converter/";  //this for docker_application

            File file = new File(temporaryDirectory + nameFromSource);
            FileUtils.writeByteArrayToFile(new File(file.getAbsolutePath()), arrayFromWavFile);
            log.info(" IN writingArrayToFile() : " + file.getName() + " " + file.getAbsolutePath() + " " + file.length() + " " + file.getPath());
            mp3File = wavToMp3ConversationService.executeConvetion(file);
            log.info(" IN writingArrayToFile() : " + mp3File.getName() + " " + mp3File.getAbsolutePath() + " " + mp3File.length() + " " + mp3File.getPath());
            saveMp3FileToStorages();
        } catch (IOException e) {
            log.error("EXCEPTION IN writingArrayToFile() : " + e.getMessage());
        }
    }

    private void saveMp3FileToStorages() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        MultipartFile multipartFile = convertFileToMultipartFile();
        Song songDataToSaveInStorage = getDataSongBySource();
        log.info("IN saveMp3FileToStorages() : songDataToSaveInStorage " + songDataToSaveInStorage.toString());

        Long albumId = songDataToSaveInStorage.getAlbum_id();
        Integer songYear = songDataToSaveInStorage.getYear();
        String songNotes = songDataToSaveInStorage.getNotes();
        Resource multipartFileResource = multipartFile.getResource();
        log.info("IN saveMp3FileToStorages() : multipartFileResource " + multipartFileResource.getDescription());

        LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", multipartFileResource);
        parts.add("albumId", albumId);
        parts.add("songName", multipartFile.getName());
        parts.add("songNotes", songNotes);
        parts.add("songYear", songYear);

        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parts, httpHeaders);
        final ResponseEntity<String> responseEntity = restTemplate.postForEntity(HTTP_REQUEST_POST_SAVE_FILE, httpEntity, String.class);
        log.info("IN saveMp3FileToStorages() : request implemented " + responseEntity.getStatusCode());
    }

    private Song getDataSongBySource() {
        return restTemplate.getForObject(HTTP_REQUEST_GET_SONG, Song.class, song_id);
    }

    private MultipartFile convertFileToMultipartFile() {
        Path path = Paths.get(mp3File.getAbsolutePath());
        log.info("IN convertFileToMultipartFile() : " + "path : " + path.getFileName() + " " + path);
        String name = mp3File.getName();
        String originalFileName = mp3File.getName();
        String contentType = "audio/mpeg";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
            log.info("IN convertFileToMultipartFile() : " + "content lenght : " + content.length);

        } catch (IOException e) {
            log.error("IN convertFileToMultipartFile() :" + e.getMessage());
        }
        return new MockMultipartFile(name,
                originalFileName, contentType, content);
    }
}

package com.darashuk.activeMQ.service;


import it.sauronsoftware.jave.*;
import it.sauronsoftware.jave.EncoderProgressListener;
import it.sauronsoftware.jave.audio.AudioAttributes;
import it.sauronsoftware.jave.video.VideoAttributes;
import it.sauronsoftware.jave.video.VideoSize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class WavToMp3ConversationService implements IWavToMp3ConversationService {

    @Value("${temporaryDirectory}")
    private String temporaryDirectory;

    public File executeConvetion(File fileToConvert) {
        log.info(" IN executeConvetion() : " + "fileToConvert : " + fileToConvert.getName() + " " + fileToConvert.getPath() + " " + fileToConvert.length());
        String newFilename = fileToConvert.getName().replace(".wav", ".mp3");
        log.info(" IN executeConvetion() : " + "newFilename : " + newFilename);

        File readyMp3File = new File(temporaryDirectory + newFilename);
        readyMp3File.getParentFile().mkdirs();
        log.info(" IN executeConvetion() : " + "readyMp3File : " + readyMp3File.getName() + " " + readyMp3File.length() + " " + readyMp3File.getAbsolutePath());

        try {
            List<String> ffmpeg = new ArrayList<>();
            ffmpeg.add("-i");
            ffmpeg.add(fileToConvert.getAbsolutePath());
            ffmpeg.add("-vn");
            ffmpeg.add("-ab");
            ffmpeg.add("128000");
            ffmpeg.add("-ac");
            ffmpeg.add("2");
            ffmpeg.add("-ar");
            ffmpeg.add("44100");
            ffmpeg.add("-f");
            ffmpeg.add("mp3");
            ffmpeg.add("-y");
            ffmpeg.add(readyMp3File.getAbsolutePath());

            final MpegExec mpegExec = new MpegExec();
            final String ffmpegExecutablePath = mpegExec.getFFMPEGExecutablePath();

            execute(ffmpeg, ffmpegExecutablePath).waitFor();
        } catch (IllegalArgumentException e) {
            log.error("EXCEPTION IN executeConvetion() : " + e.getMessage());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        log.info(" IN executeConvetion() : " + "readyMp3File : " + readyMp3File.getName() + " " + readyMp3File.getPath() + " " + readyMp3File.length());
        return readyMp3File;
    }

    public Process execute(List<String> args, String path) throws IOException, InterruptedException {
        int argsSize = args.size();
        String[] cmd = new String[argsSize + 1];
        cmd[0] = path;
        for (int i = 0; i < argsSize; i++) {
            cmd[i + 1] = (String) args.get(i);
        }
        Runtime runtime = Runtime.getRuntime();
        log.info("exec cmd: {}", Arrays.toString(cmd));
        Process ffmpeg = runtime.exec(cmd);
        runtime.addShutdownHook(new Thread(() -> ffmpeg.destroy()));
        return ffmpeg;

    }

    public static class MpegExec extends DefaultFFMPEGLocator {
        @Override
        public String getFFMPEGExecutablePath() {
            return super.getFFMPEGExecutablePath();
        }
    }
}


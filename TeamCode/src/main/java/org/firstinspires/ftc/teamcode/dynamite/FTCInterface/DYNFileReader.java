package org.firstinspires.ftc.teamcode.dynamite.FTCInterface;

import android.content.Context;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;

import com.qualcomm.robotcore.hardware.HardwareMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class DYNFileReader {
    private final boolean isOnUSB;
    private final HardwareMap hardwareMap;
    public DYNFileReader(HardwareMap HWM) {
        this(false,HWM);
    }

    public DYNFileReader(boolean isOnUSB, HardwareMap HWM){
        this.isOnUSB = isOnUSB;
        this.hardwareMap = HWM;
    }

    public String readFile(String name){
        if (isOnUSB){
            try {
                return readFromUSB(name);
            } catch (RuntimeException e){
                // fallback to asset loading if USB fails
                try{
                    return readFromAssets(name);
                } catch (RuntimeException ex) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            return readFromAssets(name);
        }
    }

    private void log(String message) {
        System.out.println("[FileReader] " + message);
    }
    // written by Claude Sonnet 5
    private String readFromUSB(String name){
        StorageManager sm = (StorageManager) hardwareMap.appContext.getSystemService(Context.STORAGE_SERVICE);
        List<StorageVolume> volumes = sm.getStorageVolumes();
        log("found " + volumes.size() + " storage volume(s) via StorageManager");
        for (StorageVolume vol : volumes) {
            log("volume: removable=" + vol.isRemovable()
                    + " state=" + vol.getState()
                    + " desc=" + vol.getDescription(hardwareMap.appContext));
        }

        File storageRoot = new File("/storage");
        File[] entries = storageRoot.listFiles();
        if (entries == null) {
            log("could not list /storage directory");
            throw new RuntimeException(new IOException("could not list /storage"));
        }
        log("found " + entries.length + " entry(ies) under /storage");

        for (File entry : entries) {
            String entryName = entry.getName();
            if (entryName.equals("emulated") || entryName.equals("self")) {
                log("skipping " + entryName);
                continue;
            }
            if (!entry.isDirectory()) {
                log("skipping non-directory entry " + entryName);
                continue;
            }
            log("checking mount point " + entry.getAbsolutePath());

            File target = new File(entry, name);
            if (target.isFile()) {
                log("found \"" + name + "\" (" + target.length() + " bytes)");
                try {
                    StringBuilder content = new StringBuilder();
                    InputStream is = new FileInputStream(target);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    int lines = 0;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                        lines++;
                    }
                    reader.close();
                    log("read " + lines + " line(s) from \"" + name + "\"");
                    return content.toString();
                } catch (IOException e) {
                    log("read failed - " + e.getMessage());
                    throw new RuntimeException(e);
                }
            } else {
                log("\"" + name + "\" not present at " + entry.getAbsolutePath());
            }
        }
        log("read failed - Script \"" + name + "\" not found under /storage");
        throw new RuntimeException(new IOException("Script \"" + name + "\" not found on any mounted USB volume"));
    }
    private String readFromAssets(String name){
        try {
            StringBuilder content = new StringBuilder();
            InputStream is = hardwareMap.appContext.getAssets().open(name);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            return content.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
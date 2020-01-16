package com.kuruvatech.pipelinerecorder.utils;

/**
 * Created by dganeshappa on 7/5/2016.
 */

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context){

        //Find the dir at SDCARD to save cached images

        if (true)
        {
            //if SDCARD is mounted (SDCARD is present on device and mounted)
//            cacheDir = new File(
//                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath(),"LazyList");
          //  Environment.getExternalStoragePublicDirectory
            //Find the dir to save cached images
            String directory = "LazyList";
            String path = Environment.getDataDirectory().getAbsolutePath().toString()+ "/YourDirectoryName";
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
                cacheDir=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath(),"LazyList");
            else
            {
                cacheDir = context.getDir(directory, Context.MODE_PRIVATE); //Creating an internal directry
               // cacheDir = new File(path);
//                if(!mydir.exists())           //check if not created then create the firectory
//                    mydir.mkdirs();
            }
//                cacheDir=context.getFilesDir();
            if(!cacheDir.exists())
                cacheDir.mkdirs();

        }
//        else
//        {
//            // if checking on simulator the create cache dir in your application context
//            cacheDir=context.getCacheDir();
//        }

        if(!cacheDir.exists()){
            // create cache dir in your application context
            cacheDir.mkdirs();
        }
    }

    public File getFile(String url){
        //Identify images by hashcode or encode by URLEncoder.encode.
        String fileName2 = url.substring(url.lastIndexOf('/') + 1);
        String filename= String.valueOf(fileName2);

        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear(){
        // list all files inside cache directory
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        //delete all cache directory files
        for(File f:files)
            f.delete();
    }

}

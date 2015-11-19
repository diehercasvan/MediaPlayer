package classMediaPlayer;


import android.content.Intent;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by DIEGO CASALLAS on 17/11/2015.
 */
public class Files {

    //Create  variable
    private String[] aExtensions;
    private LinkedList<String> aListFiles;
    private LinkedList<String> aListDirectory;
    private String sExt;
    private File[] afDirectory;
    private File[] afDirectoryAux;
    private File fFiles;
    private File fDirectory;
    private String sRoute;
    private static final int FILE_SELECT_CODE = 0;
    public Files() {

        this.sRoute = General.ROUTE;
        this.aExtensions = General.EXTENSIONS;
    }

    public LinkedList<String> searchDirectory(int iTityExtension) {

        try {
            fDirectory = new File(sRoute);
            aListDirectory = new LinkedList<>();
            afDirectory = fDirectory.listFiles();
            for (int i = 0; i < afDirectory.length; i++) {
                if (afDirectory[i].isDirectory()) {


                    //if (searchFiles(afDirectory[i].getName(), iTityExtension)) {

                        aListDirectory.add(afDirectory[i].getName());
                    //}
                }

            }


        } catch (Exception e) {
            General.printToast("Error in  search files type error:" + e.getMessage());

        }
        return aListDirectory;
    }

    public boolean searchFiles(String sNameDirectory, int iExtension) {

        try {
            fFiles = new File(sRoute, sNameDirectory);
            sExt = "." + aExtensions[iExtension];

            afDirectoryAux = fFiles.listFiles();
            for (int i = 0; i < afDirectoryAux.length; i++) {

                if (afDirectoryAux[i].isDirectory()) {
                    String[] NameFile = afDirectoryAux[i].getName().split("\\.");
                    String data = afDirectoryAux[i].getName();

                    if (NameFile[0].equals(sExt)) {

                     return true;
                    }else{

                        String ddd = afDirectoryAux[i].getName();
                    }

                }
            }


        } catch (Exception e) {

            General.printToast("Error in  search files type error:" + e.getMessage());
        }
        return false;

    }

    public  void  showFileChooser(){
        Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            General.ACTIVITY.startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog

                    General.printToast("Please install a File Manager:" + ex.getMessage());

        }


    }


}

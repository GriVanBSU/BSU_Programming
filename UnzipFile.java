import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
public class UnzipFile {
    public static void unzip(String zipFilePath, String destDirPath){
        File destDir = new File(destDirPath);
        if(!destDir.exists()){
            destDir.mkdirs();
        }

        ZipInputStream zipIn = null;

        try{
            zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            while(entry != null){
                String filePath = destDirPath + File.separator + entry.getName();
                if(!entry.isDirectory()){
                    extractFile(zipIn, filePath);
                }
                else{
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }

        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (zipIn != null) {
                    zipIn.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException{
     FileOutputStream fos = new FileOutputStream(filePath);
     byte[] bytes = new byte[1024];
     int length;
     while((length = zipIn.read(bytes)) > 0){
         fos.write(bytes, 0, length);
     }
     fos.close();
    }
}
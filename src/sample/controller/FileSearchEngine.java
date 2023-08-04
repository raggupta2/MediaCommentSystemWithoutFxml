package sample.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileSearchEngine {
    private String[] excludeDirs;

    public FileSearchEngine() {
        excludeDirs = new String[]{"Program Files", "Program Files (x86)", "Windows", ".git", ".idea", "Program Data"};
    }

    /**
     * A method to search file with same filename and checksum.
     *
     * @param filename : A file name to be searched.
     * @param checksum : A checksum of file to be searched.
     * @return
     */
    public String searchFile(String filename, String checksum) {
        String ret = "";
        List<String> drivers = getDriver();
        for (String driver : drivers) {
            ret = searchFileWithExclude(driver, filename, checksum);
            if (!ret.isEmpty()) return ret;
        }
        return ret;
    }

    /**
     * A method to search file with filename and checksum from root path.
     *
     * @param rootPath : A driver path.
     * @param filename : A filename to be searched.
     * @param checksum : A checksum of file to be searched.
     * @return String : A file path searched. if there is no, return empty string.
     */
    private String searchFileWithExclude(String rootPath, String filename, String checksum) {
        String ret = "";
        // for exlude file extension
        //    NotFileFilter suffixFileFilterFileFilter=new NotFileFilter(new
        //    SuffixFileFilter(new String[] { "jpg" }));

        //to include only selected extension use below one
        IOFileFilter suffixFileFilterFileFilter = new SuffixFileFilter(new String[]{filename});

        NotFileFilter directoryFileFilterFileFilter = new NotFileFilter(new PrefixFileFilter(excludeDirs));   // for directory
        File dir = new File(rootPath);
        List<File> fileList = (List<File>) FileUtils.listFiles(dir, suffixFileFilterFileFilter, directoryFileFilterFileFilter);
        for (File file : fileList) {
            try {
                boolean isValid = verifyFileWithNameAndChecksum(file, filename, checksum);
                if (isValid)
                    ret = file.getCanonicalPath();
                System.out.println("file with same name: " + file.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
                return ret;
            }
        }
        return ret;
    }

    private boolean verifyFileWithNameAndChecksum(File file, String searchingName, String checksum) throws IOException {
        String inputName = file.getName();
        if (inputName.contentEquals(searchingName)) {
            return ChecksumUtil.verifyChecksum(file.getCanonicalPath(), checksum);
        }
        return false;
    }

    private List<String> getDriver() {
        List<String> driverList = new ArrayList<>();
        File[] fs = File.listRoots();
        for (int i = 0; i < fs.length; i++) {
            driverList.add(fs[i].getAbsolutePath());
        }
        return driverList;
    }
}

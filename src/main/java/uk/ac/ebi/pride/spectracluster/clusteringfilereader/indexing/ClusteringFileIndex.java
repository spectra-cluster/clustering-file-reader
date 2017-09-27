package uk.ac.ebi.pride.spectracluster.clusteringfilereader.indexing;

import java.io.*;
import java.util.Map;

/**
 * Represents the index to a .clustering file.
 * Created by jg on 27.09.17.
 */
public class ClusteringFileIndex {
    private Map<String, ClusteringIndexElement> index;

    public ClusteringFileIndex(Map<String, ClusteringIndexElement> index) {
        this.index = index;
    }

    /**
     * Loads a .clustering file's index from a binary index file.
     * @param indexFile The binary index file
     * @return A ClusteringFileIndex object
     * @throws Exception
     */
    public static ClusteringFileIndex loadFromFile(File indexFile) throws Exception {
        FileInputStream inputStream = new FileInputStream(indexFile);
        ObjectInput objectInput = new ObjectInputStream(inputStream);

        Map<String, ClusteringIndexElement> index = (Map<String, ClusteringIndexElement>) objectInput.readObject();

        return new ClusteringFileIndex(index);
    }

    /**
     * Save the current index object to a binary file.
     * @param targetFile The file to save the index to. This file will
     *                   be overwritten if it exists.
     * @throws Exception Thrown if file access errors occur.
     */
    public void saveToFile(File targetFile) throws Exception {
        FileOutputStream outputStream = new FileOutputStream(targetFile);
        ObjectOutput objectOutput = new ObjectOutputStream(outputStream);

        objectOutput.writeObject(this.index);

        objectOutput.close();
    }

    public Map<String, ClusteringIndexElement> getIndex() {
        return index;
    }
}

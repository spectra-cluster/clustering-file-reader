package uk.ac.ebi.pride.spectracluster.clusteringfilereader.indexing;

import uk.ac.ebi.pride.tools.braf.BufferedRandomAccessFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Indexer to index .clustering files.
 *
 * Created by jg on 27.09.17.
 */
public class ClusteringFileIndexer implements IIndexer {
    @Override
    public ClusteringFileIndex indexFile(File resultFile) throws Exception {
        // open the file
        BufferedRandomAccessFile randomAccessFile = new BufferedRandomAccessFile(resultFile, "r", 1024 * 100);

        Map<String, ClusteringIndexElement> clusterIndexes = new HashMap<String, ClusteringIndexElement>();

        // process the file line by line
        String line;
        long currentStart = 0;
        // the end position of the last line = the starting position of the current line
        long lastLineEnd = 0;
        float precursorMz = 0;
        boolean inHeader = true;
        String currentId = null;
        int currentIndex = 0;


        while ((line = randomAccessFile.readLine()) != null) {
            if (Thread.currentThread().isInterrupted()) {
                randomAccessFile.close();
                throw new InterruptedException();
            }

            // ignore all header fields
            if (line.startsWith("=Cluster=")) {
                // if the header has already been processed before, process the last cluster
                if (!inHeader) {
                    if (currentId == null) {
                        currentId = String.valueOf(currentIndex);
                    }

                    ClusteringIndexElement clusteringIndexElement = new ClusteringIndexElement(currentId, precursorMz,
                            currentStart,
                            (int) (lastLineEnd - currentStart));
                    clusterIndexes.put(currentId, clusteringIndexElement);

                    // to detect any errors
                    precursorMz = 0;
                    currentId = null;
                    currentIndex++;
                }
                else {
                    inHeader = false;
                }
                currentStart = lastLineEnd;
            }

            lastLineEnd = randomAccessFile.getFilePointer();

            if (inHeader) {
                continue;
            }

            // process fields of interest
            if (line.startsWith("av_precursor_mz=")) {
                int index = line.indexOf("=");
                String value = line.substring(index + 1);
                String[] fields = value.split("\\s+");

                precursorMz = Float.parseFloat(fields[0]);
            }
            else if (line.startsWith("id=")) {
                int index = line.indexOf("=");
                String value = line.substring(index + 1);
                currentId = value.trim();
            }
        }

        if (precursorMz != 0) {
            if (currentId == null) {
                currentId = String.valueOf(currentIndex);
            }

            ClusteringIndexElement clusteringIndexElement = new ClusteringIndexElement(currentId, precursorMz,
                    currentStart,
                    (int) (lastLineEnd - currentStart));
            clusterIndexes.put(currentId, clusteringIndexElement);
        }

        randomAccessFile.close();

        return new ClusteringFileIndex(clusterIndexes);
    }
}

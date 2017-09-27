package uk.ac.ebi.pride.spectracluster.clusteringfilereader.indexing;

import java.io.File;
import java.util.Map;

/**
 * Interface representing classes to index clustering result files.
 * Created by jg on 27.09.17.
 */
public interface IIndexer {
    /**
     * Index the defined result file and returns a map containing the cluster's ids as keys and the
     * ClusteringIndexElementS representing these clusters as values.
     *
     * @param resultFile File to index.
     * @return A Map with the cluster id as key and the ClusteringIndexElement as value.
     */
    public Map<String, ClusteringIndexElement> indexFile(File resultFile) throws Exception;
}

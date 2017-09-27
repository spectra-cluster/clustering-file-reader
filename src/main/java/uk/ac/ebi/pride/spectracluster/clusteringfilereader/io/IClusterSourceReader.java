package uk.ac.ebi.pride.spectracluster.clusteringfilereader.io;

import uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects.ICluster;

import java.util.Collection;
import java.util.List;

/**
 * Created by jg on 10.07.14.
 */
public interface IClusterSourceReader {
    /**
     * This function reads all clusters from the clustering source. Spectra
     * are never includeded in this output.
     * @return
     * @throws Exception
     */
    public List<ICluster> readAllClusters() throws Exception;

    public boolean supportsReadAllClusters();

    /**
     * This function includes spectra if they are available.
     * @param listeners
     * @throws Exception
     */
    public void readClustersIteratively(Collection<IClusterSourceListener> listeners) throws Exception;

    /**
     * Read a specific cluster.
     * @param id The cluster's id
     * @return The cluster.
     */
    public ICluster readCluster(String id) throws Exception;
}

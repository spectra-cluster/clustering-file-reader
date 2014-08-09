package uk.ac.ebi.pride.spectracluster.clusteringfilereader.io;

import uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects.ICluster;

import java.util.Collection;
import java.util.List;

/**
 * Created by jg on 10.07.14.
 */
public interface IClusterSourceReader {
    public List<ICluster> readAllClusters() throws Exception;

    public boolean supportsReadAllClusters();

    public void readClustersIteratively(Collection<IClusterSourceListener> listeners) throws Exception;
}

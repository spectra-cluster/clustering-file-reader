package uk.ac.ebi.pride.spectracluster.clusteringfilereader;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.io.ClusteringFileReader;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.io.IClusterSourceListener;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.io.IClusterSourceReader;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects.ICluster;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by jg on 01.08.14.
 */
public class ClusteringFileReaderTest {
    public File testFile;

    @Before
    public void setUp() throws URISyntaxException {
        URI testFileUri = ClusteringFileReaderTest.class.getClassLoader().getResource("testfile.clustering").toURI();
        testFile = new File(testFileUri);
    }

    @Test
    public void testReadClustersIteratively() throws Exception {
        IClusterSourceReader reader = new ClusteringFileReader(testFile);

        List<IClusterSourceListener> listeners = new ArrayList<IClusterSourceListener>();
        reader.readClustersIteratively(listeners);
    }

    @Test
    public void testReadAllClusters() throws Exception {
        IClusterSourceReader reader = new ClusteringFileReader(testFile);

        List<ICluster> clusters = reader.readAllClusters();

        Assert.assertEquals(960, clusters.size());

        ICluster cluster = clusters.get(6);

        Assert.assertEquals(305.0F, cluster.getAvPrecursorMz());
        Assert.assertEquals(2, cluster.getSpecCount());

        Assert.assertEquals("KGSCR", cluster.getSpectrumReferences().get(0).getSequence());
        Assert.assertEquals("PXD000090;PRIDE_Exp_Complete_Ac_27993.xml;spectrum=2338", cluster.getSpectrumReferences().get(0).getSpectrumId());
        Assert.assertEquals(304.61032F, cluster.getSpectrumReferences().get(0).getPrecursorMz());
        Assert.assertEquals(0, cluster.getSpectrumReferences().get(0).getCharge());
    }
}

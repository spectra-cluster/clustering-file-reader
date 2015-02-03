package uk.ac.ebi.pride.spectracluster.clusteringfilereader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.io.ClusteringFileReader;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects.ICluster;

import java.io.File;
import java.net.URI;
import java.util.List;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class MaxRatioTest {
    private File testFile;

    @Before
    public void setUp() throws Exception {
        URI testFileUri = ClusteringFileReaderTest.class.getClassLoader().getResource("max_ratio.clustering").toURI();
        testFile = new File(testFileUri);
    }

    @Test
    public void testMaxRatioIsCorrect() throws Exception {
        ClusteringFileReader reader = new ClusteringFileReader(testFile);
        List<ICluster> clusters = reader.readAllClusters();
        ICluster cluster = clusters.get(0);
        Assert.assertEquals(1.00, cluster.getMaxRatio(), 0.001);

    }
}

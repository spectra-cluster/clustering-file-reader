package uk.ac.ebi.pride.spectracluster.clusteringfilereader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.io.ClusteringFileReader;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects.ICluster;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects.ISpectrumReference;

import java.io.File;
import java.util.List;

/**
 * Created by jg on 30.07.15.
 */
public class UnidentifiedSequencesTest {
    private File testFile;
    private static List<ICluster> testClusters;

    @Before
    public void setUp() throws Exception {
        testFile = new File(UnidentifiedSequencesTest.class.getClassLoader().getResource("unidentified.clustering").toURI());
        if (testClusters == null) {
            ClusteringFileReader reader = new ClusteringFileReader(testFile);
            testClusters = reader.readAllClusters();
        }
    }

    @Test
    public void testMixedCluster() throws Exception {
        ICluster testCluster = testClusters.get(1);

        // make sure it's the correct cluster
        Assert.assertEquals("ccd02911-81b7-4583-9101-58a88cab1f04", testCluster.getId());

        // code was adapted to prevent duplicate PSMs to be stored
        Assert.assertEquals(1, testCluster.getSpectrumReferences().get(0).getPSMs().size());

        // check whether identification details are found correctly
        Assert.assertTrue(testCluster.getSpectrumReferences().get(1).isIdentified());
        Assert.assertFalse(testCluster.getSpectrumReferences().get(2).isIdentified());

        Assert.assertFalse(testCluster.getSpectrumReferences().get(1).isIdentifiedAsMultiplePeptides());

        Assert.assertEquals(81, testCluster.getSpecCount());
        Assert.assertEquals(54, testCluster.getPsmCount());
    }

    @Test
    public void testUnidentifiedCluster() throws Exception {
        ICluster testCluster = testClusters.get(2);

        Assert.assertEquals("61326dc1-6a3a-49b7-b668-5edd0dae5136", testCluster.getId());

        Assert.assertEquals(0, testCluster.getPsmCount());
        Assert.assertEquals(5, testCluster.getSpecCount());

        Assert.assertEquals(0, testCluster.getIdentifiedSpecCount());
        Assert.assertEquals(5, testCluster.getUnidentifiedSpecCount());

        for (ISpectrumReference specRef : testCluster.getSpectrumReferences()) {
            Assert.assertFalse(specRef.isIdentified());
        }
    }

    @Test
    public void testSmallMixedCluster() throws Exception {
        ICluster testCluster = testClusters.get(3);

        Assert.assertEquals("66c1ebcb-dffc-43b6-a862-3807a7d9847d", testCluster.getId());

        Assert.assertEquals(2, testCluster.getIdentifiedSpecCount());
        Assert.assertEquals(2, testCluster.getUnidentifiedSpecCount());

        Assert.assertEquals(0.5, testCluster.getMaxRatio(), 0.00000001);
    }

    @Test
    public void testIdentifiedCluster() throws Exception {
        ICluster testCluster = testClusters.get(4);

        Assert.assertEquals("268360b6-92d5-4976-8a15-a1b1456188aa", testCluster.getId());

        Assert.assertEquals(3, testCluster.getIdentifiedSpecCount());
        Assert.assertEquals(0, testCluster.getUnidentifiedSpecCount());

        for (ISpectrumReference specRef : testCluster.getSpectrumReferences()) {
            Assert.assertTrue(specRef.isIdentified());
            Assert.assertEquals(1, specRef.getPSMs().size());
        }
    }

    @Test
    public void testLargeUnidentifiedCluster() throws Exception {
        ICluster testCluster = testClusters.get(5);
        Assert.assertEquals("45fa0c9d-a5c0-48a9-b6c3-f596b7c65da6", testCluster.getId());

        Assert.assertEquals(0, testCluster.getIdentifiedSpecCount());
        Assert.assertEquals(testCluster.getSpecCount(), testCluster.getUnidentifiedSpecCount());

    }


}

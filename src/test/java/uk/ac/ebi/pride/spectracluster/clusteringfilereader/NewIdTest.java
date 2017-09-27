package uk.ac.ebi.pride.spectracluster.clusteringfilereader;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.io.ClusteringFileReader;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects.ICluster;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by jg on 25.05.16.
 */
public class NewIdTest {
    public File testFile;

    @Before
    public void setUp() throws URISyntaxException {
        URI testFileUri = ClusteringFileReaderTest.class.getClassLoader().getResource("testfile-file_id.clustering").toURI();
        testFile = new File(testFileUri);
    }

    @Test
    public void testNewIds() throws Exception {
        ClusteringFileReader reader = new ClusteringFileReader(testFile);
        List<ICluster> clusters = reader.readAllClusters();

        ICluster cluster1 = clusters.get(3);

        Assert.assertEquals(2, cluster1.getSpecCount());

        Assert.assertEquals("index=120", cluster1.getSpectrumReferences().get(0).getSourceId());
        Assert.assertEquals("test1.mgf", cluster1.getSpectrumReferences().get(0).getSourceFilename());
        Assert.assertEquals("PRD000493;PRIDE_Exp_Complete_Ac_18184.xml;spectrum=138243",
                cluster1.getSpectrumReferences().get(0).getOriginalSpectrumTitle());

        Assert.assertNull(cluster1.getSpectrumReferences().get(1).getSourceId());
        Assert.assertNull(cluster1.getSpectrumReferences().get(1).getSourceFilename());
        Assert.assertEquals("PRD000493;PRIDE_Exp_Complete_Ac_18184.xml;spectrum=138248", cluster1.getSpectrumReferences().get(1).getOriginalSpectrumTitle());
    }

}

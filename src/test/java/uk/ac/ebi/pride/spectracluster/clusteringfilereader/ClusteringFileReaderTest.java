package uk.ac.ebi.pride.spectracluster.clusteringfilereader;

import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.io.ClusteringFileReader;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.io.IClusterSourceListener;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.io.IClusterSourceReader;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects.ICluster;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects.ISpectrumReference;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by jg on 01.08.14.
 */
public class ClusteringFileReaderTest {
    public File testFile;
    public File completeFile;

    @Before
    public void setUp() throws URISyntaxException {
        URI testFileUri = ClusteringFileReaderTest.class.getClassLoader().getResource("testfile.clustering").toURI();
        URI completeSpectrumInfoFileUri = ClusteringFileReaderTest.class.getClassLoader().getResource("complete_spectrum_info.clustering").toURI();
        testFile = new File(testFileUri);
        completeFile = new File(completeSpectrumInfoFileUri);
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

        assertEquals(960, clusters.size());

        ICluster cluster = clusters.get(6);

        assertEquals(305.0F, cluster.getAvPrecursorMz());
        assertEquals(2, cluster.getSpecCount());

        assertEquals("KGSCR", cluster.getSpectrumReferences().get(0).getSequence());
        assertEquals("PXD000090;PRIDE_Exp_Complete_Ac_27993.xml;spectrum=2338", cluster.getSpectrumReferences().get(0).getSpectrumId());
        assertEquals(304.61032F, cluster.getSpectrumReferences().get(0).getPrecursorMz());
        assertEquals(0, cluster.getSpectrumReferences().get(0).getCharge());
    }


    @Test
    public void testReadCompleteSpectrumInformation() throws Exception {
        IClusterSourceReader reader = new ClusteringFileReader(completeFile);

        List<ICluster> clusters = reader.readAllClusters();

        assertEquals(1, clusters.size());

        ICluster cluster = clusters.get(0);
        List<ISpectrumReference> spectrumReferences = cluster.getSpectrumReferences();
        assertEquals(1, spectrumReferences.size());

        ISpectrumReference spectrum = spectrumReferences.get(0);
        assertEquals("PRD000715;PRIDE_Exp_Complete_Ac_24805.xml;spectrum=11", spectrum.getSpectrumId());

        assertEquals("TSLAGGGR", spectrum.getSequence());
        assertEquals(399.68015F, spectrum.getPrecursorMz());
        assertEquals(2, spectrum.getCharge());
        assertEquals("9606", spectrum.getSpecies());
        assertEquals("1-MOD:01455", spectrum.getModifications());
        assertEquals(1.0f, spectrum.getSimilarityScore());

    }
}

package uk.ac.ebi.pride.spectracluster.clusteringfilereader;

import junit.framework.Assert;
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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;

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

        assertEquals("PXD000090;PRIDE_Exp_Complete_Ac_27993.xml;spectrum=2338", cluster.getSpectrumReferences().get(0).getSpectrumId());
        assertEquals(304.61032F, cluster.getSpectrumReferences().get(0).getPrecursorMz());
        assertEquals(0, cluster.getSpectrumReferences().get(0).getCharge());

        ISpectrumReference ref = cluster.getSpectrumReferences().get(0);
        assertEquals(1, ref.getPSMs().size());
        assertFalse(ref.isIdentifiedAsMultiplePeptides());
        assertEquals("KGSCR", ref.getMostCommonPSM().getSequence());
        assertEquals(0, ref.getMostCommonPSM().getModifications().size());
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

        assertEquals(399.68015F, spectrum.getPrecursorMz());
        assertEquals(2, spectrum.getCharge());
        assertEquals("9606", spectrum.getSpecies());
        assertEquals(1.0f, spectrum.getSimilarityScore());

        assertEquals(1, spectrum.getPSMs().size());
        assertFalse(spectrum.isIdentifiedAsMultiplePeptides());
        assertEquals("TSLAGGGR", spectrum.getMostCommonPSM().getSequence());
        assertEquals(1, spectrum.getMostCommonPSM().getModifications().size());
        assertEquals(1, spectrum.getMostCommonPSM().getModifications().get(0).getPosition());
        assertEquals("MOD:01455", spectrum.getMostCommonPSM().getModifications().get(0).getAccession());
    }

    @Test
    public void testReadConsensusSpectrum() throws Exception {
        IClusterSourceReader reader = new ClusteringFileReader(completeFile);

        List<ICluster> clusters = reader.readAllClusters();

        // mz and intens values are the same, count values are missing
        for (ICluster cluster : clusters) {
            int nMzValues = cluster.getConsensusMzValues().size();
            int nIntensValues = cluster.getConsensusIntensValues().size();
            int nCountsValues = cluster.getConsensusCountValues().size();

            Assert.assertTrue(nMzValues > 0);
            Assert.assertTrue(nMzValues == nIntensValues);
            Assert.assertEquals(0, nCountsValues);
        }
    }
}

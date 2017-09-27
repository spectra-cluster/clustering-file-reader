package uk.ac.ebi.pride.spectracluster.clusteringfilereader.io;

import uk.ac.ebi.pride.spectracluster.clusteringfilereader.indexing.ClusteringIndexElement;
import uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects.*;
import uk.ac.ebi.pride.tools.braf.BufferedRandomAccessFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by jg on 10.07.14.
 */
public class ClusteringFileReader implements IClusterSourceReader {
    private final File clusteringFile;
    private BufferedReader br;
    private boolean inCluster = false;
    private Map<String, ClusteringIndexElement> index;

    public ClusteringFileReader(File clusteringFile) {
        this.clusteringFile = clusteringFile;
    }

    public ClusteringFileReader(File clusteringFile, Map<String, ClusteringIndexElement> fileIndex) {
        this.clusteringFile = clusteringFile;
        this.index = fileIndex;
    }

    @Override
    public List<ICluster> readAllClusters() throws Exception {
        // always reopen the file
        if (br != null) {
            br.close();
            inCluster = false;
        }

        br = openClusteringFile(clusteringFile);

        List<ICluster> clusters = new ArrayList<ICluster>();
        ICluster cluster;

        while ((cluster = readNextCluster(br, false)) != null)
            clusters.add(cluster);

        br.close();
        br = null;
        inCluster = false;

        return clusters;
    }

    @Override
    public boolean supportsReadAllClusters() {
        return true;
    }

    @Override
    public void readClustersIteratively(Collection<IClusterSourceListener> listeners) throws Exception {
        if (br == null) {
            br = openClusteringFile(clusteringFile);
            inCluster = false;
        }

        ICluster cluster;

        while ((cluster = readNextCluster(br, true)) != null) {
            for (IClusterSourceListener listener : listeners)
                listener.onNewClusterRead(cluster);
        }
    }

    /**
     * Reads the specified cluster from the file. This is only supported
     * if the ClusteringFileReader was created with an index element.
     * @param id The cluster's id
     * @return The cluster object
     * @throws Exception
     */
    @Override
    public ICluster readCluster(String id) throws Exception {
        if (index == null) {
            throw new Exception("Clusters can only be read by id if the ClusteringFileReader is" +
                    " constructed with a file index.");
        }
        if (!index.containsKey(id)) {
            throw new Exception("Cluster " + id + " could not be found in the index");
        }

        // read the cluster string
        ClusteringIndexElement indexElement = index.get(id);
        byte[] byteBuffer = new byte[indexElement.getSize()];
        BufferedRandomAccessFile randomAccessFile = new BufferedRandomAccessFile(clusteringFile,
                "r", 1024 * 100);
        randomAccessFile.seek(indexElement.getStart());
        randomAccessFile.read(byteBuffer);
        randomAccessFile.close();
        String clusteringString = new String(byteBuffer);

        return readNextCluster(new BufferedReader(new StringReader(clusteringString)), true);
    }

    /**
     * Open the clustering file for reading.
     * @param file
     * @return
     */
    private BufferedReader openClusteringFile(File file) throws IOException {
        if (file.getName().endsWith(".gz")) {
            GZIPInputStream inputStream = new GZIPInputStream(new FileInputStream(file));
            return new BufferedReader(new InputStreamReader(inputStream));
        }
        else {
            return new BufferedReader(new FileReader(file));
        }
    }

    private ICluster readNextCluster(final BufferedReader br, boolean includeSpectra) throws Exception {
        String line;

        float avPrecursorMz = 0, avPrecursorIntens = 0;
        String id = null;
        List<Float> consensusMzValues = new ArrayList<Float>();
        List<Float> consensusIntensValues = new ArrayList<Float>();

        List<ISpectrumReference> spectrumRefs = new ArrayList<ISpectrumReference>();
        ISpectrumReference lastSpecRef = null;
        String lastMzString = "";

        while((line = br.readLine()) != null) {
            if (line.trim().equals("=Cluster=")) {
                // if we're already in a cluster, the current cluster is complete
                if (inCluster) {
                    if (lastSpecRef != null) {
                        spectrumRefs.add(lastSpecRef);
                        lastSpecRef = null;
                    }

                    // create the cluster and return
                    ICluster cluster = new ClusteringFileCluster(avPrecursorMz, avPrecursorIntens,
                            spectrumRefs, consensusMzValues, consensusIntensValues, id, clusteringFile.getName());

                    return cluster;
                }
                else {
                    // this means that this is the start of the first cluster in the file
                    inCluster = true;
                }

                continue;
            }

            if (!inCluster)
                continue;

            if (line.startsWith("id=")) {
                id = line.trim().substring(3);
                continue;
            }

            if (line.startsWith(("av_precursor_mz="))) {
                avPrecursorMz = Float.parseFloat(line.trim().substring(16));
                continue;
            }

            if (line.startsWith("av_precursor_intens=")) {
                avPrecursorIntens = Float.parseFloat(line.trim().substring(20));
                continue;
            }

            if (line.startsWith("sequence=")) {
                // this is no longer supported and unreliable
                continue;
            }

            if (line.startsWith("consensus_mz=")) {
                consensusMzValues = parseFloatValuesString(line);
                continue;
            }

            if (line.startsWith("consensus_intens=")) {
                consensusIntensValues = parseFloatValuesString(line);
                continue;
            }

            if (line.startsWith("SPEC\t")) {
                if (lastSpecRef != null) {
                    spectrumRefs.add(lastSpecRef);
                }

                lastSpecRef = new ClusteringFileSpectrumReference(line.trim());
                continue;
            }

            if (includeSpectra && line.startsWith("SPEC_MZ\t")) {
                lastMzString = line.trim();
                continue;
            }

            if (includeSpectra && line.startsWith("SPEC_INTENS\t") && lastSpecRef != null) {
                lastSpecRef.addPeaksFromString(lastMzString, line.trim());
                continue;
            }
        }

        if (lastSpecRef != null)
            spectrumRefs.add(lastSpecRef);

        if (inCluster && spectrumRefs.size() > 0 && avPrecursorMz > 0) {
            // create the cluster and return
            ICluster cluster = new ClusteringFileCluster(avPrecursorMz, avPrecursorIntens, spectrumRefs,
                    consensusMzValues, consensusIntensValues, id, clusteringFile.getName());
            inCluster = false;

            return cluster;
        }

        // we're done, so return
        return null;
    }

    private List<Float> parseFloatValuesString(String line) {
        List<Float> values = new ArrayList<Float>();

        line = line.substring(line.indexOf('=') + 1);

        // no peaks
        if (line.length() < 1)
            return values;

        String[] stringValues = line.trim().split(",");
        for (String stringValue : stringValues) {
            values.add(Float.parseFloat(stringValue));
        }

        return values;
    }

    private List<SequenceCount> parseSequenceString(String line) {
        List<SequenceCount> sequenceCounts = new ArrayList<SequenceCount>();

        line = line.trim();
        String value = line.substring(10, line.length() - 1); // remove '[' and ']'

        String[] sequenceCountStrings = value.split(",");

        for (String sequenceCountString : sequenceCountStrings) {
            int index = sequenceCountString.indexOf(':');
            if (index < 0)
                continue;
            String sequence = sequenceCountString.substring(0, index);
            sequence = sequence.toUpperCase().replaceAll("[^A-Z]", "");
            int count = Integer.parseInt(sequenceCountString.substring(index + 1));
            sequenceCounts.add(new SequenceCount(sequence, count));
        }

        return sequenceCounts;
    }
}

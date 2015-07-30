package uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jg on 10.07.14.
 */
public interface ICluster {
    public float getAvPrecursorMz();

    public float getAvPrecursorIntens();

    public Set<String> getSequences();

    public List<ISpectrumReference> getSpectrumReferences();

    public int getSpecCount();

    public int getIdentifiedSpecCount();

    public int getUnidentifiedSpecCount();

    /**
     * Returns the total number of PSMs. This can be larger than the total
     * number of spectra as spectra can be identified as multiple peptides.
     * @return
     */
    public int getPsmCount();

    public List<SequenceCount> getSequenceCounts();

    /**
     * Returns a Map with the PSM sequence as key and its occurrence
     * as value.
     * @return
     */
    public Map<String, Integer> getPsmSequenceCounts();

    public float getMaxRatio();

    public String getMaxSequence();

    public float getSpectrumPrecursorMzRange();

    public List<Float> getConsensusMzValues();

    public List<Float> getConsensusIntensValues();

    public String getId();

    public Set<String> getSpecies();
}

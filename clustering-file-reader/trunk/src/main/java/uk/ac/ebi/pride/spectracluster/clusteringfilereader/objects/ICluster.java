package uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects;

import java.util.List;
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

    public List<SequenceCount> getSequenceCounts();

    public float getMaxRatio();

    public String getMaxSequence();

    public float getSpectrumPrecursorMzRange();

    public List<Float> getConsensusMzValues();

    public List<Float> getConsensusIntensValues();
}

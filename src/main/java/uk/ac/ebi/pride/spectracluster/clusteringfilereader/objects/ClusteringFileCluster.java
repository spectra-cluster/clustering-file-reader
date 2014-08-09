package uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jg on 10.07.14.
 */
public class ClusteringFileCluster implements ICluster {
    private final float avPrecursorMz;
    private final float avPrecursorIntens;

    private final List<SequenceCount> sequenceCounts;
    private final List<ISpectrumReference> spectrumRefs;
    private final Set<String> sequences;
    private final String maxSequence;
    private Float minSpecPrecursorMz = Float.MAX_VALUE;
    private Float maxSpecPrecursorMz = 0F;

    private final float maxRatio;

    private final List<Float> consensusMzValues;
    private final List<Float> consensusIntensValues;

    @Override
    public String getMaxSequence() {
        return maxSequence;
    }

    public ClusteringFileCluster(float avPrecursorMz, float avPrecursorIntens, List<SequenceCount> sequenceCounts, List<ISpectrumReference> spectrumRefs, List<Float> consensusMzValues, List<Float> consensusIntensValues) {
        this.avPrecursorMz = avPrecursorMz;
        this.avPrecursorIntens = avPrecursorIntens;
        this.sequenceCounts = sequenceCounts;
        this.spectrumRefs = spectrumRefs;
        this.consensusMzValues = consensusMzValues;
        this.consensusIntensValues = consensusIntensValues;

        // calculate the maximum ratio
        sequences = new HashSet<String>();
        int maxSequenceCount = 0;
        String tmpMaxSequence = "";

        for (SequenceCount c : sequenceCounts) {
            sequences.add(c.getSequence());

            if (maxSequenceCount < c.getCount()) {
                maxSequenceCount = c.getCount();
                tmpMaxSequence = c.getSequence();
            }
        }

        maxRatio = (float) maxSequenceCount / (float) spectrumRefs.size();
        maxSequence = tmpMaxSequence;

        // get min and max spec precursor mz
        for (ISpectrumReference specRef : spectrumRefs) {
            float specPrecursorMz = specRef.getPrecursorMz();

            if (minSpecPrecursorMz > specPrecursorMz)
                minSpecPrecursorMz = specPrecursorMz;
            if (maxSpecPrecursorMz < specPrecursorMz)
                maxSpecPrecursorMz = specPrecursorMz;
        }
    }

    @Override
    public float getAvPrecursorMz() {
        return avPrecursorMz;
    }

    @Override
    public float getAvPrecursorIntens() {
        return avPrecursorIntens;
    }

    @Override
    public Set<String> getSequences() {
        return Collections.unmodifiableSet(sequences);
    }

    @Override
    public int getSpecCount() {
        return spectrumRefs.size();
    }

    @Override
    public float getMaxRatio() {
        return maxRatio;
    }

    @Override
    public List<SequenceCount> getSequenceCounts() {
        return Collections.unmodifiableList(sequenceCounts);
    }

    @Override
    public float getSpectrumPrecursorMzRange() {
        return maxSpecPrecursorMz - minSpecPrecursorMz;
    }

    @Override
    public List<ISpectrumReference> getSpectrumReferences() {
        return Collections.unmodifiableList(spectrumRefs);
    }

    @Override
    public List<Float> getConsensusMzValues() {
        return Collections.unmodifiableList(consensusMzValues);
    }

    @Override
    public List<Float> getConsensusIntensValues() {
        return Collections.unmodifiableList(consensusIntensValues);
    }
}

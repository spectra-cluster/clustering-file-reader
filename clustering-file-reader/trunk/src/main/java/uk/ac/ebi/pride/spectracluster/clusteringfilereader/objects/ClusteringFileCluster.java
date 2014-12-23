package uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects;

import java.util.*;

/**
 * Created by jg on 10.07.14.
 */
public class ClusteringFileCluster implements ICluster {
    private final float avPrecursorMz;
    private final float avPrecursorIntens;

    private final List<SequenceCount> sequenceCounts;
    private final List<ISpectrumReference> spectrumRefs;
    private final String maxSequence;
    private Float minSpecPrecursorMz = Float.MAX_VALUE;
    private Float maxSpecPrecursorMz = 0F;

    private final float maxRatio;
    private final int totalPsms;

    private final List<Float> consensusMzValues;
    private final List<Float> consensusIntensValues;

    private final String id;

    private Map<String, Integer> countPerPsmSequence;

    private Set<String> species;

    @Override
    public String getMaxSequence() {
        return maxSequence;
    }

    public ClusteringFileCluster(float avPrecursorMz, float avPrecursorIntens, List<SequenceCount> sequenceCounts, List<ISpectrumReference> spectrumRefs, List<Float> consensusMzValues, List<Float> consensusIntensValues, String id) {
        this.avPrecursorMz = avPrecursorMz;
        this.avPrecursorIntens = avPrecursorIntens;
        this.sequenceCounts = sequenceCounts;
        this.spectrumRefs = spectrumRefs;
        this.consensusMzValues = consensusMzValues;
        this.consensusIntensValues = consensusIntensValues;
        this.id = id;

        // calculate the ratio for each sequence
        int nTotalPSMs = 0;
        countPerPsmSequence = new HashMap<String, Integer>();

        for (ISpectrumReference specRef : spectrumRefs) {
            // ignore identical PSMs
            Set<IPeptideSpectrumMatch> uniquePsms = new HashSet<IPeptideSpectrumMatch>(specRef.getPSMs());

            for (IPeptideSpectrumMatch psm : uniquePsms) {
                if (!countPerPsmSequence.containsKey(psm.getSequence()))
                    countPerPsmSequence.put(psm.getSequence(), 0);

                countPerPsmSequence.put(psm.getSequence(), countPerPsmSequence.get(psm.getSequence()) + 1);

                nTotalPSMs++;
            }
        }

        totalPsms = nTotalPSMs;

        // get the maximum sequence
        String tmpMaxSequence = null;
        int maxSequenceCount = 0;

        for (String s : countPerPsmSequence.keySet()) {
            if (countPerPsmSequence.get(s) > maxSequenceCount) {
                tmpMaxSequence = s;
                maxSequenceCount = countPerPsmSequence.get(s);
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

        // save the species
        species = new HashSet<String>();
        for (ISpectrumReference specRef : spectrumRefs) {
            species.add(specRef.getSpecies());
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
        return Collections.unmodifiableSet(countPerPsmSequence.keySet());
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getPsmCount() {
        return totalPsms;
    }

    @Override
    public Map<String, Integer> getPsmSequenceCounts() {
        return Collections.unmodifiableMap(countPerPsmSequence);
    }

    @Override
    public Set<String> getSpecies() {
        return Collections.unmodifiableSet(species);
    }
}

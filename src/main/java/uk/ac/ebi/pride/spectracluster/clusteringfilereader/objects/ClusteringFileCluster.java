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

    private final int identifiedSpecCount;
    private final int unidentifiedSpecCount;

    private Map<String, Integer> countPerPsmSequence;

    private Set<String> species;

    private final String fileName;

    @Override
    public String getMaxSequence() {
        return maxSequence;
    }

    public ClusteringFileCluster(float avPrecursorMz,
                                 float avPrecursorIntens,
                                 List<ISpectrumReference> spectrumRefs,
                                 List<Float> consensusMzValues,
                                 List<Float> consensusIntensValues,
                                 String id,
                                 String fileName) {
        this.avPrecursorMz = avPrecursorMz;
        this.avPrecursorIntens = avPrecursorIntens;
        this.spectrumRefs = spectrumRefs;
        this.consensusMzValues = consensusMzValues;
        this.consensusIntensValues = consensusIntensValues;
        this.id = id;
        this.fileName = fileName;

        // calculate the ratio for each sequence
        int nTotalPSMs = 0, nIdentifiedSpec = 0, nUnidentifiedSpec = 0;
        countPerPsmSequence = new HashMap<String, Integer>();

        for (ISpectrumReference specRef : spectrumRefs) {
            // SpecRefs now only store unqiue psms, the previous HashSet is no longer necessary
            for (IPeptideSpectrumMatch psm : specRef.getPSMs()) {
                if (!countPerPsmSequence.containsKey(psm.getSequence()))
                    countPerPsmSequence.put(psm.getSequence(), 0);

                countPerPsmSequence.put(psm.getSequence(), countPerPsmSequence.get(psm.getSequence()) + 1);

                nTotalPSMs++;
            }

            if (specRef.isIdentified())
                nIdentifiedSpec++;
            else
                nUnidentifiedSpec++;
        }

        totalPsms = nTotalPSMs;
        this.identifiedSpecCount = nIdentifiedSpec;
        this.unidentifiedSpecCount = nUnidentifiedSpec;

        // create the sequence counts
        this.sequenceCounts = new ArrayList<SequenceCount>();
        for (String s : countPerPsmSequence.keySet()) {
            SequenceCount sc = new SequenceCount(s, countPerPsmSequence.get(s));
            sequenceCounts.add(sc);
        }

        // get the maximum sequence
        String tmpMaxSequence = null;
        int maxSequenceCount = 0;

        for (String s : countPerPsmSequence.keySet()) {
            if (countPerPsmSequence.get(s) > maxSequenceCount) {
                tmpMaxSequence = s;
                maxSequenceCount = countPerPsmSequence.get(s);
            }
        }

        float ratio = (float) maxSequenceCount / (float) identifiedSpecCount;
        if (ratio > 1) {
            maxRatio = 1.0f;
        } else {
            maxRatio = ratio;
        }

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
            if (specRef.getSpecies() == null) {
                continue;
            }
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

    @Override
    public int getIdentifiedSpecCount() {
        return identifiedSpecCount;
    }

    @Override
    public int getUnidentifiedSpecCount() {
        return unidentifiedSpecCount;
    }

    public String getFileName() {
        return fileName;
    }
}

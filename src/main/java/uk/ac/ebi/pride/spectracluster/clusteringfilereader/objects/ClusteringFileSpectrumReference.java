package uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jg on 01.08.14.
 */
public class ClusteringFileSpectrumReference implements ISpectrumReference {
    public final int INDEX_ID = 1;
    public final int INDEX_SEQUENCE = 3;
    public final int INDEX_PRECURSOR_MZ = 4;
    public final int INDEX_CHARGE = 5;
    public final int INDEX_SPECIES = 6;
    public final int INDEX_MODIFICATIONS = 7;
    public final int INDEX_SIMILARITY_SCORE = 8;
    public final String SEPARATOR = "\t";

    private final Pattern modificationPattern = Pattern.compile("([0-9]+)-([A-Z:0-9]+),?");

    private final String sequence;
    private final int charge;
    private final float precursorMz;
    private final String id;
    private float similarityScore = 0;
    private final String species;
    private final String modifications;
    private final boolean isIdentified;
    private boolean hasPeaks = false;
    private List<Peak> peaks;

    private List<IPeptideSpectrumMatch> psms = new ArrayList<IPeptideSpectrumMatch>();
    private IPeptideSpectrumMatch mostCommonPsm;

    public ClusteringFileSpectrumReference(String sequence, int charge, float precursorMz,
                                           String id, float similarityScore, String species,
                                           String modifications) {
        this.sequence = sequence;
        this.isIdentified = sequence.length() > 0;
        this.charge = charge;
        this.precursorMz = precursorMz;
        this.id = id;
        this.similarityScore = similarityScore;
        this.species = species;
        this.modifications = modifications;

        createPSMs();
    }

    public ClusteringFileSpectrumReference(String specLine) throws Exception {
        String[] fields = specLine.split(SEPARATOR, -1);

        if (fields.length < 4)
            throw new Exception("Invalid SPEC line encountered: " + specLine);

        id = fields[INDEX_ID];
        sequence = fields[INDEX_SEQUENCE];
        isIdentified = sequence.length() > 0;
        precursorMz = Float.parseFloat(fields[INDEX_PRECURSOR_MZ]);

        if (fields.length > INDEX_CHARGE)
            charge = Integer.parseInt(fields[INDEX_CHARGE]);
        else
            charge = 0;

        // species
        if (fields.length > INDEX_SPECIES) {
            String field = fields[INDEX_SPECIES].trim();
            species = field.equals("") ? null : field;
        } else {
            species = null;
        }

        // modifications
        if (fields.length > INDEX_MODIFICATIONS) {
            String field = fields[INDEX_MODIFICATIONS].trim();
            modifications = field.equals("") ? null : field;
        } else {
            modifications = null;
        }

        // similarity score
        if (fields.length > INDEX_SIMILARITY_SCORE) {
            String field = fields[INDEX_SIMILARITY_SCORE].trim();
            similarityScore = field.equals("") ? 0 : Float.parseFloat(field);
        } else {
            similarityScore = 0;
        }

        createPSMs();
    }

    /**
     * Creates the PSM objects based on the sequence string and the modification strings.
     */
    private void createPSMs() {
        psms = new ArrayList<IPeptideSpectrumMatch>();

        if (sequence.equals(""))
            return;

        String[] sequences = sequence.split(",", -1);
        String[] modificationsPerPSM = (modifications != null) ? modifications.split(";", -1) : null;

        if (modificationsPerPSM != null && sequences.length != modificationsPerPSM.length) {
            throw new IllegalStateException("Different number of peptide sequences and modification definitions encountered.");
        }

        for (int i = 0; i < sequences.length; i++) {
            ClusteringFilePSM psm = new ClusteringFilePSM(sequences[i]);

            if (modificationsPerPSM != null) {
                Matcher matcher = modificationPattern.matcher(modificationsPerPSM[i]);

                List<IModification> mods = new LinkedList<IModification>();
                while (matcher.find()) {
                    int position = Integer.parseInt(matcher.group(1));
                    String accession = matcher.group(2);

                    ClusteringFileModification mod = new ClusteringFileModification(position, accession);
                    mods.add(mod);
                }

                Collections.sort(mods);
                psm.addModifications(mods);
            }

            if (!psms.contains(psm))
                psms.add(psm);
        }

        // count how often a certain PSM was found
        Map<ClusteringFilePSM, Integer> psmCounts = new HashMap<ClusteringFilePSM, Integer>();

        for (IPeptideSpectrumMatch psmI : psms) {
            ClusteringFilePSM psm = (ClusteringFilePSM) psmI;

            if (!psmCounts.containsKey(psm))
                psmCounts.put(psm, 0);

            psmCounts.put(psm, psmCounts.get(psm) + 1);
        }

        int maxCount = 0;
        mostCommonPsm = null;

        for (ClusteringFilePSM psm : psmCounts.keySet()) {
            if (psmCounts.get(psm) > maxCount) {
                mostCommonPsm = psm;
                maxCount = psmCounts.get(psm);
            }
        }
    }

    /**
     * Adds peaks to the spectrum based on the m/z and intensity value string
     * @param mzString
     * @param intensityString
     * @throws Exception
     */
    @Override
    public void addPeaksFromString(String mzString, String intensityString) throws Exception {
        if (mzString.startsWith("SPEC_MZ\t"))
            mzString = mzString.substring(8);
        if (intensityString.startsWith("SPEC_INTENS\t"))
            intensityString = intensityString.substring(12);

        String[] mzValues = mzString.split(",");
        String[] intensValues = intensityString.split(",");

        if (mzValues.length != intensValues.length) {
            throw new Exception("Different number of m/z and intensity values encountered");
        }

        peaks = new ArrayList<Peak>(mzValues.length);
        for (int i = 0; i < mzValues.length; i++) {
            peaks.add(new Peak(Float.parseFloat(mzValues[i]), Float.parseFloat(intensValues[i])));
        }

        hasPeaks = true;
    }

    @Override
    public boolean hasPeaks() {
        return hasPeaks;
    }

    @Override
    public List<Peak> getPeaks() {
        return Collections.unmodifiableList(peaks);
    }

    @Override
    public String getSpectrumId() {
        return id;
    }

    @Override
    public float getPrecursorMz() {
        return precursorMz;
    }

    @Override
    public int getCharge() {
        return charge;
    }

    @Override
    public float getSimilarityScore() {
        return similarityScore;
    }

    @Override
    public String getSpecies() {
        return species;
    }

    @Override
    public boolean isIdentifiedAsMultiplePeptides() {
        return psms.size() > 1;
    }

    @Override
    public IPeptideSpectrumMatch getMostCommonPSM() {
        return mostCommonPsm;
    }

    @Override
    public List<IPeptideSpectrumMatch> getPSMs() {
        return Collections.unmodifiableList(psms);
    }

    public final class Peak {
        private final float mz;
        private final float intensity;

        public Peak(float mz, float intensity) {
            this.mz = mz;
            this.intensity = intensity;
        }

        public float getMz() {
            return mz;
        }

        public float getIntensity() {
            return intensity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Peak peak = (Peak) o;

            if (Float.compare(peak.intensity, intensity) != 0) return false;
            if (Float.compare(peak.mz, mz) != 0) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (mz != +0.0f ? Float.floatToIntBits(mz) : 0);
            result = 31 * result + (intensity != +0.0f ? Float.floatToIntBits(intensity) : 0);
            return result;
        }
    }

    public boolean isIdentified() {
        return isIdentified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClusteringFileSpectrumReference that = (ClusteringFileSpectrumReference) o;

        if (charge != that.charge) return false;
        if (hasPeaks != that.hasPeaks) return false;
        if (isIdentified != that.isIdentified) return false;
        if (Float.compare(that.precursorMz, precursorMz) != 0) return false;
        if (Float.compare(that.similarityScore, similarityScore) != 0) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (modifications != null ? !modifications.equals(that.modifications) : that.modifications != null)
            return false;
        if (mostCommonPsm != null ? !mostCommonPsm.equals(that.mostCommonPsm) : that.mostCommonPsm != null)
            return false;
        if (peaks != null ? !peaks.equals(that.peaks) : that.peaks != null) return false;
        if (psms != null ? !psms.equals(that.psms) : that.psms != null) return false;
        if (sequence != null ? !sequence.equals(that.sequence) : that.sequence != null) return false;
        if (species != null ? !species.equals(that.species) : that.species != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sequence != null ? sequence.hashCode() : 0;
        result = 31 * result + charge;
        result = 31 * result + (precursorMz != +0.0f ? Float.floatToIntBits(precursorMz) : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (similarityScore != +0.0f ? Float.floatToIntBits(similarityScore) : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        result = 31 * result + (modifications != null ? modifications.hashCode() : 0);
        result = 31 * result + (isIdentified ? 1 : 0);
        result = 31 * result + (hasPeaks ? 1 : 0);
        result = 31 * result + (peaks != null ? peaks.hashCode() : 0);
        result = 31 * result + (psms != null ? psms.hashCode() : 0);
        result = 31 * result + (mostCommonPsm != null ? mostCommonPsm.hashCode() : 0);
        return result;
    }

    @Override
    public String getSourceFilename() {
        int start = id.indexOf("#file=");
        if (start >= 0) {
            int end = id.indexOf("#", start + 1);
            return id.substring(start + 6, end);
        }

        return null;
    }

    @Override
    public String getSourceId() {
        int start = id.indexOf("#id=");
        if (start >= 0) {
            int end = id.indexOf("#", start + 1);
            return id.substring(start + 4, end);
        }

        return null;
    }

    @Override
    public String getOriginalSpectrumTitle() {
        int start = id.indexOf("#title=");
        if (start >= 0) {
            return id.substring(start + 7);
        }

        return id;
    }
}

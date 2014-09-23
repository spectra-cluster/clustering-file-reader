package uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects;

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

    private final String sequence;
    private final int charge;
    private final float precursorMz;
    private final String id;
    private float similarityScore = 0;
    private final String species;
    private final String modifications;

    public ClusteringFileSpectrumReference(String sequence, int charge, float precursorMz,
                                           String id, float similarityScore, String species,
                                           String modifications) {
        this.sequence = sequence;
        this.charge = charge;
        this.precursorMz = precursorMz;
        this.id = id;
        this.similarityScore = similarityScore;
        this.species = species;
        this.modifications = modifications;
    }

    public ClusteringFileSpectrumReference(String specLine) throws Exception {
        String[] fields = specLine.split(SEPARATOR, -1);

        if (fields.length < 4)
            throw new Exception("Invalid SPEC line encountered: " + specLine);

        id = fields[INDEX_ID];
        sequence = fields[INDEX_SEQUENCE];
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
    }

    @Override
    public String getSpectrumId() {
        return id;
    }

    @Override
    public String getSequence() {
        return sequence;
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
    public String getModifications() {
        return modifications;
    }


}

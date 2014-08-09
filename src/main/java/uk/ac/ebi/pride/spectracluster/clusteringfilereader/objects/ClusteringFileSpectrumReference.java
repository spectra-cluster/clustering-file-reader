package uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects;

/**
 * Created by jg on 01.08.14.
 */
public class ClusteringFileSpectrumReference implements ISpectrumReference {
    public final int INDEX_ID = 1;
    public final int INDEX_SEQUENCE = 3;
    public final int INDEX_PRECURSOR_MZ = 4;
    public final int INDEX_CHARGE = 5;
    public final String SEPARATOR = "\t";

    private final String sequence;
    private final int charge;
    private final float precursorMz;
    private final String id;

    public ClusteringFileSpectrumReference(String sequence, int charge, float precursorMz, String id) {
        this.sequence = sequence;
        this.charge = charge;
        this.precursorMz = precursorMz;
        this.id = id;
    }

    public ClusteringFileSpectrumReference(String specLine) throws Exception {
        String[] fields = specLine.split(SEPARATOR);

        if (fields.length < 4)
            throw new Exception("Invalid SPEC line encountered: " + specLine);

        id = fields[INDEX_ID];
        sequence = fields[INDEX_SEQUENCE];
        precursorMz = Float.parseFloat(fields[INDEX_PRECURSOR_MZ]);

        if (fields.length > INDEX_CHARGE)
            charge = Integer.parseInt(fields[INDEX_CHARGE]);
        else
            charge = 0;
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
}

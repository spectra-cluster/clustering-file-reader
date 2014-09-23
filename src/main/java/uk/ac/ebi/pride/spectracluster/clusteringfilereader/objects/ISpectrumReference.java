package uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects;

/**
 * Created by jg on 01.08.14.
 */
public interface ISpectrumReference {
    public String getSpectrumId();

    public String getSequence();

    public float getPrecursorMz();

    public int getCharge();

    public float getSimilarityScore();

    public String getSpecies();

    public String getModifications();
}

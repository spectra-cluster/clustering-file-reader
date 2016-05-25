package uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects;

import java.util.List;

/**
 * Created by jg on 01.08.14.
 */
public interface ISpectrumReference {
    public String getSpectrumId();

    public float getPrecursorMz();

    public int getCharge();

    public float getSimilarityScore();

    public String getSpecies();

    public boolean isIdentifiedAsMultiplePeptides();

    public boolean isIdentified();

    public IPeptideSpectrumMatch getMostCommonPSM();

    public List<IPeptideSpectrumMatch> getPSMs();

    public boolean hasPeaks();

    public List<ClusteringFileSpectrumReference.Peak> getPeaks();

    public void addPeaksFromString(String mzString, String intensityString) throws Exception;

    /**
     * Spectrum source filenames may be reported using a special id format. In cases
     * were this format was used, this function returns the spectrum's source filename.
     * @return The spectrum's source filename or NULL in case it wasn't reported.
     */
    public String getSourceFilename();

    /**
     * Spectrum source filenames and 0-based index within may be reported using a special id format. In cases
     * were this format was used, this function returns the spectrum's index within the source file.
     * @return The spectrum's source filename or NULL in case it wasn't reported.
     */
    public Integer getSourceIndex();

    /**
     * If the spectrum source file and source index were reported using the special format, this function
     * returns the original spectrum's title without the added information.
     * Otherwise, the whole title is returned.
     * @return The spectrum's original title
     */
    public String getOriginalSpectrumTitle();
}

package uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects;

import java.util.List;

/**
 * Created by jg on 24.09.14.
 */
public interface IPeptideSpectrumMatch {
    public String getSequence();

    public List<IModification> getModifications();


}

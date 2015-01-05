package uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects;

import java.util.Comparator;

/**
 * Created by jg on 05.01.15.
 */
public class PeakMzComparator implements Comparator<ClusteringFileSpectrumReference.Peak> {
    @Override
    public int compare(ClusteringFileSpectrumReference.Peak o1, ClusteringFileSpectrumReference.Peak o2) {
        return Float.compare(o1.getMz(), o2.getMz());
    }
}

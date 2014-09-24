package uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jg on 24.09.14.
 */
public class ClusteringFilePSM implements IPeptideSpectrumMatch {
    private final String sequence;
    private List<IModification> modifications;

    public ClusteringFilePSM(String sequence) {
        this.sequence = sequence;
        modifications = new ArrayList<IModification>();
    }

    @Override
    public String getSequence() {
        return sequence;
    }

    @Override
    public List<IModification> getModifications() {
        return Collections.unmodifiableList(modifications);
    }

    public void addModification(IModification modification) {
        modifications.add(modification);
    }

    public void clearModifications() {
        modifications.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClusteringFilePSM that = (ClusteringFilePSM) o;

        if (!modifications.equals(that.modifications)) return false;
        if (!sequence.equals(that.sequence)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sequence.hashCode();
        result = 31 * result + modifications.hashCode();
        return result;
    }
}

package uk.ac.ebi.pride.spectracluster.clusteringfilereader.objects;

/**
 * Created by jg on 24.09.14.
 */
public class ClusteringFileModification implements IModification {
    private final int position;
    private final String accession;

    public ClusteringFileModification(int position, String accession) {
        this.position = position;
        this.accession = accession;
    }

    public int getPosition() {
        return position;
    }

    public String getAccession() {
        return accession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClusteringFileModification that = (ClusteringFileModification) o;

        if (position != that.position) return false;
        if (!accession.equals(that.accession)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = position;
        result = 31 * result + accession.hashCode();
        return result;
    }

    @Override
    public int compareTo(IModification mod) {
        int pos = this.getPosition();
        int modPos = mod.getPosition();

        return Integer.compare(pos, modPos);
    }
}

package uk.ac.ebi.pride.spectracluster.clusteringfilereader.indexing;

import java.io.Serializable;

/**
 * IndexElement used to index .clustering files.
 *
 */
public class ClusteringIndexElement implements Serializable {
	private final String id;
	private final long start;
	private final int size;
	private float precursorMz;

	public ClusteringIndexElement(String id, float precursorMz, long start, int size) {
		this.id = id;
		this.precursorMz = precursorMz;
		this.start = start;
		this.size = size;
	}

	public long getStart() {
		return start;
	}

	public int getSize() {
		return size;
	}

	public String getId() {
		return id;
	}

    public float getPrecursorMz() {
        return precursorMz;
    }
}

# clustering-file-reader

# Introduction
The clustering-file-reader is a Java API to process .clustering files - a result file format for MS/MS based spectrum clustering. .clustering files are currently used by the [spectra-cluster](https://github.com/spectra-cluster/spectra-cluster) API, the [spectra-cluster-hadoop](https://github.com/spectra-cluster/spectra-cluster-hadoop) application and the [spectra-cluster-cli](https://github.com/spectra-cluster/spectra-cluster-cli) application.

The [spectra-cluster-hadoop](https://github.com/spectra-cluster/spectra-cluster-hadoop) application is currently used to create the [PRIDE Cluster](https://www.ebi.ac.uk/pride/cluster) resource. The complete clustering results created as a basis of [PRIDE Cluster](https://www.ebi.ac.uk/pride/cluster) are available for download in the .clustering format ([ftp location](ftp://ftp.pride.ebi.ac.uk/pride/data/cluster/result-files/)).

The .clustering result file format is a compact text file format which contains all the information related to clusters, these can include the consensus spectrum, precursor details, and spectrum related details. It is also possible to even store the spectra's original peaklists within the .clustering file.

# Getting started

### Installation
You will need to have [Maven](http://maven.apache.org/) installed in order to build and use the spectra-cluster library.

Add the following snippets in your Maven pom file:

```maven
<!-- spectra-cluster dependency -->
<dependency>
    <groupId>uk.ac.ebi.pride.spectracluster</groupId>
    <artifactId>clustering-file-reader</artifactId>
    <version>${current.version}</version>
</dependency>
```

```maven
 <!-- EBI repo -->
 <repository>
     <id>nexus-ebi-repo</id>
     <url>http://www.ebi.ac.uk/intact/maven/nexus/content/repositories/ebi-repo</url>
 </repository>

 <!-- EBI SNAPSHOT repo -->
 <snapshotRepository>
    <id>nexus-ebi-repo-snapshots</id>
    <url>http://www.ebi.ac.uk/intact/maven/nexus/content/repositories/ebi-repo-snapshots</url>
 </snapshotRepository>
```

### Running the library
The library supports two methods of reading a .clustering file:

  1. Reading all clusters in at once (only advisable for smaller files)
  2. Reading a .clustering file incrementally (optimised for very large result files)

```Java
/**
 * Example reading a file in at once
 */
File myClusteringFile = new File("/tmp/test.clustering");

// create an instance of a ClusteringFileReader
IClusterSourceReader reader = new ClusteringFileReader(myClusteringFile);

// read all clusters
List<ICluster> clusters = reader.readAllClusters();
```

```Java
/**
 * Example processing a file incrementally.
 */
File myLargeClusteringFile = new File("/tmp/large_clustering_file.clustering");

// To process clusters incrementally, the respective classes must implement
// the IClusterSourceListener interface.
IClusterSourceListener myListener = new MyListener();

// multiple readers can be added at once (f.e. one
// calculating the average cluster size, another
// writing out all consensus spectra)
List<IClusterSourceListener> listeners = new ArrayList<IClusterSourceListener>(1);
listeners.add(myListener);

// create the ClusteringFileReader
IClusterSourceReader reader = new ClusteringFileReader(myLargeClusteringFile);

// process the clusters incrementally
reader.readClustersIteratively(listeners);
```

# File format specification
The ".clustering" file format is text based. 

The first lines contain an optional header specifying properties of the algorithm and the sample set.
Each line contains one property where the property's name is separated by an "=" from the value.

### Defining clusters

Clusters start with the line "=Cluster=". 

The next lines contain the cluster's properties, one property per line where the property's
name is separated by an "=" from the value. Cluster properties are:

1. id: the cluster's id
2. av_precursor_mz: the average precursor m/z
3. av_precursor_intensity: the average precursor intensity
4. sequence: List of sequences of the peptides identified in the cluster in the format "[{sequence}:{count}]"
5. consensus_mz: ',' delimited m/z values of the consensus spectrum
6. consensus_intens: ',' delimited intensity values of the consensus spectrum

### Defining spectra in clusters

Spectra are defined one line per spectrum containing 'tab' delimited fields. A spectrum line must start
with the term "SPEC". The following fields are: 

1. spectrum's id
  * The spectrum id supports a special format to encode more detailed information about the spectrum's origin: `#file=test.mgf#id=index=120#title=The original title`.  The `id=` field should contain the spectrum's id according to the PSI convention for formatting ids in peak list files (see [mzTab specification](https://github.com/HUPO-PSI/mzTab) as an example.
2. whether this spectrum was identified as the most common peptide in the cluster ("true" / "false")
3. The identified sequence. If multiple ranks are reported, sequences must be sorted by rank and delimited by an ","
4. Spectrum's precursor's m/z
5. Spectrum's charge
6. Species (taxid), ',' delimited
7. Modifications in the format "[position]-[accession]". Multiple modifications must be separated by an ",". If multiple PSMs are reported these modification groups must be separated by an ";".
8. The similarity of the spectrum (based on the used similarity metric) to the cluster's consensus spectrum

It is possible to add a spectrum's peak list to the clustering file. To do this, the spectrum definition line ("SPEC..." line) is followed by a "SPEC_MZ" and "SPEC_INTENS" line. These lines contain the spectrum's m/z and intensity values respectively as ',' separated lists.

### Example

```
=Cluster=
id=197b4666-4e7e-4d61-b1a1-e032b1e15aa7
av_precursor_mz=357.221
av_precursor_intens=1.0
sequence=[GIFAFVK,GIFAFVK:3]
consensus_mz=114.109,115.106,120.076,...
consensus_intens=292.16,272.41,2241.61,...
SPEC	PXD000732;MFerrer_PAO1_2013.xml;spectrum=3121	true	GIFAFVK,GIFAFVK	357.22116	3	287	7-MOD:01499,0-MOD:01499;7-MOD:01499,0-MOD:01499	0.9987784157651239
```


# Getting help
If you have questions or need additional help, please contact the PRIDE Help desk at the EBI.

email: pride-support at ebi.ac.uk (replace at with @).

# Giving your feedback
Please give us your feedback, including error reports, suggestions on improvements, new feature requests. You can do so by opening a new issue at our [issues section](https://github.com/spectra-cluster/clustering-file-reader/issues)

# How to cite
Please cite this library using one of the following publications:
- Griss J, Foster JM, Hermjakob H, Vizcaíno JA. PRIDE Cluster: building the consensus of proteomics data. Nature methods. 2013;10(2):95-96. doi:10.1038/nmeth.2343. [PDF](http://www.nature.com/nmeth/journal/v10/n2/pdf/nmeth.2343.pdf),  [HTML](http://www.nature.com/nmeth/journal/v10/n2/full/nmeth.2343.html),  [PubMed](http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3667236/)

# Contribute
We welcome all contributions submitted as [pull](https://help.github.com/articles/using-pull-requests/) request.

# License
This project is available under the [Apache 2](http://www.apache.org/licenses/LICENSE-2.0.html) open source software (OSS) license.

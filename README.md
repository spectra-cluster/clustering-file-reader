# clustering-file-reader

# Introduction
clustering-file-reader is a library for parsing the clustering output produced by either [spectra-cluster](https://github.com/spectra-cluster/spectra-cluster) or [spectra-cluster-hadoop](https://github.com/spectra-cluster/spectra-cluster-hadoop) library.
The clustering result file is compact text file format which contains all the information related to clusters, these can include the consensus spectrum, precursor details, and spectrum related details.
This library supports parsing both the entire file and iterate over all the entries in a particular file

# Getting started

### add
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
TBD

# Getting help
If you have questions or need additional help, please contact the PRIDE Help desk at the EBI.

email: pride-support at ebi.ac.uk (replace at with @).

# Giving your feedback
Please give us your feedback, including error reports, suggestions on improvements, new feature requests. You can do so by opening a new issue at our [issues section](https://github.com/spectra-cluster/spectra-cluster/issues) 

# How to cite
Please cite this library using one of the following publications:
- Griss J, Foster JM, Hermjakob H, Vizcaíno JA. PRIDE Cluster: building the consensus of proteomics data. Nature methods. 2013;10(2):95-96. doi:10.1038/nmeth.2343. [PDF](http://www.nature.com/nmeth/journal/v10/n2/pdf/nmeth.2343.pdf),  [HTML](http://www.nature.com/nmeth/journal/v10/n2/full/nmeth.2343.html),  [PubMed](http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3667236/)

# Contribute
We welcome all contributions submitted as [pull](https://help.github.com/articles/using-pull-requests/) request.

# License
This project is available under the [Apache 2](http://www.apache.org/licenses/LICENSE-2.0.html) open source software (OSS) license.

Genome Database of Kimchi-associated Microbes (GDKM) is an integrated data management system that produces genomic information of the microorganisms associated with Korean fermented vegetables (kimchi) and makes standardized genome data available to researchers in academia and industry.

GDKM is a web-based application that can be serviced in any web browser that supports HTML5. It is designed to manage data in MySQL DB and share information through REST-ful API.
This source does not contain genomic data. It provides only archive function to register and manage microbial genomic data. 
Kimchi and associated microbial genome data can be found at the following site.(http://gdkm.wikim.re.kr)

# Dependencies
### Required
 * JDK 1.8+ (https://www.oracle.com/technetwork/java/index.html)
 * MySQL 5.+ (https://www.mysql.com)
 * FastQC v0.11.+ (https://www.bioinformatics.babraham.ac.uk/projects/fastqc/)
 * clc_sequence_info tool for clc-assembly-cell 4.2 (https://www.qiagenbioinformatics.com/products/clc-assembly-cell/)
 * dRep (https://drep.readthedocs.io/en/latest/index.html)
 * ncbi-blast-2.7.+ (https://ftp.ncbi.nlm.nih.gov/blast/executables/blast+/)
 
### Optional
 * Eclipse Java EE IDE (https://www.eclipse.org/)
 * Apache Maven (https://maven.apache.org/download.cgi) 
  *If you use Eclipse, no installation required*

# Web Application Build
GDKM was developed using the Eclipse Java EE IDE (Oxygen 4.7) for web developers, and the shared source of the repository contains the configuration files for you to include into your Eclipse project.
We will explain how to build a project using Eclipse. 

### Include GDKM project into Eclipse
 * Copy the workspace folder from the repository into the Eclipse workspace folder with the appropriate name(ex.GDKM).
 * Run installed eclipse. 
 * Click ***File > Import*** of the main menu at the top of Eclipse.
 * Select *Exisiting Projets into workspace*, then select the folder you copied in the previous step.
 
### Web application build by Maven
 * Select GDKM project in ***Project Explorer*** of eclipse.
 * Click the left mouse button to select the next menu.***Run As > Maven install*** 
 * When you run Maven, it checks the library dependencies and create a WAR file in the target folder set in pom.xml.
 
# Installation
### MySQL Database Creation
 * Create a database for GDKM in MySQL (recommand name is 'gdkm')
 * Create GDKM DB tables by executing a query of the file ***/workspace/src/main/gdkm_create_tables_20190930.sql*** file.
 * MySQL database settings are contained in the ***/workspace/src/main/resources/egovframework/spring/context-datasource.xml*** file. 
 You can change settings to your database.
 ```
  <property name="url" value="jdbc:mysql://localhost:3306/gdkm?useUnicode=true&amp;characterEncoding=utf8"/>
  <property name="username" value="your_db_account"/>
  <property name="password" value="your_db_password"/>
 ```
 
### Analysis tools setting
  * The analysis tool path setting is in the ***/workspace/src/main/resources/egovframework/egovProps/globals.properties*** file.
 ```
 TOOL.CLC_ASSEMBLY_CELL.PATH = /tools/clc_sequence_info
 TOOL.FASTQC.PATH = /tools/fastqc_v0.11.5/fastqc
 TOOL.ANI.JAR.PATH = /tools/gdkm-drep-ani-cli.jar
 TOOL.BLAST.JAR.PATH = /tools/gdkm-blast-cli.jar
 TOOL.BLAST.SEQ.JAR.PATH = /tools/gdkm-create-blast-formatdb.jar
 TOOL.BLAST.DB.DIR = /tools/ncbi-blast-2.7.1+/blastDb
 TOOL.BLAST.SEQUENCE.DIR = /data/gdkm/tools/blast/sequence
 TOOL.BLAST.INPUT.DIR = /data/gdkm/tools/blast/input
 TOOL.BLAST.OUTPUT.DIR = /data/gdkm/tools/blast/output
 ```
 * The analysis tools are installed in */tools* folder and the data storage folder is to be */data*. (it can change settings)
 * Copy all jar file in *tools* folder of source repository to the *tools* folder in your server.
 
### Deploy Web Application to Tomcat server.
 * Run the WAS server(Apache Tomcat) installed on your server.
 * Deploy the built GDKM WAR file to webapps folder of your WAS server. 

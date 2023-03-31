> :warning: **This project is no longer maintained.**  
> For tools related to RODA, please look at https://market.roda-community.org

keeps-characterization-nsf
==========================

Characterization tool for Lotus Notes .nsf files, made by KEEP SOLUTIONS.


## Build & Use

To build the application simply clone the project and execute the following Maven command: `mvn clean package`  
The binary will appear at `target/nsf-characterization-tool-1.0-SNAPSHOT-jar-with-dependencies.jar`

To run execute the jar file, the folder where Lotus Notes is installed must be added to the environment variable LD_LIBRARY_PATH and the java used to should be the one provided with Lotus Notes.
In a *nix environment, the steps are:
```bash
NOTES_PATH=/opt/ibm/notes
NOTES_JVM_PATH=/opt/ibm/notes/jvm
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$NOTES_PATH
$NOTES_JVM_PATH/bin/java -jar [jar file] -f [file to process]
```

To see the usage options execute the command:

```bash
$ java -jar target/nsf-characterization-tool-1.0-SNAPSHOT-jar-with-dependencies.jar -h
usage: java -jar [jarFile]
 -f <arg> file to analyze
 -h       print this message
 -v       print this tool version
```

## Tool Output Example
```bash
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<nsfCharacterizationResult>
    <features>
        <item>
            <key>size</key>
            <value>2.251803305930516E15</value>
        </item>
        <item>
            <key>percentUsed</key>
            <value>91.2</value>
        </item>
        <item>
            <key>documentCount</key>
            <value>1</value>
        </item>
        <item>
            <key>sizeQuota</key>
            <value>0</value>
        </item>
        <item>
            <key>created</key>
            <value>10/14/2013 06:24:12 PM GDT</value>
        </item>
        <item>
            <key>categories</key>
            <value></value>
        </item>
        <item>
            <key>views</key>
            <value>($Subscriptions) ; Subscriptions ; ($FA0F04057:3C58A4BB-N852566AB:0077885A)</value>
        </item>
    </features>
    <validationInfo>
        <valid>true</valid>
    </validationInfo>
</nsfCharacterizationResult>
```

## License

This software is available under the [LGPL version 3 license](LICENSE).


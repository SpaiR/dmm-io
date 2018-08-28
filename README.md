[![Build Status](https://travis-ci.org/SpaiR/dmm-io.svg?branch=master)](https://travis-ci.org/SpaiR/dmm-io)
[![Javadocs](https://www.javadoc.io/badge/io.github.spair/dmm-io.svg)](https://www.javadoc.io/doc/io.github.spair/dmm-io)
[![License](http://img.shields.io/badge/license-MIT-blue.svg)](http://www.opensource.org/licenses/MIT)

# Dmm I/O

## About

I/O library for dmm files of default and TGM format.

## Installation
[![Maven Central](https://img.shields.io/maven-central/v/io.github.spair/dmm-io.svg?style=flat)](https://search.maven.org/search?q=a:dmm-io%20g:io.github.spair)
[![JCenter](https://img.shields.io/bintray/v/spair/io.github.spair/dmm-io.svg?label=jcenter)](https://bintray.com/spair/io.github.spair/dmm-io/_latestVersion)

Library deployed to Maven Central and JCenter repositories.

#### pom.xml
```
<dependency>
    <groupId>io.github.spair</groupId>
    <artifactId>dmm-io</artifactId>
    <version>${last.version}</version>
</dependency>
```

#### build.gradle:
```
compile 'io.github.spair:dmm-io:${last.version}'
```

## How To Use

File could be parsed like: `DmmData data = DmmReader.readMap(mapFile)`
The map itself could be in standard BYOND format as well as in TGM, reader will handle everything.

To write `DmmData` to file use `DmmWriter` class like that:
```
DmmWriter.saveAsByond(fileToSave, dmmData)  // to save in BYOND format
DmmWriter.saveAsTGM(fileToSave, dmmData)    // to save in TGM format
```
If `fileToSave` doesn't exist it will be created.

More could be found in [JavaDoc](https://www.javadoc.io/doc/io.github.spair/dmm-io).

## Credits

Some implementation ideas were taken from [JMerge](https://github.com/Baystation12/JMerge) repository. Big thanks to [@atlantiscze](https://github.com/atlantiscze) for his work.

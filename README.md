# DBMole

A database connectivity testing tool. Currently works with Oracle, but plans are to make it work with databases that are supported by Java.

[![Download DBMole](https://img.shields.io/badge/DBMole-Download-orange.svg)](https://github.com/davydany/dbmole/releases)

## Supported Databases

* Oracle
* Oracle with TLS
* PostgreSQL
* MongoDB

## Getting Started

1. Download the `dbmole-<VERSION>.jar` jar file from here: [![Download DBMole](https://img.shields.io/badge/DBMole-Download-orange.svg)](https://github.com/davydany/dbmole/releases)
2. Run: `java -jar dbmole-<VERSION>.jar` to see all the options you can pass to DBConn. 
3. Now test the database you want to connect to.

## Basic Usage

To get started, run with `--help` flag to see all the options. 

You will need to provide the `hostname` and `port` of your database
as they are required, followed by optional fields like `--username`, 
`--password`, and `--database`. Use `--help` for additional parameters
and options you can pass to `dbmole`.

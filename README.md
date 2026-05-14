# pc2lib3: The PC² Assistant Tool

A simple tool with convienience functions for PC². Forked from the originally written code at [lingfeishengtian/pc2lib2](https://github.com/lingfeishengtian/pc2lib2) and modified to support even more features.

## Getting Started

Due to the way PC^2 works, you must use legacy encryption methods. To do this, add this to your vm options:

```
-Djdk.crypto.KeyAgreement.legacyKDF=true
```

Without it, the library will always fail to load a contest.

**Important:** All judges and the host must use the same PC² installation path. If judges are on a different operating system than the host, the host can adjust the Judge path to match the judge's OS path formatting in the Problems section. This ensures that all judges can access the problem data correctly regardless of their platform.

Since the library simulates the running of a PC^2 server, the following line of code is recommended to forcibly close the server, but will also terminate the program. It is recommended you put this line at the end of your main method.

```java
System.exit(0);
```

View the [wiki](https://github.com/procurial/pc2lib3/wiki) for more documentation.

For detailed documentation on the pc2pqascript automation script and its commands, see [pc2pqascript.README.md](pc2pqascript.README.md).

You can run a pc2pqascript with a command like this:

```bash
echo "run script.pc2pqascript" | java -cp pc2lib3-1.0.0.jar -Djdk.crypto.KeyAgreement.legacyKDF=true com.procurial.cli.CLIStarter
```

## Building pc2lib3

pc2lib3 is built using Maven. Make sure Maven is installed, and then you have to manually install the `pc2.jar` library with the `mvn install:install-file -Dfile=[path to pc2.jar] -DgroupId=edu.csus.ecs.pc2 -DartifactId=pc2 -Dversion=1.0 -Dpackaging=jar` where `[path to pc2.jar]` is the path to the `pc2.jar` file found in the lib folder of the `pc2-[version]` folder that can be downloaded in zip form from the [pc2 website](https://pc2ccs.github.io/). After that, you can run `maven compile` and then `maven package` to get the `pc2lib3-[version].jar`.
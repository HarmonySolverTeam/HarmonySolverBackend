####This module implement rest api logic.

In order to build you need to run 
1) 'sbt rest/openApiGenerate'
2) 'sbt rest/run'
3) In order to clean up generated sources there is clean.bat script prepared

Server will be available on address localhost:9000

"C:\Program Files\Java\jdk1.8.0_231\bin\java.exe" -Dfile.encoding=UTF-8 -Djava.library.path=C:/Users/lenovo/Desktop/In
zynierka/HarmonySolverBackend/libs -Xms1024m -Xmx1024m -cp "C:\Program Files (x86)\sbt\\bin\sbt-launch.jar" xsbt.boot.Boot rest/run

Note: generated sources should not be committed to git repository.\
Note2: probably generating sources at compile time as well as cleaning generated sources could be done using sbt, but it hasn't been achieved due to lack of time
.PHONY: run clean

byog/Core/Main.class:
	/usr/lib/jvm/java-11-openjdk-amd64/bin/javac -classpath .:library-sp18/javalib/algs4.jar:library-sp18/javalib/hamcrest-core-1.3.jar:library-sp18/javalib/jh61b.jar:library-sp18/javalib/junit-4.12.jar:library-sp18/javalib/stdlib-package.jar:library-sp18/javalib/stdlib.jar byog/*/*.java

run: byog/Core/Main.class
	/usr/lib/jvm/java-11-openjdk-amd64/bin/java -classpath .:library-sp18/javalib/algs4.jar:library-sp18/javalib/hamcrest-core-1.3.jar:library-sp18/javalib/jh61b.jar:library-sp18/javalib/junit-4.12.jar:library-sp18/javalib/stdlib-package.jar:library-sp18/javalib/stdlib.jar byog.Core.Main

clean:
	rm byog/*/*.class

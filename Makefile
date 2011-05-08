build-all:
	javac *.java

javadoc:
	javadoc -private -linksource -d javadocs *.java

clean:
	rm *.class

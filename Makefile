build-classfiles: *.java
	javac *.java

all: build-classfiles javadocs

javadocs: *.java
	javadoc -private -linksource -d javadocs *.java

clean:
	rm *.class
	rm -rf javadocs

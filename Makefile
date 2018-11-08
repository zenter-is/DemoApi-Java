

.SUFFIXES: .class

ifeq ($(OS),Windows_NT)
CP=".;jars\*"
else
CP=".:jars/*"
endif

Example1: clean Example1.class
	java  -cp $(CP) Example1

Example2: clean Example2.class
	java -cp $(CP) Example2

Example3: clean Example3.class
	java -cp $(CP) Example3

%.class: %.java
	javac -cp $(CP) $<

clean:
	rm -f *.class

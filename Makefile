

.SUFFIXES: .class

CP=".:jars/*"

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
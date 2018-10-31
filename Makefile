.SUFFIXES: .class

Example1: clean Example1.class
	java -cp ".:jars/*" Example1

Example2: clean Example2.class
	java -cp ".:jars/*" Example2

%.class: %.java
	javac -cp ".:jars/*" $<

clean:
	rm -f *.class
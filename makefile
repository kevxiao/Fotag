JFLAGS = -g
JC = javac
JVM= java
FILE=
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Fotag.java \
	FotagModel.java \
	Toolbar.java \
	ImageFrame.java \
	ImageModel.java \
	ImagePanel.java \
	Constants.java

MAIN = Fotag

default: classes

classes: $(CLASSES:.java=.class)

run: classes
	$(JVM) $(MAIN)

clean:
	$(RM) *.class
all: build run

build:
	javac spar/Main.java

run:
	java spar/Main

clean:
	rm spar/*.class
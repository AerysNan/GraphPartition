all: bin run

bin:
	javac spar/Main.java

run:
	java spar/Main

clean:
	rm spar/*.class
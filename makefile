JFLAGS = -g -d bin -cp bin

vpath %.java src/
vpath %.class bin/

.SUFFIXES: .java .class

.java.class:
	javac $(JFLAGS) $<

all: Util.class Packet.class Listener.class Server.class Client.class

Listener.class : Server.class

Server.class:
	rm -rf bin/Listener.class bin/Server.class
	javac $(JFLAGS) src/Listener.java src/Server.java

run:
	@open; java -cp bin Server &
	@open; java -cp bin Client &
	@open; java -cp bin Client &

clean:
	@rm -f bin/*

'''
KTN-project 2013 / 2014
Python daemon thread class for listening for events on
a socket and notifying a listener of new messages or
if the connection breaks.

A python thread object is started by calling the start()
method on the class. in order to make the thread do any
useful work, you have to override the run() method from
the Thread superclass. NB! DO NOT call the run() method
directly, this will cause the thread to block and suspend the
entire calling process' stack until the run() is finished.
it is the start() method that is responsible for actually
executing the run() method in a new thread.
'''
from threading import Thread


class ReceiveMessageWorker(Thread):

    def __init__(self, listener, connection):
        Thread.__init__(self)
        self.daemeon = True
        self.running = True
        self.c = connection
        self.l = listener
        #self.start()

    def run(self):
        while self.running:
            try:
                self.m = self.c.recv(1024)
                self.l.message_received(self.m, self.c)
            except:
                print 'lost connection to server.'
                self.l.force_disconnect()

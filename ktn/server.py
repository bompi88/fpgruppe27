'''
KTN-project 2013 / 2014
Very simple server implementation that should serve as a basis
for implementing the chat server
'''
import socket
import SocketServer
import json
'''
The RequestHandler class for our server.

It is instantiated once per connection to the server, and must
override the handle() method to implement communication to the
client.
'''

allowedCharacters = '1234567890qwertyuiopasdfghjklzxcvbnm_'
brukernavn = []
handlers = []  # List containing all instances of CLientHandlers
rooms = {} # key is room name, value is list of usernames of users currently in the room.

class CLientHandler(SocketServer.BaseRequestHandler):
    
    
    
    def handle(self):
        # Add this handler to the list with all handlers
        handlers.append(self)
        
        self.loggedIn = False
        self.inRoom = False
        
        # Get a reference to the socket object
        self.connection = self.request
        # Get the remote ip adress of the socket
        self.ip = self.client_address[0]
        # Get the remote port number of the socket
        self.port = self.client_address[1]
        print 'Client connected @' + self.ip + ':' + str(self.port)
        
        
        # Main loop 
        
        while True:
            #Wait for data from the client
            recv = None
            try:
                recv = self.received()
            except:
                print 'Lost conntact to client ' + self.ip + ' abruptly.'
                
            # Respond accordingly to received request
            if not recv:
                print 'Client disconnected \n'
                if self.loggedIn:
                    brukernavn.remove(self.username)
                    self.loggedIn = False
                    if self.inRoom:
                        rooms[self.room].remove(self.username)
                        self.inRoom = False
                break
                
            elif recv['request'] == 'login':
                if self.godtarBrukernavn(recv['username']) and not( self.loggedIn) :
                    self.response({'response': 'login', 'username': recv['username'], 'message': 'Username accepted'})
                    brukernavn.append(recv['username'])
                    self.loggedIn = True
                    self.username = recv['username']
                    m = self.username + ' logged in to server.'
                    server.sendMessageToAll(m)
                else:
                    self.response({'response': 'login', 'username': recv['username'], 'error': 'Invalid username!'})
                    
            elif recv['request'] == 'message':
                if self.loggedIn:
                    if self.inRoom:
                        m = self.username + ' says: ' + recv['message']
                        server.sendMessageInRoom(m, self.room)
                    else:
                        m = self.username + ' says : ' + recv['message']
                        server.sendMessageToAll(m)
                else:
                    self.response({'response': 'message', 'error': 'Not logged in!'})
                    
            elif recv['request'] == 'enter_room':
                if not self.loggedIn:
                    self.response({'response': 'enter_room', 'error': 'Not logged in!'})
                elif not(recv['room'] in rooms):
                    self.response({'response': 'enter_room', 'error': 'The room does not exist!'})
                else:
                    rooms[recv['room']].append(self.username)
                    self.inRoom = True
                    self.room = recv['room']
                    self.response({'response': 'enter_room', 'room': recv['room']})
                    m = self.username + ' entered room.'
                    server.sendMessageInRoom(m, self.room)
            
            elif recv['request'] == 'leave_room':
                if not self.loggedIn:
                    self.response({'response': 'leave_room', 'error': 'Not logged in!'})
                elif not self.inRoom:
                    self.response({'response': 'leave_room', 'error': 'You are not in a room.'})
                else:
                    rooms[self.room].remove(self.username)
                    self.inRoom = False
                    self.response({'response': 'leave_room', 'room': recv['room']})
                    m = self.username + ' left this room.'
                    server.sendMessageInRoom(m, self.room)
                    self.room = None
            
            elif recv['request'] == 'create_room':
                if not self.loggedIn:
                    self.response({'response': 'create_room', 'error': 'Not logged in!'})
                elif recv['room'] in rooms or not all(c in allowedCharacters for c in recv['room']) :
                    self.response({'response': 'create_room', 'error': 'Invalid room name'})
                else:
                    rooms[recv['room']] = []
                    self.response({'response': 'create_room', 'room': recv['room']})
                    m = self.username + ' created a room called ' + recv['room']
                    server.sendMessageToAll(m)
            
            elif recv['request'] == 'delete_room':
                if not self.loggedIn:
                    self.response({'response': 'delete_room', 'error': 'Not logged in!'})
                elif not recv['room'] in rooms:
                    self.response({'response': 'delete_room', 'error': 'No such room!'})
                elif len(recv['room']) != 0:
                    self.response({'response': 'delete_room', 'error': 'All users must leave room before it can be deleted.'})
                else:
                    del rooms[recv['room']]
                    self.response({'response': 'delete_room', 'room': recv['room']})
            
            elif recv['request'] == 'logout':
                if self.loggedIn:
                    self.response({'response': 'logout', 'username': self.username})
                    self.loggedIn = False
                    brukernavn.remove(self.username)
                else:
                    self.response({'response': 'logout', 'username': '', 'error': 'Not logged in!'})
                
            else:
                print 'unrecognised request'
        
            
                
    def godtarBrukernavn(self,b):
        if not(b in brukernavn) and all(c in allowedCharacters for c in b):
            return True
        return False
        
    def response(self,md):
        self.connection.sendall(json.dumps(md))
        
    def received(self):
        json_data = self.connection.recv(1024)
        if json_data:
            data_dict = json.loads(json_data)
            return data_dict
        else:
            pass
            

'''
This will make all Request handlers being called in its own thread.
Very important, otherwise only one client will be served at a time
'''


class ThreadedTCPServer(SocketServer.ThreadingMixIn, SocketServer.TCPServer):
    
    def sendMessageInRoom(self, m, r):
        for c in handlers:
            if c.loggedIn:
                if c.username in rooms[r]:
                    c.connection.sendall(json.dumps({'response': 'message', 'message': m}))
    
    def sendMessageToAll(self, m):
        for c in handlers:
            if c.loggedIn:
                c.connection.sendall(json.dumps({'response': 'message', 'message': m}))

if __name__ == "__main__":
    HOST = socket.gethostbyname(socket.gethostname())
    PORT = 9999
    
    print 'Server ip: ' + HOST
    
    # Create the server, binding to localhost on port 9999
    server = ThreadedTCPServer((HOST, PORT), CLientHandler)

    # Activate the server; this will keep running until you
    # interrupt the program with Ctrl-C
    server.serve_forever()

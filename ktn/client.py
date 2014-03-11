'''
KTN-project 2013 / 2014
'''
import socket
import json
import MessageWorker
import time


class Client(object):

    def __init__(self):
        self.connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def start(self, host, port):
        
        self.running = True
        
        print 'Welcome \n'
        while True:
            host = raw_input('Host: ')
            if host == 'end':
                self.running = False
                break
            #port = int(raw_input('Port: '))
            
            try:
                self.connection.connect((host, port))
                break
            except:
                print 'Try again. \n'
        
        if self.running:
            self.mw = MessageWorker.ReceiveMessageWorker(self, self.connection)
            self.mw.start()
        
        
        while self.running:
            self.inputRequest()

    def message_received(self, message, connection):
        d = json.loads(message)
        
        if 'error' in d:
            print 'Error: ' + d['error']
                                          
        elif 'request' in d:
            print d['message']
            
        elif 'response' in d:
            if d['response'] == 'login':
                if 'error' in d:
                    print d['error']
                else:
                    print d['message']
                    
            elif d['response'] == 'logout':
                if 'error' in d:
                    print d['error']
                else:
                    print 'You have logged out.'
                    
            elif d['response'] == 'message':
                print d['message']

    def connection_closed(self, connection):
        pass

    def send(self, data):
        try:
            j = json.dumps(data)
            self.connection.sendall(j)
        except:
            print 'JSON ERROR. Remember to not use norwegian characters.'

    def force_disconnect(self):
        print 'Shutting down'
        try:
            self.connection.shutdown(1)
        except:
            pass
        self.connection.close()
        self.running = False
        self.mw.running = False
        #self.mw.join(1)
        
        
    def inputRequest(self):
        time.sleep(1)
        if self.running:
            req = raw_input('State your request (login, message, logout or end):\n')
            req = req.strip()
        if not self.running:
            pass
        elif req == 'login':
            self.login()
            
        elif req == 'message':
            print 'The message will be sent to all logged in users (say "back" to go back) \n'
            while self.running:
                mes = raw_input()
                if mes == 'back':
                    break
                self.send({'request': 'message', 'message': mes})
                
        elif req == 'logout':
            self.send({'request': 'logout'})
        
        elif req == 'advanced':
            D = {}
            while True:
                key = raw_input('Key: ')
                if key == '':
                    break
                value = raw_input('Value: ')
                D[key] = value
            
            self.send(D)
            
        elif req == 'end':
            self.force_disconnect()
            
        else:
            print 'Unrecognised request'
        
    def login(self):
        inp = raw_input('Username: ')
        d = {'request': 'login','username': inp}
        self.send(d)
   


if __name__ == "__main__":
    client = Client()
    client.start('localhost', 9999)

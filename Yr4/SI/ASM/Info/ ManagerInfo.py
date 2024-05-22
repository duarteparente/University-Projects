class ManagerInfo():

    def __init__(self, name):
        self.name = name
        self.transport_list = []
        self.local_list = []
    
    def getNames(self):
        return self.name

    def getTransList(self):
        return self.transport_list
    
    def getLocalsList(self):
        return self.local_list

    def addTransport(self, transport):
        self.transport_list.append(transport)

    
    def addLocal(self, local):
        self.local_list.append(local)
    

    
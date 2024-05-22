import random

class TransportInfo():

    def __init__(self, model ,type, passengers,location, _class):
        self.model = model
        self.type = type
        self.location = location #(random.randint(-1000,1000), random.randint(-1000,1000))
        self.passengers = passengers
        self._class = _class
        self.available = True
        #self.flag = 0


    def getModel(self):
        return self.model
    
    def getType(self):
        return self.type

    def getLocation(self):
        return self.location
    
    def getPassengers(self):
        return self.passengers

    def getAvailability(self):
        return self.available
    
    def getFlag(self):
        return self.flag

    def getClass(self):
        return self._class
    
    def setAvailability(self,aval):
        self.available = aval
from spade.agent import Agent

from Behaviours.SubscribeTransport import SubscribeTransport
from Behaviours.ExecuteRide import ExecuteRide

import random

class Moto(Agent):

    async def setup(self):

        model = str(self.jid).split("@")[0]
        
        managers = ["uber","freenow","glovo"]

        self.set("Model", model)
        self.set("Status", "Available")
        self.set("Managers", managers)
        self.set("Type", "Moto")
        self.set("Class","Saver")
        self.set("Passengers", 1)
        self.set("Location",(random.randint(-1000,1000), random.randint(-1000,1000)))

        subs = SubscribeTransport()
        self.add_behaviour(subs)

        ride = ExecuteRide()
        self.add_behaviour(ride)
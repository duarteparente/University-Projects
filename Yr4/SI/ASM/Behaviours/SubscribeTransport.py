from spade.behaviour import OneShotBehaviour
from spade.message import Message

from Info.TransportInfo import TransportInfo

import jsonpickle


class SubscribeTransport(OneShotBehaviour):

    async def run(self):

        transport = TransportInfo(self.agent.get("Model"), self.agent.get("Type"), self.agent.get("Passengers"),self.agent.get("Location"), self.agent.get("Class"))
        managers = self.agent.get("Managers")

        
        for manager in managers:
            msg = Message(to=f"{manager}@santi1904")
            msg.body = jsonpickle.encode(transport)
            msg.set_metadata("performative", "subscribe")
            await self.send(msg)
            #print("Transport subscription Sent to Manager..." + manager)

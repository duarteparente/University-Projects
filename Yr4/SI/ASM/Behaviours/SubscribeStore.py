import random
from spade.behaviour import OneShotBehaviour
from spade.message import Message
from Info.StoreInfo import StoreInfo
import jsonpickle


class SubscribeStore(OneShotBehaviour):

    async def run(self):

        store = StoreInfo(self.agent.get("Name"),self.agent.get("Stock1"),self.agent.get("Stock2"),self.agent.get("Stock3"))
        managers = self.agent.get("Managers")
        
        for manager in managers:
            msg = Message(to=f"{manager}@santi1904")
            msg.body = jsonpickle.encode(store)
            msg.set_metadata("performative", "subscribe")
            await self.send(msg)
            #print("Store subscription Sent to Manager..." + manager)

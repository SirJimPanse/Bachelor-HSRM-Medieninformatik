def change():
    newWeigth = 0.
    newPrice = 0.
    temp = list(changer.products)
    inputProductNames = {"Apfel":0, "Birne":0, "Mango":0, "Maracuja":0}
    for act in temp:
        if act.name in inputProductNames:
            newWeigth += act.weight/2
            newPrice += act.price/2
            inputProductNames[act.name] += 1
            changer.products.remove(act)
    changedProduct = Product("-".join([k for k,v in inputProductNames.items() if  v > 0])+"Saft", newWeigth, newPrice, "Saft");
    #if changer.maxCapacityIsReached():
    changer.products += [changedProduct]
    
def produce():
    producer.counter += 1
    producer.product = Product("Mango", 5, 7, "Obst")
    producer.noProductsPerProduction = 1
    if(producer.counter > 1):
        producer.product = Product("Apfel", 2, 3, "Obst")
        producer.noProductsPerProduction = 1
        producer.counter = 0

def init():
    positioner.setOrigin("5-2")
    positioner.addFollower("5-3")
    positioner.addFollower("5-5")

        
def move():
    for product in positioner.origin.products:
        if product.weight > 3:
            positioner.chooseDestination("5-3") 
        else:
            positioner.chooseDestination("5-5") 


    

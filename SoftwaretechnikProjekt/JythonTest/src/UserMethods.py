def edit():
    changer.inputProducts = ["Apfel", "Birne"]
    for product in changer.products:
        if product in changer.inputProducts:
            changer.products += [product+"saft"]
            changer.products.remove(product)

def edit_alternative():
    lis = list(changer.products)
    changer.inputProducts = ["Apfel", "Birne"]
    for product in changer.products:
        if product in changer.inputProducts:
            lis.append(product+"saft")
            lis.remove(product)
    changer.products = lis

def change():
    inputProducts = ["Apfel", "Birne"]
    for product in products:
        if product in inputProducts:
            products.append(product+"saft")
            products.remove(product)
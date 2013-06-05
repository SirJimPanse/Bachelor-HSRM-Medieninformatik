import Image, string

pic = "mi.png"

im = Image.open(pic)
piclist = list(im.getdata())

l = []
c = 2
for i in piclist[3:]:
    if piclist[c] == piclist[c - 1] == piclist[c - 2]:
        l.append(piclist[c])
    c += 1

print l[:100]
    



    

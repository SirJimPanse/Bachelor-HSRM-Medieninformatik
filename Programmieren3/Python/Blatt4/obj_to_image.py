import Image, sys
import obj_parser, obj_triangle


def makepic(points, name, mode):
    im = Image.new("1", (400, 400))
    if mode == "xy":
        for i in points:
            im.putpixel((int(i[0]), int(i[1])), 1)
        im.save(name + '_' + mode + '.png')
    elif mode == "xz":
        for i in points:
            im.putpixel((int(i[0]), int(i[2])), 1)
        im.save(name + '_' + mode + '.png')
    elif mode == "yz":
        for i in points:
            im.putpixel((int(i[1]), int(i[2])), 1)
        im.save(name + '_' + mode + '.png')

if len(sys.argv) > 3:
    fle = file(sys.argv[1]).read().split()
    objlist = obj_parser.filter_obj(fle)
    if sys.argv[3] == 'all':
        makepic(objlist, sys.argv[2], "xy")
        makepic(objlist, sys.argv[2], "xz")
        makepic(objlist, sys.argv[2], "yz")
    else:
        makepic(objlist, sys.argv[2], sys.argv[3])
elif len(sys.argv) == 2:
    fle = file(sys.argv[1]).read().split()
    objlist = obj_parser.objread(fle)
    obj_triangle.make_tri(objlist[1])
else:
    print "Zu wenige Argumente"

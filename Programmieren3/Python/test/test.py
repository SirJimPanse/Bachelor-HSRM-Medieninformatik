# -*- coding: utf-8 -*-
import string, sys
l = ["ab2c3","a3b2c","a4b3c2defg","a20b2"]



def decompress(s):
    ret, nums = "", "0123456789"
    print list(enumerate(s + '\n'))
    for count, char in enumerate(s + '\n'):
        if char in nums:
            print string.atoi(char)-1
            for _ in range(0, string.atoi(char)-1):
                ret += s[count-1]
        else: ret += char
    return ret.strip()                   
        
for i in l:
    print decompress(i)



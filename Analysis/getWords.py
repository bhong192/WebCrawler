import re 
from operator import itemgetter
import numpy as np 
import matplotlib.pyplot as plt
from scipy import special 

frequency = {}
open_file = open('repository/Android Developers.html', 'r')

file_to_string = open_file.read()
words = re.findall(r'(\b[A-Za-z][a-z]{2,9}\b)', file_to_string)

for word in words: 
        count = frequency.get(word,0)
        frequency[word]= count +1

#for key, value in reversed(sorted(frequency.items(), key = itemgetter(1))): 
#   print(key,value) 

n = 1000
frequency = {key:value for key,value in frequency.items()}

s = list(frequency.items())
# s = frequency.values()
s = np.array(s)
print(s)

a = 2. 

for x in s: 
        count,bins, ignored = plt.hist(s[s[x*2]<50], 50, normed = True)

#count,bins, ignored = plt.hist(s[s<50], 50, normed = True)
x = np.arrange(1.,50.)
y = x**(-a) / special.zetac(a)
plt.plot(x, y/max(y), linewidth=2, color='r')
plt.show()
#!python3

import glob
import re
import os


dir_name = os.path.dirname(__file__)
regex = re.compile('\w+')
cache = {}

def text_in_dir_stats(root_dir='texts', maxfiles=9223372036854775807):
	pattern = os.path.join(root_dir, '**\*.*')
	dictionary = {}
	files = 0
	for filename in glob.iglob(os.path.join(dir_name, pattern), recursive=True):
		files += 1
		if files > maxfiles:
			break
		if filename in cache:
			words = cache[filename]
		else:
			words = regex.findall(open(filename, 'r').read())
		for word in words:
			if word == "s": # remove 's endings
				pass
			dictionary[word] = dictionary.get(word, 0) + 1
	return dictionary
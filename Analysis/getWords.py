import xlsxwriter
from stats import text_in_dir_stats

# Root directory with all texts
root_dir='repository'

# Check Zipf's law
print('Working on Zipf\'s law...')
dictionary = text_in_dir_stats(root_dir)
sorted_dict = sorted(dictionary.items(), key=lambda x: (x[1],x[0]), reverse=True)
workbook = xlsxwriter.Workbook('Zipf.xlsx')
worksheet = workbook.add_worksheet()
worksheet.write(0, 0, 'Rank')
worksheet.write(0, 1, 'Count')
worksheet.write(0, 2, 'Word')
row = 1
total_words = 0
for [key, value] in sorted_dict:
	worksheet.write(row, 0, row)
	worksheet.write(row, 1, value)
	worksheet.write(row, 2, key)
	total_words += value
	row += 1
workbook.close()
print('Done! Total words count: ' + str(total_words) + '\n')

# Check Heaps' law
print('Working on Heaps\' law...')
files = 5 # of files being read
workbook = xlsxwriter.Workbook('Heaps.xlsx')
worksheet = workbook.add_worksheet()
worksheet.write(0, 0, 'Texts analyzed')
worksheet.write(0, 1, 'Unique Word Count')
worksheet.write(0, 2, 'Total words in texts')
for max_files in range (1, files):
	dictionary = text_in_dir_stats(root_dir, max_files)
	worksheet.write(max_files, 0, max_files)
	worksheet.write(max_files, 1, len(dictionary))
	worksheet.write(max_files, 2, sum(dictionary.values()))
	print('Progress: ' + str(round(100 * max_files / files)) + '%', end='\r')
workbook.close()
print('\nDone!')

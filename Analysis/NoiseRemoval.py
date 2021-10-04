import requests
from bs4 import BeautifulSoup
import pandas as pd

directory = "../report.csv"
urlList = []
fileCounter = 1

# makes the columns of the csv file with the respective data
# and reads through the report.csv to get only the urls
# stores each url into a urlList
col_list = ['URL', 'Outlinks']
df = pd.read_csv(directory, usecols=col_list)
for i in df['URL']:
    urlList.append(i)

# iterates through each URL in the list
# and extracts the texts from them
for i in urlList:
    url = i
    res = requests.get(url)
    html_page = res.content
    soup = BeautifulSoup(html_page, 'html.parser')
    text = soup.find_all(text=True)

    output = ''
    blacklistTags = ['[document]', 'noscript', 'header', 'html', 'meta', 'head',
        'input', 'script', 'audio', 'button', 'img', 'input', 'nav', 'video',
        'script', 'style', 'a', 'link', 'footer', 'object', 'figure', 'track',
        'form'
        # there may be more elements you don't want, such as "style", etc.
    ]

    for t in text:
        if t.parent.name not in blacklistTags:
            output += '{} '.format(t)

    # saves each extracted text from each url into it's own txt file
    # in the test folder
    with open("test/File%s.txt" % fileCounter, 'w', newline='', encoding="utf-8") as outF:
        outF.writelines(output)
        fileCounter += 1

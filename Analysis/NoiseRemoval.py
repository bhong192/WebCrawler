import requests
from bs4 import BeautifulSoup

url = 'https://en.wikipedia.org/wiki/Java_(programming_language)'
res = requests.get(url)
html_page = res.content
soup = BeautifulSoup(html_page, 'html.parser')
text = soup.find_all(text=True)

output = ''
blacklistTags = ['[document]','noscript', 'header','html','meta','head',
    'input', 'script', 'audio', 'button', 'img', 'input', 'nav', 'video',
    'script', 'style', 'a', 'link', 'footer', 'object', 'figure', 'track',
    'form'
    # there may be more elements you don't want, such as "style", etc.
]

for t in text:
    if t.parent.name not in blacklistTags:
        output += '{} '.format(t)

print(output)

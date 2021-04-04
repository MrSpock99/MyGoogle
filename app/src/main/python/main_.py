from os.path import dirname, join
import nltk
#Необходимо прогнать эти строки при первом запуске


import pymorphy2
import codecs
import string
import re
import math

from bs4 import BeautifulSoup
from nltk import word_tokenize
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
lemmatizer = WordNetLemmatizer()
analyzer = pymorphy2.MorphAnalyzer(path=join(dirname(__file__), "pymorphy2-dicts-ru-2.4.417127.4579844/pymorphy2_dicts_ru/data"))

rus_filter = re.compile("[а-яА-Я]+")
eng_filter = re.compile("[a-zA-Z]+")

PAGES_PATH = join(dirname(__file__), "pages")
INDEX_PATH = join(dirname(__file__), "pages/index.txt")

# список символов, которые удаются из текста
MARKS = [',', '.', ':', '?', '«', '»', '-', '(', ')', '!', '\'', "—", ';', "”", "...", "\'\'", "/**//**/",
         "“", "„", "–"]

def parse_words_from_html(file):
    html_page = codecs.open(f"{PAGES_PATH}/{file}", 'r', 'utf-8')

    soup = BeautifulSoup(html_page.read(), features='html.parser')
    # kill all script, style, meta, links, span, a, time, button, li, dt, h2, h3, legend elements
    for script in soup(
            ["script", "style", "meta", "link", "span", "a", "time", "button", "li", "dt", "h2", "h3", "legend"]):
        script.extract()  # rip it out

    # get text
    text = soup.get_text()
    # break into lines and remove leading and trailing space on each
    lines = (line.strip() for line in text.splitlines())
    # break multi-headlines into a line each
    chunks = (phrase.strip() for line in lines for phrase in line.split("  "))
    # drop blank lines
    text = '\n'.join(chunk for chunk in chunks if chunk)
    # убираем знаки препинания
    pattern = r"[^\w]"
    text = re.sub(pattern, " ", text)
    # split to words
    return text.split()

def tokenize():
    words = []
    docs_dict = {}
    with open(INDEX_PATH, "r") as index:
        lines = index.readlines()
        numbers = [line[: line.find(" ")] for line in lines]
        for num in numbers:
            words.extend([[parse_words_from_html(f"{num}.txt"), num]])
            docs_dict[int(num)] = 0
        with open(join(dirname(__file__), "words.txt"), "r", encoding="utf-8") as file:
            for item in words:
                filtered_words = filter_words(item[0])
                for word in filtered_words:
                    docs_dict[int(item[1])] += 1
                    #file.write(word + ' ' + item[1] + '\n')
    return docs_dict

def filter_words(words):
    words = list(filter(lambda word: (word not in string.punctuation) and (word not in MARKS), words))
    filtered_words = []
    for word in words:
        filtered_words.append("".join(filter(lambda char: char not in MARKS, word)))
    stop_words_rus = set(stopwords.words('russian'))
    stop_words_eng = set(stopwords.words('english'))
    filtered_words = [w.lower() for w in filtered_words if w not in stop_words_rus]
    filtered_words = [w.lower() for w in filtered_words if w not in stop_words_eng]
    filtered_words = filter(lambda word: word.isalpha(), filtered_words)
    return filtered_words

def lemmatize(docs_dict):
    with open(join(dirname(__file__), "words.txt"), "r", encoding="utf-8") as words_file:
        lines = words_file.readlines()

    rus_lines = [line for line in filter(rus_filter.match, lines)]
    eng_lines = [line for line in filter(eng_filter.match, lines)]

    rus_words = []
    eng_words = []

    for line in rus_lines:
        rus_words.append(line.strip().split())
    for line in eng_lines:
        eng_words.append(line.strip().split())

    lem_dict = {}
    index_dict = {}
    token_count_dict = {}
    #print(rus_words)
    #print(eng_words)

    for word in rus_words:
        # получаем начальную форму слова
        normal_form = get_normal_form_rus(word[0])
        if normal_form:
            # если такое слово еще не встречалось,создаем ключ с нормальной формой и помещаем само слово как значение
            if normal_form not in lem_dict.keys():
                lem_dict[normal_form] = [word[0].strip()]
                index_dict[normal_form] = [int(word[1])]
            # если такое слово встречалось ранее, добавляем его вариацию в список значений
            else:
                if(int(word[1]) not in index_dict[normal_form]):
                    index_dict[normal_form].append(int(word[1]))
                if(word[0] not in lem_dict[normal_form]):
                    lem_dict[normal_form].append(word[0].strip())
            if (normal_form not in token_count_dict.keys()):
                token_count_dict[normal_form] = {}
                token_count_dict[normal_form][int(word[1])] = 1
            else:
                if (int(word[1]) not in token_count_dict[normal_form].keys()):
                    token_count_dict[normal_form][int(word[1])] = 1
                else:
                    token_count_dict[normal_form][int(word[1])] += 1

    for word in eng_words:
        # получаем начальную форму слова
        normal_form = get_normal_form_eng(word[0])
        if normal_form:
            # если такое слово еще не встречалось,создаем ключ с нормальной формой и помещаем само слово как значение
            if normal_form not in lem_dict.keys():
                lem_dict[normal_form] = [word[0].strip()]
                index_dict[normal_form] = [int(word[1])]
            # если такое слово встречалось ранее, добавляем его вариацию в список значений
            else:
                if(int(word[1]) not in index_dict[normal_form]):
                    index_dict[normal_form].append(int(word[1]))
                if(word[0] not in lem_dict[normal_form]):
                    lem_dict[normal_form].append(word[0].strip())
            if (normal_form not in token_count_dict.keys()):
                token_count_dict[normal_form] = {}
                token_count_dict[normal_form][int(word[1])] = 1
            else:
                if (int(word[1]) not in token_count_dict[normal_form].keys()):
                    token_count_dict[normal_form][int(word[1])] = 1
                else:
                    token_count_dict[normal_form][int(word[1])] += 1

    for key in index_dict.keys():
        index_dict[key].sort()

    tf_dict = {}
    for token in token_count_dict.keys():
        tf_dict[token] = {}
        for doc in token_count_dict[token].keys():
            token_count =  token_count_dict[token][doc]
            total_words_count = docs_dict[doc]
            tf = token_count / total_words_count
            tf_dict[token][doc] = tf

    return(lem_dict, index_dict, tf_dict)

def get_normal_form_rus(word):
    return analyzer.parse(word)[0].normal_form

def get_normal_form_eng(word):
    return lemmatizer.lemmatize(word)

# записываем полученные результаты в формате:
# "начальная форма слова: токен токен ..."
# знак ":" служит разделителем между ключом и значениями
def save_lemmatized_tokens_to_file(lem_dict):
    file = open(join(dirname(__file__), "lemmatized_tokens.txt"), "w", encoding="utf-8")
    for word, tokens in lem_dict.items():
        file.write(f"{word}:")
        [file.write(f" {tok}") for tok in set(tokens)]
        file.write("\n")
    file.close()

def save_inverted_index_to_file(index_dict):
    file = open(join(dirname(__file__), "inverted_index.txt"), "w", encoding="utf-8")
    for word, indexes in index_dict.items():
        file.write(f"{word}")
        [file.write(f" {index}") for index in set(indexes)]
        file.write("\n")
    file.close()

def save_tf_idf_to_file(tf_idf_dict):
    file = open("tf_idf.txt", "w", encoding="utf-8")
    for token in tf_idf_dict.keys():
        for doc in tf_idf_dict[token].keys():
            file.write(f"{token} {doc} {tf_idf_dict[token][doc][0]} {tf_idf_dict[token][doc][1]}")
            file.write("\n")
    file.close()

def split_to_words(text):
    # убираем знаки препинания
    pattern = r"[^\w]"
    text = re.sub(pattern, " ", text)

    # создаем массив слов
    words = text.split()

    # убираем стоп-слова, числа и ненужные символы
    words = filter_words(words)
    return words

# получение нормальных форм слов
def get_normal_words(words):
    # группируем русские и ангийские слова отдельно
    rus_words = [word for word in filter(rus_filter.match, words)]
    eng_words = [word for word in filter(eng_filter.match, words)]

    normal_words = []

    # добавляем начальные формы русских слов в список
    for word in rus_words:
        normal_words.append(get_normal_form_rus(word))
    # добавляем начальные формы английских слов в список
    for word in eng_words:
        normal_words.append(get_normal_form_eng(word))

    return normal_words

# Булев поиск
def boolean_search(text, index_dict):
    words = split_to_words(text)

    # нормализуем слова
    normal_words = get_normal_words(words)

    # получаем список страниц, в которых встречаются слова из текста
    page_numbers = []
    for word in normal_words:
        if(word in index_dict.keys()):
            page_numbers.extend(index_dict[word])

    #это чтобы убрать дупликаты
    page_numbers = list(dict.fromkeys(page_numbers))

    page_numbers.sort()
    return page_numbers

# Словарь с idf каждого токена
def idf(docs_count, index_dict):
    idf_dict = {}
    for token in index_dict.keys():
        idf = math.log(docs_count / len(index_dict[token]))
        idf_dict[token] = idf
    return idf_dict

# Словарь с tf-idf для каждого документа каждого токена
def tf_idf(tf_dict, idf_dict):
    tf_idf_dict = {}
    for token in tf_dict.keys():
        tf_idf_dict[token] = {}
        for doc in tf_dict[token].keys():
            tf_idf = tf_dict[token][doc] * idf_dict[token]
            tf_idf_dict[token][doc] = [idf_dict[token], tf_idf]
    return tf_idf_dict

# Словарь с tf-idf для каждого токена каждого документа
def get_docs_tf_idf(tf_idf_dict, docs_dict):
    docs_tf_idf_dict = dict.fromkeys(docs_dict.keys())

    for doc in docs_tf_idf_dict.keys():
        docs_tf_idf_dict[doc] = {}

    for token in tf_idf_dict.keys():
        for doc in tf_idf_dict[token].keys():
            docs_tf_idf_dict[doc][token] = tf_idf_dict[token][doc][1]

    return docs_tf_idf_dict

# Словарь с длинами каждого документа (для векторного поиска)
def lengths(docs_tf_idf_dict):
    lengths_dict = dict.fromkeys(docs_tf_idf_dict.keys())
    for doc in docs_tf_idf_dict.keys():
        tf_idf_sum = 0
        for token in docs_tf_idf_dict[doc].keys():
            tf_idf_sum += docs_tf_idf_dict[doc][token] * docs_tf_idf_dict[doc][token]
        lengths_dict[doc] = math.sqrt(tf_idf_sum)
    return lengths_dict

def vector_space_model(text, index_dict, idf_dict, lengths_dict, docs_tf_idf_dict):
    # список слов из текста
    words = split_to_words(text)

    # нормализуем слова
    normal_words = get_normal_words(words)

    # список страниц, в которых встречаются слова из текста
    pages = boolean_search(text, index_dict)

    # количество слов в тексте
    words_count = len(normal_words)

    # словарь, содержащий количество каждого слова в тексте
    words_count_dict = {}
    for word in normal_words:
        if(word not in words_count_dict.keys()):
            words_count_dict[word] = 1
        else:
            words_count_dict[word] += 1

    # tf каждого слова в тексте
    tf_dict = dict.fromkeys(words_count_dict.keys())
    for word in words_count_dict.keys():
        tf_dict[word] = words_count_dict[word] / words_count

    # tf-idf каждого слова в тексте
    tf_idf_dict = dict.fromkeys(tf_dict.keys())
    for word in tf_dict.keys():
        tf_idf_dict[word] = tf_dict[word] * idf_dict.get(word, 0.0)

    # Длина текста
    tf_idf_sum = 0
    for word in tf_idf_dict.keys():
        tf_idf_sum += tf_idf_dict[word] * tf_idf_dict[word]
    text_length = math.sqrt(tf_idf_sum)

    cos_sim_dict = dict.fromkeys(pages)
    # cosine similarity
    for page_num in pages:
        cos_sim_dict[page_num] = cosine_similarity(tf_idf_dict, docs_tf_idf_dict[page_num], text_length, lengths_dict[page_num])

    cos_sim_list = sorted(cos_sim_dict.items(), key=lambda page: page[1], reverse=True)

    sorted_pages_list = []
    for item in cos_sim_list:
        sorted_pages_list.append(item[0])
    return sorted_pages_list

def cosine_similarity(tf_idf_dict_X, tf_idf_dict_Y, length_X, length_Y):
    cos_sim_sum = 0
    for token in tf_idf_dict_X.keys():
        if (token in tf_idf_dict_Y.keys()):
            cos_sim_sum += tf_idf_dict_X[token] + tf_idf_dict_Y[token]
    cos_sim = cos_sim_sum / (length_X * length_Y)
    return cos_sim
lem_dict_global = 0
index_dict_global = 0
tf_dict_global = 0
idf_dict_global = 0
tf_idf_dict_global = 0
lengths_dict_global = 0
docs_tf_idf_dict_global = 0

def main():
    nltk.download('stopwords')
    nltk.download('punkt')
    nltk.download('wordnet')

    docs_dict = tokenize()
    global lem_dict_global, index_dict_global, tf_dict_global
    lem_dict_global, index_dict_global, tf_dict_global = lemmatize(docs_dict)
    docs_count = len(docs_dict.keys())
    global idf_dict_global
    idf_dict_global = idf(docs_count, index_dict_global)

    global tf_idf_dict_global
    tf_idf_dict_global = tf_idf(tf_dict_global, idf_dict_global)
    #save_lemmatized_tokens_to_file(lem_dict)
    #save_inverted_index_to_file(index_dict)
    #save_tf_idf_to_file(tf_idf_dict)

    global docs_tf_idf_dict_global
    docs_tf_idf_dict_global = get_docs_tf_idf(tf_idf_dict_global, docs_dict)
    global lengths_dict_global
    lengths_dict_global = lengths(docs_tf_idf_dict_global)

def search(text):
    pages_list = vector_space_model(text, index_dict_global, idf_dict_global, lengths_dict_global, docs_tf_idf_dict_global)
    return pages_list
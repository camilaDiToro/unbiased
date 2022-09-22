import os
from colorama import Fore
import sys


ignore_chars = ['\t', ' ', '\n', ':', '*', '#']

def is_blank(character): 
    return character in ignore_chars

def i18n_parser(file):
    in_tag = 0
    in_script = True 
    jsp = open(str(file), "r")
    linenr = 0
    for line in jsp:

        if "<script>" in str(line): 
            if js:
                print(Fore.BLUE+"[JS] Skipping <script> at{1}[{0}]\n".format(linenr, file)) 
            in_script = True
            
        if "</script>" in str(line) and js: 
            in_script = False
            continue

        for character in line:
            if not in_script:
                if character == '<':
                    in_tag += 1
                elif character == '>':
                    in_tag -= 1
                elif str(line).lstrip().startswith("<%--"):
                    break
                elif not is_blank(character) and in_tag == 0:
                    if character == "$":
                        print(Fore.RED+"[DANGER] Possible free variable at {1}[{0}]:".format(linenr, file))
                    else: 
                        print(Fore.YELLOW+"[WARN] Possible i18n violation at {1}[{0}]:".format(linenr, file))
                    print(Fore.WHITE+line.lstrip())
                    break
        linenr += 1
    
 
def is_jsp(file):
    return (str(file).endswith('.jsp'))

def iterate_through_directory(dir):    
    for filename in os.listdir(dir):
        f = os.path.join(dir, filename)
        if os.path.isfile(f) and is_jsp(f):
            i18n_parser(f)
        elif os.path.isdir(f):
            iterate_through_directory(f)

js = len(sys.argv) >= 3 and sys.argv[2] == "-js"
directory = sys.argv[1]
iterate_through_directory(directory)
print(Fore.GREEN+"[DONE] :)\n")



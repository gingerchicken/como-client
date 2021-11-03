import os

FEATURES_DIR = "src/main/java/net/como/client/cheats"

class NotJavaPath(Exception):
    """The path specified was not a Java file and hence must not be a feature."""

class Feature:
    # Just incase we want to easily change it in the future.
    __desc_selector = "this.description = \""

    def __init__(self, path):
        if not path.endswith(".java"):
            raise NotJavaPath(path)

        self.__path = path

    def get_name(self):
        return os.path.basename(self.__path)[:-len(".java")]

    def __get_code(self):
        f = open(self.__path)
        code = f.read()
        f.close()

        return code

    def __desc_index(self, code):
        if code == None:
            code = self.__get_code()

        i = -1
        try:
            i = code.index(self.__desc_selector)
        except:
            pass

        return i

    def get_description(self, default):
        code = self.__get_code()
        start = self.__desc_index(code)

        if start == -1: return default

        start += len(self.__desc_selector)
        
        # Get all of the string util we finish the line
        c = ""
        i = 0

        # This might cause issues since I might do something weird rather than ";\n at the end of a string.
        # So just be warned
        while not c.endswith("\";\n"):
            c += code[start + i]
            i += 1

        # Remove ";
        c = c[:-3]

        # Add a fullstop
        c + "." if not c.endswith(".") else c

        return c

features = [Feature(os.path.join(FEATURES_DIR, i)) for i in os.listdir(FEATURES_DIR)]

print(len(features))
for feature in features:
    print(feature.get_name(), " - ", feature.get_description("No description available as of yet."))
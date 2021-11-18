import os

FEATURES_DIR = "src/main/java/net/como/client/cheats"
TARGET_PATH = "FEATURES.md"
NOT_PRESENT = "No description available as of yet."

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
    
    # TODO maybe get the settings
    # TODO add command to activate the cheat
    # TODO populate the file with screenshots

    def get_readme_line(self, not_present) -> str():
        line = ""

        line += f"## {self.get_name()}\n"
        line += f"[(Source Code)]({self.__path}) "
        line += self.get_description(not_present)

        return line

features = [Feature(os.path.join(FEATURES_DIR, i)) for i in os.listdir(FEATURES_DIR)]

print(f"Detected {len(features)} features... Generating Feature List...")
output = '''# List of Features\n'''
for feature in features:
    output += feature.get_readme_line(NOT_PRESENT) + '\n'

print(output)

print(f"Saving to {TARGET_PATH}...")
f = open(TARGET_PATH, 'w')
f.write(output)
f.close()
print("Finished.")
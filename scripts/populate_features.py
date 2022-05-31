from enum import Enum
import os
import hashlib
import json
from pathlib import Path

FEATURES_DIR = "src/main/java/net/como/client/modules"
TARGET_PATH = "FEATURES.md"
NOT_PRESENT = "No description available as of yet."

class NotJavaPath(Exception):
    """The path specified was not a Java file and hence must not be a feature."""

class SettingType(Enum):
    STRING = 1,
    STR_BOOL_HASHMAP = 2,
    FLOAT = 3,
    BOOLEAN = 4,
    OTHER = 5,
    INTEGER = 6
    MODE = 7

class Setting:
    def __init__(self, name, default) -> None:
        self.__name = name
        self.__default = default

    def get_type(self):
        v = self.get_default_value()

        if v.startswith("\"") and v.endswith("\""):
            return SettingType.STRING
        
        # Floats/Doubles
        if (v.endswith("d") or v.endswith("f")) and v[:-1].replace('.','',1).isdigit():
            return SettingType.FLOAT

        # Ints
        if v.isdigit():
            return SettingType.INTEGER

        # Boolean
        if v == "true" or v == "false":
            return SettingType.BOOLEAN

        # HashMap thing
        if v == "new HashMap<String, Boolean>()":
            return SettingType.STR_BOOL_HASHMAP
        
        if v.startswith("new Mode("):
            return SettingType.MODE

        return SettingType.OTHER

    def get_name(self):
        return self.__name

    def get_default_value(self):
        return self.__default

    def get_parsed_default(self):
        setting_type = self.get_type()
        v = self.get_default_value()

        if setting_type == SettingType.FLOAT:
            return float(v.replace("d", "").replace("f", ""))
        
        if setting_type == SettingType.INTEGER:
            return int(v)
        
        if setting_type == SettingType.BOOLEAN:
            return bool(v)

        if setting_type == SettingType.STR_BOOL_HASHMAP:
            return "{ ... }"
        
        if setting_type == SettingType.STRING:
            return str(v)

        if setting_type == SettingType.MODE:
            return self.get_mode_items(v)

        return v

    def get_mode_items(self, value=str()):
        # Remove new Mode(
        value = value[len("new Mode("):]
        
        # Remove )
        value = value[:-1]

        # Now for the dodgy bit
        return json.loads(f"[{value}]") 


    @staticmethod
    def from_line(line = str()):
        line = line.strip()

        line = line.replace("this.addSetting(new Setting(", "")
        line = line.replace("));", "")

        # Parse the name
        name = line[1:]
        name = name[:name.find("\"")]

        # Parse the value
        default = line[len("\"\",") + len(name):].strip()

        # Handle description setting
        # TODO get this and display it
        # ... but for now I will just remove it
        default = default.split(') {')[0]

        return Setting(name, default)

class Feature:
    # Just incase we want to easily change it in the future.
    __desc_selector = "this.setDescription(\""

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
        while not c.endswith("\");\n"):
            c += code[start + i]
            i += 1

        # Remove ";
        c = c[:-4]

        # Add a fullstop
        c + "." if not c.endswith(".") else c

        return c
    
    # TODO populate the file with screenshots

    def get_command(self):
        return f".{self.get_name().lower()}"

    def get_readme_line(self, not_present) -> str():
        line = ""

        # Display basic information
        line += f"## {self.get_name()} (`{self.get_command()}`)\n"
        line += f"[(Source Code)]({self.__path}) "
        line += self.get_description(not_present) + "\n"

        # Display settings
        settings = self.get_settings()
        if len(settings) > 0:
            line += "### Default Settings\n"
            for setting in settings:
                default = setting.get_parsed_default()

                line += f" - {setting.get_name()}"
                if type(default) == list:
                    for item in default:
                        line += f"\n    - {item}"
                    line += "\n"
                else:
                    line += f": `{default}`\n"

        return line

    def get_settings(self):
        code = self.__get_code()
        selector = "this.addSetting(new Setting("

        settings = []
        
        i = code.find(selector)
        while i != -1:
            part = c =''
            j = 0

            while c != ';':
                c = code[i + j]
                part += c

                j += 1
            settings.append(Setting.from_line(part))

            i = code.find(selector, i + 1)

        settings.sort(key=lambda x: x.get_name())
        return settings

# Scan for all of the features
features = [Feature(str(path)) for path in Path(FEATURES_DIR).rglob("*.java") if path.name not in ["Module.java", "DummyModule.java"]]

# Sort them alphabetically
features.sort(key=lambda x: x.get_name())

print(f"Detected {len(features)} features... Generating Feature List...")

# Generate the new FEATURES.md
output = '''# List of Features\n'''
for feature in features:
    output += feature.get_readme_line(NOT_PRESENT) + '\n'

# See if there was a change
# Hashing function
def hash(x):
    return hashlib.md5(x.encode()).hexdigest()

# Read the file
f = open(TARGET_PATH, 'r')
old_output = f.read()
f.close()

# Make the hashes
old_hash = hash(old_output)
new_hash = hash(output)

print(f"{TARGET_PATH}: {old_hash} -> {new_hash}")

# Compare the hashes
if old_hash != new_hash:
    # Save the new FEATURES.md to a file
    f = open(TARGET_PATH, 'w')
    f.write(output)
    f.close()

    print("Changes made and saved.")
else:
    print("There are no changes detected. Aborting...")
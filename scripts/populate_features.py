class NotJavaPath(Exception):
    """The path specified was not a Java file and hence must not be a feature."""

class Feature:
    def __init__(self, path):
        self.__path = path
